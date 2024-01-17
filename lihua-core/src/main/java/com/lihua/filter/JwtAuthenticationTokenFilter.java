package com.lihua.filter;

import com.lihua.cache.RedisCache;
import com.lihua.constant.Constant;
import com.lihua.enums.SysBaseEnum;
import com.lihua.model.LoginUser;
import com.lihua.utils.security.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.lihua.enums.ResultCodeEnum.AUTHENTICATION_EXPIRED;
import static com.lihua.enums.ResultCodeEnum.TOKEN_ERROR;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(Constant.TOKEN.getCode());
        if (StringUtils.hasText(token)) {
            // 解析token 后从redis 获取用户信息
            LoginUser loginUser = getUserInfoByToken(request,token);
            if (loginUser == null) {
                // 用户过期，将信息存入 request 由认证中心进行处理
                request.setAttribute(Constant.ERROR_MSG.getCode(),AUTHENTICATION_EXPIRED);
            }
            // 用户信息存入 SecurityContextHolder
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities()));
        }

        filterChain.doFilter(request,response);
    }


    /**
     * 通过 token 获取用户信息
     * @param token
     * @return
     */
    private LoginUser getUserInfoByToken(HttpServletRequest request, String token) {
        try {
            JwtUtils.verify(token);
        } catch (Exception e) {
            request.setAttribute(Constant.ERROR_MSG.getCode(),TOKEN_ERROR);
            e.printStackTrace();
            throw new RuntimeException();
        }

        String decode = JwtUtils.decode(token);
        if (!StringUtils.hasText(decode)) {
            request.setAttribute(Constant.ERROR_MSG.getCode(),TOKEN_ERROR);
            throw new RuntimeException();
        }

        return redisCache.getCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + decode);
    }
}
