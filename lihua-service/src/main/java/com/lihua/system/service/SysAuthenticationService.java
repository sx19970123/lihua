package com.lihua.system.service;

import com.lihua.model.security.LoginUser;
import com.lihua.model.security.CurrentUser;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface SysAuthenticationService {

    /**
     * 用户登陆
     * @param currentUser
     * @return
     */
    Map<String, String> login(CurrentUser currentUser);

    /**
     * 缓存用户登录信息
     * @param loginUser
     */
    void cacheUserLoginDetails(LoginUser loginUser);

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
}
