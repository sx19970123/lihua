package com.lihua.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lihua.utils.date.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUser implements UserDetails {

    /**
     * 当前登陆用户信息
     */
    private CurrentUser user;

    /**
     * 因 authorities 无法在 jackson 下序列化，所有权限字符存入 permissionList 中
     */
    private List<? extends GrantedAuthority> authorities = new ArrayList<>();

    /**
     * 权限集合，ROLE_开头为拥有的角色编码，其余为页面权限
     */
    private List<String> permissionList;

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
     * 用户岗位信息
     */
    private List<CurrentPost> postList = new ArrayList<>();

    /**
     * token 缓存过期时间
     */
    private LocalDateTime expirationTime;


    /**
     * redis 中对应的缓存key
     */
    private String cacheKey;

    /**
     * 登录 ip
     */
    private String ipAddress;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
     */
    @Override
    public boolean isAccountNonExpired() {
        return expirationTime.isAfter(DateUtils.now());
    }

    /**
     * 用户是否锁定， true 表示未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证（密码）是否过期， true 表示未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否失效， true 表示已启用
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
