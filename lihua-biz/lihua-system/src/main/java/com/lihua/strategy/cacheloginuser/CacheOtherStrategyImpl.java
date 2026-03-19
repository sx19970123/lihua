package com.lihua.strategy.cacheloginuser;

import com.lihua.ip.utils.IpUtils;
import com.lihua.security.model.LoginUser;
import org.springframework.stereotype.Component;

/**
 * 缓存其他数据实现类
 */
@Component
public class CacheOtherStrategyImpl implements CacheLoginUserStrategy {
    @Override
    public void cacheLoginUser(LoginUser loginUser, boolean isAdmin) {
        // 设置用户ip
        loginUser.setIpAddress(IpUtils.getIpAddress());
    }
}
