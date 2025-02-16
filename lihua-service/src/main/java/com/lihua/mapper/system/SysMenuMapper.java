package com.lihua.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.model.security.CurrentRouter;
import com.lihua.entity.system.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    // 根据用户id查询权限下的菜单信息
    List<CurrentRouter> selectPermsByUserId(String userId);

    // 查询所有菜单信息（admin）
    List<CurrentRouter> selectAllPerms();

    // 根据parentId将 >= 当前sort的数据sort + 1
    long peerMenuSortAddOne(String parentId, int sort);

    // 根据parentId将 符合条件的sort - 1
    long peerMenuSortSubtractOne(String parentId, Integer lastSort, List<Integer> missingSortList);
}
