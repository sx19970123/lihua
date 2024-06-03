package com.lihua.model.security;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 组合完成后交给前端store
 */
@Data
public class AuthInfo implements Serializable {
    // 权限信息（菜单权限编码，角色编码集合）
    List<String> permissions;
    // 所有路由信息
    List<CurrentRouter> routers;
    // 所有收藏固定的菜单信息
    List<CurrentViewTab> viewTabs;
    // 所有角色信息
    List<CurrentRole> roles;
    // 登陆用户信息
    CurrentUser userInfo;
    // 部门信息
    List<CurrentDept> depts;
    // 默认部门
    CurrentDept defaultDept;
    // 岗位信息
    List<CurrentPost> posts;
}
