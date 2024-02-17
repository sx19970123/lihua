package com.lihua.utils.security;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.SysBaseEnum;
import com.lihua.model.security.LoginUser;
import com.lihua.model.security.RouterVO;
import com.lihua.model.security.SysRoleVO;
import com.lihua.model.security.SysStarViewVO;
import com.lihua.utils.spring.SpringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class LoginUserReset {

    /**
     * 重新设置用户主题
     * @param theme
     */
    public static void resetTheme(String theme) {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        resetTheme(theme,loginUser);
    }

    public static void resetTheme(String theme, LoginUser loginUser) {
        loginUser.getSysUserVO().setTheme(theme);
        redisCache(loginUser);
    }

    /**
     * 重新设置权限集合
     * @param authorities
     */
    public static void resetAuthorities(List<String> authorities) {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        resetAuthorities(authorities,loginUser);
    }

    public static void resetAuthorities(List<String> authorities, LoginUser loginUser) {
        loginUser.setAuthorities(authorities);
        redisCache(loginUser);
    }

    /**
     * 重新设置用户菜单信息
     * @param routerList
     */
    public static void resetRouterList(List<RouterVO> routerList) {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        resetRouterList(routerList,loginUser);
    }

    public static void resetRouterList(List<RouterVO> routerList, LoginUser loginUser ) {
        loginUser.setRouterList(routerList);
        redisCache(loginUser);
    }

    /**
     * 重新设置用户角色信息
     * @param sysRoleList
     */
    public static void resetRoleList(List<SysRoleVO> sysRoleList) {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        resetRoleList(sysRoleList,loginUser);
    }

    public static void resetRoleList(List<SysRoleVO> sysRoleList,LoginUser loginUser) {
        loginUser.setSysRoleList(sysRoleList);
        redisCache(loginUser);
    }

    /**
     * 重新设置用户view tags 收藏信息
     * @param starViewVOList
     */
    public static void resetStarViewList(List<SysStarViewVO> starViewVOList) {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        resetStarViewList(starViewVOList,loginUser);
    }

    public static void resetStarViewList(List<SysStarViewVO> starViewVOList, LoginUser loginUser) {
        loginUser.setStarViewVOList(starViewVOList);
        redisCache(loginUser);
    }

    private static void redisCache(LoginUser loginUser) {
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);
        redisCache.setCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + loginUser.getSysUserVO().getId(), loginUser, lihuaConfig.getExpireTime(), TimeUnit.MINUTES);
    }
}
