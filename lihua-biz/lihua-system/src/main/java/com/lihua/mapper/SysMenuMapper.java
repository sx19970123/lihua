package com.lihua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.entity.SysMenu;
import com.lihua.security.model.CurrentRouter;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    // 根据用户id查询权限下的菜单信息
    List<CurrentRouter> selectPermsByUserId(String userId);

    // 查询所有菜单信息（admin）
    List<CurrentRouter> selectAllPerms();
}
