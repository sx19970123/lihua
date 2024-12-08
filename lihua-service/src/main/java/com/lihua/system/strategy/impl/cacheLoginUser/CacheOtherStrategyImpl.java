package com.lihua.system.strategy.impl.cacheLoginUser;

import com.lihua.model.security.LoginUser;
import com.lihua.system.strategy.CacheLoginUserStrategy;
import com.lihua.utils.web.WebUtils;
import org.springframework.stereotype.Component;

/**
 * 缓存其他数据实现类
 */
@Component
public class CacheOtherStrategyImpl implements CacheLoginUserStrategy {
    @Override
    public void cacheLoginUser(LoginUser loginUser, boolean isAdmin) {
        // 设置用户ip
        loginUser.setIpAddress(WebUtils.getIpAddress());
    }
}
