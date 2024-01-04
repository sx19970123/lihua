package com.lihua.system.service;

import com.lihua.model.SysUser;

public interface SysAuthenticationService {

    /**
     * 用户登陆
     * @param sysUser
     * @return
     */
    String login(SysUser sysUser);

    /**
     * 用户登出
     */
    void logout();
}
