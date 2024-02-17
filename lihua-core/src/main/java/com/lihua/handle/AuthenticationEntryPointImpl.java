package com.lihua.handle;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.ControllerResult;
import com.lihua.utils.web.WebUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 用户未认证处理器
 *
 * 因配置了全局异常处理
 * 请在 GlobalExceptionHandle.handleAuthenticationException 进行配置
 */
@Component
public class AuthenticationEntryPointImpl extends ControllerResult implements AuthenticationEntryPoint {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        try {
            requestMappingHandlerMapping.getHandler(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        WebUtils.renderJson(response, error(ResultCodeEnum.AUTHENTICATION_EXPIRED));
    }
}
