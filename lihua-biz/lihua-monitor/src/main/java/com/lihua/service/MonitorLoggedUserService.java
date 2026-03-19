package com.lihua.service;
import com.lihua.model.LoggedUser;

import java.util.List;

public interface MonitorLoggedUserService {

    /**
     * 查询登录用户列表
     */
    List<LoggedUser> queryList(String username, String nickName, String clientType);

    /**
     * 用户强退
     */
    void forceLogout(List<String> cacheKey);
}
