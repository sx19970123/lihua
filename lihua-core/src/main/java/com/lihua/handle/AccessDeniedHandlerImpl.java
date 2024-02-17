package com.lihua.handle;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.ControllerResult;
import com.lihua.utils.web.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 用户访问权限不足处理
 *
 * 因配置了全局异常处理
 * 请在 GlobalExceptionHandle.handleAccessDeniedException 进行配置
 */
@Component
public class AccessDeniedHandlerImpl extends ControllerResult implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        System.out.println("权限不足处理器");
        WebUtils.renderJson(response,error(ResultCodeEnum.ACCESS_ERROR));
    }
}
