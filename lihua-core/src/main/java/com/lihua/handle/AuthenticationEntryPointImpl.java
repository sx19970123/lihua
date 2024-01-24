package com.lihua.handle;

import com.lihua.constant.Constant;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.ControllerResult;
import com.lihua.utils.web.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 用户未认证处理器
 *
 * 因配置了全局异常处理
 * 请在 GlobalExceptionHandle.handleAuthenticationException 进行配置
 */
@Component
public class AuthenticationEntryPointImpl extends ControllerResult implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        Object errorMeg = request.getAttribute(Constant.ERROR_MSG.getCode());
        if (errorMeg != null) {
            WebUtils.renderJson(response, error((ResultCodeEnum) errorMeg));
            return;
        }
        WebUtils.renderJson(response, error(ResultCodeEnum.LOGIN_ERROR));
    }
}
