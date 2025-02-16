package com.lihua.service.monitor.logged;

import com.lihua.model.monitor.LoggedUser;

import java.util.List;

public interface MonitorLoggedUserService {

    /**
     * 查询登录用户列表
     */
    List<LoggedUser> queryList(String username, String nickName);

    /**
     * 用户强退
     */
    void forceLogout(List<String> cacheKey);
}
