package com.lihua.utils.security;

import com.lihua.model.security.LoginUser;
import com.lihua.model.security.SysUserVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取当前登录用户工具类
 */
public class LoginUserContext {

    /**
     * 获取当前登录用户 id
     * @return
     */
    public static String getUserId() {
        return getSysUser().getId();
    }

    /**
     * 获取当前登录用户 username
     * @return
     */
    public static String getUsername() {
        return getSysUser().getUsername();
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static SysUserVO getSysUser() {
        return getLoginUser().getSysUserVO();
    }

    /**
     * 获取当前登陆用户 LoginUser 信息
     * @return
     */
     public static LoginUser getLoginUser() {
         return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取当前登录用户 Authentication
     * @return
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
