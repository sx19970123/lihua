package com.lihua.system.service;

import com.lihua.model.security.LoginUser;
import com.lihua.model.security.CurrentUser;

public interface SysAuthenticationService {

    /**
     * 用户登陆
     * @param sysUserVO
     * @return
     */
    String login(CurrentUser sysUserVO);

    /**
     * 缓存用户登录信息
     * @param loginUser
     */
    void cacheUserLoginDetails(LoginUser loginUser);
}
