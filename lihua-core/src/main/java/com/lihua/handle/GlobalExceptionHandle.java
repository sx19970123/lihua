package com.lihua.handle;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.security.InvalidTokenException;
import com.lihua.model.web.ControllerResult;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Configuration
public class GlobalExceptionHandle extends ControllerResult {

    /**
     * 捕获全局 RuntimeException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(Exception e) {
        e.printStackTrace();
        return error(ResultCodeEnum.ERROR, e.getMessage());
    }

    /**
     * 用户登陆失败处理
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(Exception e) {
        e.printStackTrace();
        return error(ResultCodeEnum.getByDefaultExceptionMessage(e.getMessage()));
    }

    /**
     * 访问权限不足处理
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(Exception e) {
        e.printStackTrace();
        return error(ResultCodeEnum.ACCESS_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleInvalidTokenException(Exception e) {
        e.printStackTrace();
        return error(ResultCodeEnum.LOGIN_ERROR);
    }
}
