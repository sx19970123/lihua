package com.lihua.handle;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.ControllerResult;
import com.lihua.utils.web.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 用户登陆失败处理
 *
 * 因配置了全局异常处理
 * 请在 GlobalExceptionHandle.handleAuthenticationException 进行配置
 */
@Component
public class AuthenticationEntryPointImpl extends ControllerResult implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        WebUtils.renderJson(response, error(ResultCodeEnum.LOGIN_ERROR));
    }
}
