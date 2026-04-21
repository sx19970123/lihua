package com.lihua.service;

import com.lihua.model.dto.SysLoginUserDTO;
import com.lihua.security.model.LoginUserSession;

import java.util.List;

public interface SysAuthenticationService {

    /**
     * 用户登录
     */
    LoginUserSession login(SysLoginUserDTO loginUserDTO);

    /**
     * 缓存用户信息
     * @param loginUserSession 登录用户缓存数据
     * @return redis缓存key
     */
    String cacheLoginUserInfo(LoginUserSession loginUserSession);

    /**
     * 缓存用户信息并返回token
     * @param loginUserSession 登录用户信息
     * @return token
     */
    String cacheAndCreateToken(LoginUserSession loginUserSession);

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

    /**
     * 获取一次性令牌，用完后需要在各自业务中进行删除
     * 自动删除时间1分钟
     */
    String getOnceToken();
}
