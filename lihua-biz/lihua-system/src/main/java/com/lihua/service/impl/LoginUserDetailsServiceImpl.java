package com.lihua.service.impl;

import config.LihuaConfig;
import com.lihua.mapper.SysUserMapper;
import com.lihua.model.CurrentUser;
import com.lihua.model.LoginUser;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utils.date.DateUtils;

@Service
public class LoginUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private LihuaConfig lihuaConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CurrentUser user = sysUserMapper.loginSelect(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名未找到");
        }
        // 创建 LoginUser 包含登录的用户信息 和 过期时间
        return new LoginUser(user, DateUtils.now().plusMinutes(lihuaConfig.getTokenExpireTime()));
    }
}

