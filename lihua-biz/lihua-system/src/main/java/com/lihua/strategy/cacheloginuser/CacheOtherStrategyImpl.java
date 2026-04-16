package com.lihua.strategy.cacheloginuser;

import com.lihua.ip.utils.IpUtils;
import com.lihua.security.model.LoginUserSession;
import org.springframework.stereotype.Component;

/**
 * 缓存其他数据实现类
 */
@Component
public class CacheOtherStrategyImpl implements CacheLoginUserStrategy {
    @Override
    public void cacheLoginUser(LoginUserSession loginUserSession, boolean isAdmin) {
        // 设置用户ip
        loginUserSession.setIpAddress(IpUtils.getIpAddress());
    }
}
