package com.lihua.utils.security;

import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.model.security.*;
import com.lihua.utils.spring.SpringUtils;
import com.lihua.utils.tree.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 获取当前登录用户工具类
 */
@Slf4j
public class LoginUserContext implements Serializable {

    private static final RedisCache redisCache;

    static {
        redisCache = SpringUtils.getBean(RedisCache.class);
    }

    /**
     * 获取当前登录用户 id
     */
    public static String getUserId() {
        return getUser().getId();
    }

    /**
     * 获取当前登录用户 username
     */
    public static String getUsername() {
        return getUser().getUsername();
    }

    /**
     * 获取当前登陆用户角色
     */
    public static List<CurrentRole> getRoleList() {
        return getLoginUser().getRoleList();
    }

    /**
     * 获取当前登陆用户角色编码
     */
    public static List<String> getRoleCodeList() {
        return getRoleList().stream().map(CurrentRole::getCode).toList();
    }

    /**
     * 判断当前登陆用户是否为超级管理员
     */
    public static boolean isAdmin() {
        return getRoleCodeList().contains("ROLE_admin");
    }

    /**
     * 获取当前用户所有部门
     */
    public static List<CurrentDept> getDeptList() {
        return getLoginUser().getDeptList();
    }

    /**
     * 获取当前用户所有部门（树型结构）
     */
    public static List<CurrentDept> getDeptTree() {
        List<CurrentDept> deptList = getLoginUser().getDeptList();
        return TreeUtils.buildTree(deptList);
    }

    /**
     * 获取当前用户所有部门编码
     */
    public static List<String> getDeptCodeList() {
        return getDeptList().stream().map(CurrentDept::getCode).toList();
    }

    /**
     * 获取当前用户默认部门
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
     */
    public static String getDefaultDeptCode() {
        return getDefaultDept() != null ? getDefaultDept().getCode() : null;
    }

    /**
     * 获取当前用户所有岗位
     */
    public static List<CurrentPost> getPostList() {
        return getLoginUser().getPostList();
    }

    /**
     * 获取当前用户所有岗位编码
     */
    public static List<String> getPostCodeList() {
        return getPostList().stream().map(CurrentPost::getCode).toList();
    }

    /**
     * 获取当前用户默认部门下所有岗位
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
     */
    public static List<String> getDefaultDeptPostCodeList() {
        return getDefaultDeptPostList().stream().map(CurrentPost::getDeptCode).toList();
    }

    /**
     * 获取当前登录用户信息
     */
    public static CurrentUser getUser() {
        try {
            return getLoginUser().getUser();
        } catch (Exception e) {
            log.error("获取当前登录用户失败，返回空用户");
            return new CurrentUser();
        }
    }

    /**
     * 获取当前登陆用户 LoginUser 信息
     */
     public static LoginUser getLoginUser() {
         return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 判断当前 userId 用户是否为登陆状态
     */
    public static boolean isLogin(String userId) {
        if (!StringUtils.hasText(userId)) {
            return false;
        }

        // 根据redisKey规则，通过前缀+userId+: 获取用户登录key
        Set<String> keys = redisCache.keys(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + userId + ":");

        return !keys.isEmpty();
    }

    /**
     * 获取当前登录用户 Authentication
     */
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


}
