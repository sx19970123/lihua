package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.RouterVO;
import com.lihua.system.entity.SysMenu;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.model.SysMenuDTO;
import com.lihua.system.service.SysMenuService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.tree.TreeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> findList(SysMenuDTO sysMenuDTO) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(sysMenuDTO.getLabel())) {
            queryWrapper.lambda().like(SysMenu::getLabel,sysMenuDTO.getLabel());
        }

        if (StringUtils.hasText(sysMenuDTO.getStatus())) {
            queryWrapper.lambda().eq(SysMenu::getStatus,sysMenuDTO.getStatus());
        }

        List<SysMenu> sysMenus = sysMenuMapper.selectList(queryWrapper);

        // 构建树形结构
        return TreeUtil.buildTree(sysMenus);
    }

    @Override
    public SysMenu findById(String menuId) {
        return sysMenuMapper.selectById(menuId);
    }

    @Override
    public String save(SysMenu sysMenu) {
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
    public void deleteByIds(List<String> ids) {
        checkChildren(ids);
        sysMenuMapper.deleteBatchIds(ids);
    }

    @Override
    public List<RouterVO> selectSysMenuByLoginUserId(String userId) {
        List<RouterVO> sysMenuVOS = sysMenuMapper.selectPermsByUserId(userId);
        // 递归构建树
        List<RouterVO> routerVOList = TreeUtil.buildTree(sysMenuVOS);
        // 设置层级key，再通过key设置path 和 组件name
        handleRouterPathKey(routerVOList, null);
        return routerVOList;
    }
    // 处理 routerPathKey
    private void handleRouterPathKey(List<RouterVO> routerVOList,String parentKey) {
        for (RouterVO item : routerVOList) {
            String key = item.getPath().startsWith("/") ? item.getPath() : "/" + item.getPath();
            // 根据菜单层级关系设置key
            if ("0".equals(item.getParentId())) {
                item.setKey(key);
            } else if (parentKey != null){
                item.setKey(parentKey + key);
            }
            String fullKey = item.getKey();
            // 设置path
            item.setPath(fullKey);
            // 设置组件name
            String[] keyWords = fullKey.split("/");
            StringBuilder name = new StringBuilder();
            for (String word : keyWords) {
                String upperCaseWord = com.lihua.utils.string.StringUtils.initialUpperCase(word);
                name.append(upperCaseWord);
            }

            item.setName(name.toString());
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
            throw new ServiceException("存在子集不允许删除");
        }
    }

}
