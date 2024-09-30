package com.lihua.system.service;

import com.lihua.model.security.LoginUser;
import com.lihua.model.security.CurrentUser;

public interface SysAuthenticationService {

    /**
     * 用户登陆
     * @param currentUser
     * @return
     */
    LoginUser login(CurrentUser currentUser);

    /**
     * 登录后必要信息校验，对应于前端 components/login-setting 下的组件进行处理
     * @param loginUser
     * @param password
     * @return
     */
    String checkLoginSetting(LoginUser loginUser, String password);

    /**
     * 缓存用户信息
     * @param loginUser
     * @return redis缓存key
     */
    String cacheLoginUserInfo(LoginUser loginUser);

    /**
     * 缓存用户信息并返回token
     * @param loginUser 登录用户信息
     * @return token
     */
    String cacheAndCreateToken(LoginUser loginUser);

    /**
     * 检查用户名是否重复
     * @param username
     * @return
     */
    boolean checkUserName(String username);

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    String register(String username, String password);

    /**
     * 检查是否配置了同账号最大同时登录数，超出数量后首先登录的用户会被踢下线
     * @param token
     */
    void checkSameAccount(String token);
}
