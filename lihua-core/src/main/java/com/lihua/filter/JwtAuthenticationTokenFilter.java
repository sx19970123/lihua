package com.lihua.filter;

import com.lihua.model.security.LoginUser;
import com.lihua.utils.security.LoginUserManager;
import com.lihua.utils.web.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nullable;
import java.io.IOException;


/**
 * 请求 token 过滤器
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,@Nullable  HttpServletResponse response,@Nullable  FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = WebUtils.getToken(request);

        if (StringUtils.hasText(token)) {
            LoginUser loginUser = LoginUserManager.getLoginUser(token);
            if (loginUser != null) {
                // 将用户信息存入上下文
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(
                                loginUser,
                                null,
                                loginUser.getPermissionList().stream().map(SimpleGrantedAuthority::new).toList()));
                // 判断过期时间进行重新缓存
                LoginUserManager.verifyLoginUserCache();
            } else {
                throw new ServletException("认证信息过期失效");
            }
        }

        if (filterChain != null) {
            filterChain.doFilter(request,response);
        }
    }

}
