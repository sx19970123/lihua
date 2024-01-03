package com.lihua.system.service.impl;

import com.lihua.system.entity.LoginUser;
import com.lihua.system.entity.SysUser;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public String login(SysUser sysUser) {
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (authenticate != null) {
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            return loginUser.getUsername();
        }
        return null;
    }
}
