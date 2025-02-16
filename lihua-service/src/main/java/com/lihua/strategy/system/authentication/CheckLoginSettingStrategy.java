package com.lihua.strategy.system.authentication;

import com.lihua.model.security.LoginUser;

/**
 * 检查用户是否需要登陆后进行配置
 */
public interface CheckLoginSettingStrategy {
    /**
     * 检查配置
     * @param loginUser 登录用户信息
     * @return null 或 对应配置前端组件名称
     */
    String checkSetting(LoginUser loginUser);
}
