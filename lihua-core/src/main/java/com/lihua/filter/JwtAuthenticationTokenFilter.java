package com.lihua.filter;

import com.lihua.constant.Constant;
import com.lihua.model.security.LoginUser;
import com.lihua.utils.security.LoginUserMgmt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(Constant.TOKEN.getCode());
        if (StringUtils.hasText(token)) {
            LoginUser loginUser = LoginUserMgmt.getLoginUser(token);
            if (loginUser != null) {
                // 将用户信息存入上下文
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities()));
                // 判断过期时间进行重新缓存
                LoginUserMgmt.verifyLoginUserCache();
            } else {
                throw new ServletException("认证信息过期失效");
            }
        }

        filterChain.doFilter(request,response);
    }



}
