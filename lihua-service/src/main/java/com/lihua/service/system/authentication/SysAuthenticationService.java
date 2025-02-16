package com.lihua.service.system.authentication;

import com.lihua.model.security.LoginUser;
import com.lihua.model.security.CurrentUser;

import java.util.List;

public interface SysAuthenticationService {

    /**
     * 用户登陆
     */
    LoginUser login(CurrentUser currentUser);

    /**
     * 登录后必要信息校验，对应于前端 components/login-setting 下的组件进行处理
     */
    List<String> checkLoginSetting(LoginUser loginUser);

    /**
     * 缓存用户信息
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
     */
    boolean checkUserName(String username);

    /**
     * 用户注册
     */
    String register(String username, String password);

    /**
     * 检查是否配置了同账号最大同时登录数，超出数量后首先登录的用户会被踢下线
     */
    void checkSameAccount(String token);
}
