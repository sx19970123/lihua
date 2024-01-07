package com.lihua.system.mapper.menu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.system.entity.menu.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<String> selectPermsByUserId(String userId);
}
