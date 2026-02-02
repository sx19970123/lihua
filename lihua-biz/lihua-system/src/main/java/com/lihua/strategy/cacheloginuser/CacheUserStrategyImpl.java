package com.lihua.strategy.cacheloginuser;

import com.lihua.mapper.SysUserMapper;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class CacheUserStrategyImpl implements CacheLoginUserStrategy {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void cacheLoginUser(LoginUser loginUser, boolean isAdmin) {
        CurrentUser user = loginUser.getUser();
        // 重新查询user
        user = sysUserMapper.loginSelect(user.getUsername());
        // 重新设置user
        loginUser.setUser(user);
    }
}
