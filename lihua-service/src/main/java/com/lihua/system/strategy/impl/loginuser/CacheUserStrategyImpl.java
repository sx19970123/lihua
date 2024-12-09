package com.lihua.system.strategy.impl.loginuser;

import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.strategy.CacheLoginUserStrategy;
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
        // 缓存中隐藏密码
        user.setPassword(null);
        // 重新设置user
        loginUser.setUser(user);
    }
}
