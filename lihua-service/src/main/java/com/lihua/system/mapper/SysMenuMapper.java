package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.model.security.CurrentRouter;
import com.lihua.system.entity.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<CurrentRouter> selectPermsByUserId(String userId);
}
