package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUserVO selectByUsername(String username);
}
