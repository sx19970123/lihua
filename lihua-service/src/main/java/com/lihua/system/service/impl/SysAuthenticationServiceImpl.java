package com.lihua.system.service.impl;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.SysBaseEnum;
import com.lihua.model.LoginUser;
import com.lihua.model.SysUser;
import com.lihua.system.service.SysAuthenticationService;
import com.lihua.utils.security.JwtUtils;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SysAuthenticationServiceImpl implements SysAuthenticationService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private LihuaConfig lihuaConfig;

    @Override
    public String login(SysUser sysUser) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword()));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        loginUser.getSysUser().setPassword(null);
        // 将用户信息存放到redis
        redisCache.setCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + loginUser.getSysUser().getId(), loginUser, lihuaConfig.getExpireTime(), TimeUnit.MINUTES);
        // 根据username 生成jwt 返回
        return JwtUtils.create(loginUser.getSysUser().getId());
    }

    @Override
    public void logout() {
        redisCache.delete(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + SecurityUtils.getUserId());
    }
}
