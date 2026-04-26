package com.lihua.strategy.cacheloginuser;

import com.lihua.security.model.LoginUserSession;

/**
 * 缓存登录用户信息策略接口
 */
public interface CacheLoginUserStrategy {

    /**
     * 查询登录用户信息
     * @param loginUserSession 用户登录信息
     */
    void cacheLoginUser(LoginUserSession loginUserSession, boolean isAdmin);
}
