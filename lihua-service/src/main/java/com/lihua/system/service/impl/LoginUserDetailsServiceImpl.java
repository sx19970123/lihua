package com.lihua.system.service.impl;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.security.LoginUser;
import com.lihua.model.security.RouterVO;
import com.lihua.model.security.SysRoleVO;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.mapper.SysRoleMapper;
import com.lihua.system.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserVO sysUserVO = sysUserMapper.loginSelect(username);
        if (sysUserVO == null) {
            throw new UsernameNotFoundException("用户名未找到");
        }
        return new LoginUser(sysUserVO);
    }
}

