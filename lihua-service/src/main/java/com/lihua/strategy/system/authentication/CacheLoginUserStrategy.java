package com.lihua.strategy.system.authentication;

import com.lihua.model.security.LoginUser;

/**
 * 缓存登录用户信息策略接口
 */
public interface CacheLoginUserStrategy {

    /**
     * 查询登录用户信息
     * @param loginUser 用户登录信息
     */
    void cacheLoginUser(LoginUser loginUser, boolean isAdmin);
}
