package com.lihua.strategy.cacheloginuser;

import com.lihua.mapper.SysUserMapper;
import com.lihua.security.model.CurrentUser;
import com.lihua.security.model.LoginUserSession;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class CacheUserStrategyImpl implements CacheLoginUserStrategy {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void cacheLoginUser(LoginUserSession loginUserSession, boolean isAdmin) {
        CurrentUser user = loginUserSession.getUser();
        // 重新查询user
        user = sysUserMapper.loginSelect(user.getUsername());
        // 重新设置user
        loginUserSession.setUser(user);
    }
}
