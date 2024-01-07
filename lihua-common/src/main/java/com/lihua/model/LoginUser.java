package com.lihua.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 实现了 springsecurity 的 UserDetails
 * 用于 springsecurity内部认证处理
 */
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LoginUser implements UserDetails {

    /**
     * 系统用户信息
     */
    private SysUser sysUser;

    /**
     * 权限集合
     */
    private List<String> authorities;

    /**
     *  登录时将 authorities 转换为 SimpleGrantedAuthority 类型集合，
     *  请求接口获取权限信息时直接将属性返回即可
     */
    private List<? extends GrantedAuthority> permissionList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissionList == null) {
            permissionList = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return permissionList;
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public LoginUser(SysUser sysUser, List<String> authorities) {
        this.sysUser = sysUser;
        this.authorities = authorities;
    }
}
