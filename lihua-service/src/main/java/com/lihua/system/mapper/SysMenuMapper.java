package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.entity.SysMenu;
import com.lihua.model.SysMenuVO;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenuVO> selectPermsByUserId(String userId);
}
