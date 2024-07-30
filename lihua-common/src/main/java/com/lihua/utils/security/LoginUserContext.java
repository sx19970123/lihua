package com.lihua.utils.security;

import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.model.security.*;
import com.lihua.utils.spring.SpringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取当前登录用户工具类
 */
public class LoginUserContext implements Serializable {

    private static RedisCache redisCache;

    /**
     * 获取当前登录用户 id
     * @return
     */
    public static String getUserId() {
        return getUser().getId();
    }

    /**
     * 获取当前登录用户 username
     * @return
     */
    public static String getUsername() {
        return getUser().getUsername();
    }

    /**
     * 获取当前登陆用户角色
     * @return
     */
    public static List<CurrentRole> getRoleList() {
        return getLoginUser().getRoleList();
    }

    /**
     * 获取当前登陆用户角色编码
     * @return
     */
    public static List<String> getRoleCodeList() {
        return getRoleList().stream().map(CurrentRole::getCode).toList();
    }

    /**
     * 判断当前登陆用户是否为超级管理员
     * @return
     */
    public static boolean isAdmin() {
        return getRoleCodeList().contains("ROLE_admin");
    }

    /**
     * 获取当前用户所有部门
     * @return
     */
    public static List<CurrentDept> getDeptList() {
        return getLoginUser().getDeptList();
    }

    /**
     * 获取当前用户所有部门（树型结构）
     * @return
     */
    public static List<CurrentDept> getDeptTree() {
        return getLoginUser().getDeptTree();
    }

    /**
     * 获取当前用户所有部门编码
     * @return
     */
    public static List<String> getDeptCodeList() {
        return getDeptList().stream().map(CurrentDept::getCode).toList();
    }

    /**
     * 获取当前用户默认部门
     * @return
     */
    public static CurrentDept getDefaultDept() {
        List<CurrentDept> defaultDeptList = getDeptList().stream().filter(dept -> "0".equals(dept.getDefaultDept())).toList();
        if (!defaultDeptList.isEmpty()) {
            return defaultDeptList.get(0);
        }
        return null;
    }

    /**
     * 获取当前用户默认编码
     * @return
     */
    public static String getDefaultDeptCode() {
        return getDefaultDept() != null ? getDefaultDept().getCode() : null;
    }

    /**
     * 获取当前用户所有岗位
     * @return
     */
    public static List<CurrentPost> getPostList() {
        return getLoginUser().getPostList();
    }

    /**
     * 获取当前用户所有岗位编码
     * @return
     */
    public static List<String> getPostCodeList() {
        return getPostList().stream().map(CurrentPost::getCode).toList();
    }

    /**
     * 获取当前用户默认部门下所有岗位
     * @return
     */
    public static List<CurrentPost> getDefaultDeptPostList() {
        CurrentDept defaultDept = getDefaultDept();
        List<CurrentPost> postList = getPostList();
        if (defaultDept != null) {
            return postList.stream().filter(post -> post.getDeptId().equals(defaultDept.getId())).toList();
        }
        return new ArrayList<>();
    }

    /**
     * 获取当前用户默认单位下所有岗位编码
     * @return
     */
    public static List<String> getDefaultDeptPostCodeList() {
        return getDefaultDeptPostList().stream().map(CurrentPost::getDeptCode).toList();
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static CurrentUser getUser() {
        return getLoginUser().getUser();
    }

    /**
     * 获取当前登陆用户 LoginUser 信息
     * @return
     */
     public static LoginUser getLoginUser() {
         return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 判断当前 userId 用户是否为登陆状态
     * @param userId
     * @return
     */
    public static boolean isLogin(String userId) {
        if (!StringUtils.hasText(userId)) {
            return false;
        }
        initCache();
        return redisCache.hasKey(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + userId);
    }

    /**
     * 获取当前登录用户 Authentication
     * @return
     */
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 从spring容器中获取 redisCache
     */
    private static void initCache() {
        if (redisCache == null) {
            redisCache = SpringUtils.getBean(RedisCache.class);
        }
    }
}
