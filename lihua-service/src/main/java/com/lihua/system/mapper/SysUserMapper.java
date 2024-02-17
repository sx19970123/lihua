package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.entity.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUserVO selectByUsername(String username);
}
