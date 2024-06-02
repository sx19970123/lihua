package com.lihua.system.service;

import com.lihua.model.security.LoginUser;
import com.lihua.model.security.CurrentUser;

public interface SysAuthenticationService {

    /**
     * 用户登陆
     * @param currentUser
     * @return
     */
    String login(CurrentUser currentUser);

    /**
     * 缓存用户登录信息
     * @param loginUser
     */
    void cacheUserLoginDetails(LoginUser loginUser);
}
