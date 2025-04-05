package com.lihua.handle;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.basecontroller.StrResponseController;
import com.lihua.utils.web.WebUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 用户未认证处理器
 * <p>
 * 因配置了全局异常处理
 * 请在 GlobalExceptionHandle.handleAuthenticationException 进行配置
 */
@Component
@Slf4j
public class AuthenticationEntryPointImpl extends StrResponseController implements AuthenticationEntryPoint {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        try {
            HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);
            // springMVC 请求处理器返回 null 认定为 404
            if (handler == null) {
                WebUtils.renderJson(response, error(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR));
                return;
            }
        } catch (Exception e) {
            // springMVC 处理异常也认定为 404
            WebUtils.renderJson(response, error(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR));
            log.error(e.getMessage(),e);
            return;
        }
        log.error(authException.getMessage(), authException);
        WebUtils.renderJson(response, error(ResultCodeEnum.AUTHENTICATION_EXPIRED));
    }
}
