package com.lihua.strategy.postlogincheck;

import com.lihua.security.model.LoginUserSession;

/**
 * 检查用户是否需要登录后进行配置
 */
public interface PostLoginCheckStrategy {
    /**
     * 检查配置
     * @param loginUserSession 登录用户信息
     * @return null 或 对应配置前端组件名称
     */
    String check(LoginUserSession loginUserSession);
}
