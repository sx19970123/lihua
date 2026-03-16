package com.lihua.security.filter;

import com.lihua.security.manager.LoginUserManager;
import com.lihua.security.model.LoginUser;
import com.lihua.security.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 请求 token 过滤器
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = TokenUtils.getToken(request);

        if (StringUtils.hasText(token)) {
            LoginUser loginUser = LoginUserManager.getLoginUser(token);
            if (loginUser != null) {
                PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(loginUser, null, loginUser.getPermissionList().stream().map(SimpleGrantedAuthority::new).toList());
                // 将用户信息存入上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // 判断过期时间进行重新缓存
                LoginUserManager.verifyLoginUserCache();
            }
        }

        filterChain.doFilter(request,response);
    }

}
