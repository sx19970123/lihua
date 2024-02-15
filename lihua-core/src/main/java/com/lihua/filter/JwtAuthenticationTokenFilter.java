package com.lihua.filter;

import com.lihua.cache.RedisCache;
import com.lihua.constant.Constant;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.security.InvalidTokenException;
import com.lihua.model.security.LoginUser;
import com.lihua.utils.security.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(Constant.TOKEN.getCode());
        if (StringUtils.hasText(token)) {
            LoginUser loginUser = getUserInfoByToken(token);
            if (loginUser != null) {
                // 将用户信息存入上下文
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities()));
            }
        }
        filterChain.doFilter(request,response);
    }


    /**
     * 通过 token 获取用户信息
     * @param token
     * @return
     */
    private LoginUser getUserInfoByToken(String token) {
        JwtUtils.verify(token);
        String decode = JwtUtils.decode(token);
        return redisCache.getCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + decode);
    }
}
