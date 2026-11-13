package com.lihua.security.handler;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.Responses;
import com.lihua.web.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 权限异常处理器
 * 403
 */
@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        WebUtils.renderResponse(Responses.error(ResultCodeEnum.ACCESS_ERROR));
    }
}
