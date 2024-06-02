package com.lihua.model.security;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 实现了 springsecurity 的 UserDetails
 * 用于 springsecurity内部认证处理
 */
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class LoginUser implements UserDetails {

    /**
     * 当前登陆用户信息
     */
    private CurrentUser user;

    /**
     * 权限集合
     */
    private List<String> authorities = new ArrayList<>();

    /**
     *  登录时将 authorities 转换为 SimpleGrantedAuthority 类型集合，
     *  请求接口获取权限信息时直接将属性返回即可
     */
    private List<? extends GrantedAuthority> permissionList;

    /**
     * 用户菜单信息
     */
    private List<CurrentRouter> routerList = new ArrayList<>();

    /**
     * 用户角色信息
     */
    private List<CurrentRole> roleList = new ArrayList<>();

    /**
     * 用户收藏/固定菜单信息
     */
    private List<CurrentViewTab> viewTabList = new ArrayList<>();

    /**
     * 用户部门信息
     */
    private List<CurrentDept> deptList = new ArrayList<>();

    /**
     * 用户部门信息（tree结构）
     */
    private List<CurrentDept> deptTree = new ArrayList<>();

    /**
     * 用户岗位信息
     */
    private List<CurrentPost> postList = new ArrayList<>();

    /**
     * token 缓存过期时间
     */
    private LocalDateTime expirationTime;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        permissionList = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return permissionList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 用户是否过期， true 表示未过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return expirationTime.isAfter(LocalDateTime.now());
    }

    /**
     * 用户是否锁定， true 表示未锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证（密码）是否过期， true 表示未过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否失效， true 表示已启用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return "0".equals(user.getStatus());
    }

    public LoginUser(CurrentUser currentUser, LocalDateTime expirationTime) {
        this.user = currentUser;
        this.expirationTime = expirationTime;
    }
}
