package com.lihua.system.service;

import com.lihua.model.security.SysUserVO;

public interface SysAuthenticationService {

    /**
     * 用户登陆
     * @param sysUserVO
     * @return
     */
    String login(SysUserVO sysUserVO);
}
