package com.lihua.service.system.menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.exception.ServiceException;
import com.lihua.entity.system.SysMenu;
import com.lihua.mapper.system.SysMenuMapper;
import com.lihua.mapper.system.SysRoleMapper;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.tree.TreeUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysMenu> queryList(SysMenu sysMenu) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(sysMenu.getLabel())) {
            queryWrapper.lambda().like(SysMenu::getLabel,sysMenu.getLabel());
        }

        if (StringUtils.hasText(sysMenu.getStatus())) {
            queryWrapper.lambda().eq(SysMenu::getStatus,sysMenu.getStatus());
        }

        if (StringUtils.hasText(sysMenu.getMenuType())) {
            queryWrapper.lambda().eq(SysMenu::getMenuType,sysMenu.getMenuType());
        }

        queryWrapper.lambda().orderByAsc(SysMenu::getSort);

        List<SysMenu> sysMenus = sysMenuMapper.selectList(queryWrapper);
        return TreeUtils.buildTree(sysMenus);
    }

    @Override
    public SysMenu queryById(String menuId) {
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
        sysMenu.setCreateTime(DateUtils.now());
        sysMenu.setDelFlag("0");
        // 同级菜单sort + 1 TODO 未达到预期效果，后续再进行完善
        // sysMenuMapper.peerMenuSortAddOne(sysMenu.getParentId(), sysMenu.getSort());
        sysMenuMapper.insert(sysMenu);
        return sysMenu.getId();
    }

    private String update(SysMenu sysMenu) {
        sysMenu.setUpdateId(LoginUserContext.getUserId());
        sysMenu.setUpdateTime(DateUtils.now());
        sysMenuMapper.updateById(sysMenu);
        return sysMenu.getId();
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> ids) {
        checkStatus(ids);
        checkChildren(ids);
        // 删除前查询菜单的parentId和sort
        //List<SysMenu> sysMenus = queryDeleteMenuSortInfo(ids);
        // 删除菜单
        sysMenuMapper.deleteByIds(ids);
        // 删除角色关联表数据
        deleteRoleMenu(ids);
        // 处理删除后同级重新排序 TODO 未达到预期效果，后续再进行完善
        // peerMenuReSort(sysMenus);
    }

    @Override
    public List<SysMenu> menuTreeOption() {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setStatus("0");
        return queryList(sysMenu);
    }

    @Override
    public String updateStatus(List<String> ids, String currentStatus) {
        UpdateWrapper<SysMenu> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";

        updateWrapper.lambda()
                .set(SysMenu::getStatus, status)
                .set(SysMenu::getUpdateId, LoginUserContext.getUserId())
                .set(SysMenu::getUpdateTime, DateUtils.now())
                .in(SysMenu::getId, ids);
        sysMenuMapper.update(null, updateWrapper);
        return status;
    }


    private List<SysMenu> queryDeleteMenuSortInfo(List<String> ids) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SysMenu::getId, ids)
                .select(SysMenu::getParentId, SysMenu::getSort);
        return sysMenuMapper.selectList(queryWrapper);
    }

    /**
     * 处理同级菜单重新排序
     */
    private void peerMenuReSort(List<SysMenu> sysMenus) {
       if (sysMenus.isEmpty()) {
           return;
       }

        Map<String, List<SysMenu>> groupByParentId = sysMenus.stream().collect(Collectors.groupingBy(SysMenu::getParentId));

        groupByParentId.forEach((parentId, pIdSortList) -> {
            if (pIdSortList.isEmpty()) {
                return;
            }
            List<Integer> sortList = pIdSortList.stream().map(SysMenu::getSort).sorted().toList();

            // 获取到中间不连续的sort
            List<Integer> missingSortList = IntStream
                    .rangeClosed(sortList.get(0), sortList.get(sortList.size() - 1))
                    .filter(n -> !sortList.contains(n))
                    .boxed()
                    .toList();
            // 对目标sort的菜单数据sort - 1
            sysMenuMapper.peerMenuSortSubtractOne(parentId, sortList.get(sortList.size() - 1), missingSortList);
        });
    }

    /**
     * 验证删除数据是否有未删除的子集
     */
    private void checkChildren(List<String> ids) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SysMenu::getParentId,ids)
                .eq(SysMenu::getDelFlag,"0")
                .select(SysMenu::getId);
        List<SysMenu> sysMenus = sysMenuMapper.selectList(queryWrapper);

        if (sysMenus.isEmpty()) {
            return;
        }

        // 对比以删除节点为父节点的数据，当这些数据全部与删除的数据相同，则要删除的数据中没有子节点存在
        List<String> list = new java.util.ArrayList<>(sysMenus.stream().map(SysMenu::getId).toList());
        list.removeAll(ids);

        if (!list.isEmpty()) {
            throw new ServiceException("菜单存在子集不允许删除");
        }
    }

    /**
     * 删除角色关联表数据
     */
    private void deleteRoleMenu(List<String> ids) {
        sysRoleMapper.deleteRoleMenuByMenuIds(ids);
    }

    /**
     * 验证菜单状态
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
