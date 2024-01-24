package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.model.security.SysUserVO;

public interface SysUserMapper extends BaseMapper<SysUserVO> {
    SysUserVO selectByUsername(String username);
}
