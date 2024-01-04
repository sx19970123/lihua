package com.lihua.filter;

import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.model.LoginUser;
import com.lihua.utils.securityUtils.JwtUtils;
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
        String token = request.getHeader("token");
        if (StringUtils.hasText(token)) {
            // 解析token 后从redis 获取用户信息
            LoginUser loginUser = getUserInfoByToken(token);
            if (loginUser == null) {
                throw new RuntimeException("当前用户已过期");
            }
            // 用户信息存入 SecurityContextHolder
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities()));
        }

        filterChain.doFilter(request,response);
    }


    /**
     * 通过 token 获取用户信息
     * @param token
     * @return
     */
    private LoginUser getUserInfoByToken(String token) {
        try {
            JwtUtils.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("非法token");
        }

        String decode = JwtUtils.decode(token);
        if (!StringUtils.hasText(decode)) {
            throw new RuntimeException("无效token");
        }

        return redisCache.getCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + decode);
    }
}
