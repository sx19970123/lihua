package com.lihua.security.handler;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.response.StrResponse;
import com.lihua.common.utils.web.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 认证异常处理器
 * 401
 */
@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        // token过期登录失效，返回默认提示信息
        if (authException instanceof InsufficientAuthenticationException) {
            WebUtils.renderJson(StrResponse.error(ResultCodeEnum.AUTHENTICATION_EXPIRED));
        } else {
            // 其他情况，返回security提示信息
            WebUtils.renderJson(StrResponse.error(ResultCodeEnum.AUTHENTICATION_EXPIRED, authException.getMessage()));
        }
    }
}
