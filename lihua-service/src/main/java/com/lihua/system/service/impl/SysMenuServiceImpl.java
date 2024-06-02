package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.CurrentRouter;
import com.lihua.system.entity.SysMenu;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.mapper.SysRoleMapper;
import com.lihua.system.service.SysMenuService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.tree.TreeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;

    private final String patternComponentName =  "([^/]+)\\.vue$";

    @Override
    public List<SysMenu> findList(SysMenu sysMenu) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(sysMenu.getLabel())) {
            queryWrapper.lambda().like(SysMenu::getLabel,sysMenu.getLabel());
        }

        if (StringUtils.hasText(sysMenu.getStatus())) {
            queryWrapper.lambda().eq(SysMenu::getStatus,sysMenu.getStatus());
        }

        queryWrapper.lambda().orderByAsc(SysMenu::getSort);

        List<SysMenu> sysMenus = sysMenuMapper.selectList(queryWrapper);
        // 构建树形结构
        return TreeUtil.lambda().buildTree(
                sysMenus,
                SysMenu::getId,
                SysMenu::getParentId,
                SysMenu::getChildren,
                SysMenu::setChildren);
    }

    @Override
    public SysMenu findById(String menuId) {
        return sysMenuMapper.selectById(menuId);
    }

    @Override
    public String save(SysMenu sysMenu) {
        sysMenu.setTitle(sysMenu.getLabel());
        sysMenu.setPerms(StringUtils.hasText(sysMenu.getPerms()) ? sysMenu.getPerms() : sysMenu.getMenuType());

        // 菜单id为 null，执行insert
        if (!StringUtils.hasText(sysMenu.getId())) {
           return insert(sysMenu);
        }
        return update(sysMenu);
    }

    private String insert(SysMenu sysMenu) {
        sysMenu.setCreateId(LoginUserContext.getUserId());
        sysMenu.setCreateTime(LocalDateTime.now());
        sysMenu.setDelFlag("0");
        sysMenuMapper.insert(sysMenu);
        return sysMenu.getId();
    }

    private String update(SysMenu sysMenu) {
        sysMenu.setUpdateId(LoginUserContext.getUserId());
        sysMenu.setUpdateTime(LocalDateTime.now());
        sysMenuMapper.updateById(sysMenu);
        return sysMenu.getId();
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> ids) {
        checkStatus(ids);
        checkChildren(ids);
//        checkRole(ids);
        deleteRoleMenu(ids);
        sysMenuMapper.deleteBatchIds(ids);
    }

    @Override
    public List<CurrentRouter> selectSysMenuByLoginUserId(String userId) {
        List<CurrentRouter> currentRouterList = sysMenuMapper.selectPermsByUserId(userId);
        // 不需要权限数据
        currentRouterList = currentRouterList
                .stream()
                .filter(vo -> !vo.getType().equals("perms"))
                .peek(vo -> {
                    // 使用正则表达式从组件路径中获取组件名称
                    String component = vo.getComponent();
                    if (component != null) {
                        Pattern pattern = Pattern.compile(patternComponentName);
                        Matcher matcher = pattern.matcher(component);
                        if (matcher.find()) {
                            String name = matcher.group(1);
                            if (StringUtils.hasText(name)) {
                                vo.setName(name);
                            }
                        }
                    }
                })
                .collect(Collectors.toList());
        // 递归构建树
        List<CurrentRouter> routerList = TreeUtil.buildTree(currentRouterList);
        // 设置层级key，再通过key设置path
        handleRouterPathKey(routerList, null);
        return routerList;
    }

    @Override
    public List<SysMenu> menuTreeOption() {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setStatus("0");
        return findList(sysMenu);
    }

    // 处理 routerPathKey
    private void handleRouterPathKey(List<CurrentRouter> routerList, String parentKey) {
        for (CurrentRouter item : routerList) {
            String key = item.getPath().startsWith("/") ? item.getPath() : "/" + item.getPath();
            // 根据菜单层级关系设置key
            if ("0".equals(item.getParentId())) {
                item.setKey(key);
            } else if (parentKey != null){
                item.setKey(parentKey + key);
            }
            // 设置path
            item.setPath(item.getKey());
            // 存在子集继续递归
            if (item.getChildren() != null && !item.getChildren().isEmpty()) {
               handleRouterPathKey(item.getChildren(),item.getKey());
            }
        }
    }

    /**
     * 验证删除数据是否有未删除的子集
     * @param ids
     */
    private void checkChildren(List<String> ids) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysMenu::getParentId,ids)
                .eq(SysMenu::getDelFlag,"0");
        Long count = sysMenuMapper.selectCount(queryWrapper);

        if (count != 0) {
            throw new ServiceException("菜单存在子集不允许删除");
        }
    }

    /**
     * 验证删除数据是否已绑定角色
     * @param ids
     */
    private void checkRole(List<String> ids) {
        Long menuCount = sysRoleMapper.selectRoleMenuCount("menu_id", ids);

        if (menuCount > 0) {
            throw new ServiceException("菜单已绑定角色不允许删除");
        }
    }

    /**
     * 删除角色关联表数据
     * @param ids
     */
    private void deleteRoleMenu(List<String> ids) {
        sysRoleMapper.deleteRoleMenuByMenuIds(ids);
    }

    /**
     * 验证菜单状态
     * @param ids
     */
    private void checkStatus(List<String> ids) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysMenu::getId,ids)
                        .eq(SysMenu::getStatus,"0");
        Long count = sysMenuMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("菜单状态为正常不允许删除");
        }
    }
}
