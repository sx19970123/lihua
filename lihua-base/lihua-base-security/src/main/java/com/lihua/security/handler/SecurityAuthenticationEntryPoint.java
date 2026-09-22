package com.lihua.security.handler;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.response.StrResponse;
import com.lihua.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 认证异常处理器
 * 401
 */
@Slf4j
@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        log.error(authException.getMessage(), authException);
        // token过期登录失效，返回默认提示信息
        if (authException instanceof InsufficientAuthenticationException) {
            WebUtils.renderJson(StrResponse.error(ResultCodeEnum.AUTHENTICATION_EXPIRED));
        } else {
            // 其他认证异常同样返回默认文案：getMessage 可能携带内部细节，不透传客户端，详情已在上方日志留痕
            WebUtils.renderJson(StrResponse.error(ResultCodeEnum.AUTHENTICATION_EXPIRED));
        }
    }
}
