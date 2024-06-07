package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.model.security.CurrentRouter;
import com.lihua.system.entity.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    // 根据用户id查询权限下的菜单信息
    List<CurrentRouter> selectPermsByUserId(String userId);

    // 查询所有菜单信息（admin）
    List<CurrentRouter> selectAllPerms();
}
