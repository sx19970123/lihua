package com.lihua.filter;

import com.lihua.cache.RedisCache;
import com.lihua.constant.Constant;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.security.InvalidTokenException;
import com.lihua.exception.security.LoginExpiredException;
import com.lihua.model.security.LoginUser;
import com.lihua.utils.security.JwtUtils;
import jakarta.annotation.Resource;
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
            } else {
                throw new LoginExpiredException("认证信息过期失效");
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
        try {
            JwtUtils.verify(token);
        } catch (Exception e) {
            throw new InvalidTokenException("无效的token");
        }

        String decode = JwtUtils.decode(token);
        log.info("\n当前用户token为：{}\n解密后主键id为：{}", token, decode);

        try {
            return redisCache.getCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + decode);
        } catch (Exception e) {
            log.error("从redis获取LoginUser发生异常，请检查redis状态");
            e.printStackTrace();
        }

        return null;
    }
}
