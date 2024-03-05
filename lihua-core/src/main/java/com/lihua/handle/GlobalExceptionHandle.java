package com.lihua.handle;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.ServiceException;
import com.lihua.exception.security.InvalidTokenException;
import com.lihua.exception.security.ResourceNotFoundException;
import com.lihua.model.web.BaseController;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Configuration
public class GlobalExceptionHandle extends BaseController {

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
     * 捕获全局 ServiceException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public String handleServiceException(Exception e) {
        e.printStackTrace();
        return error(ResultCodeEnum.ERROR, e.getMessage());
    }

    /**
     * 处理用户登陆发生异常
     * @param e
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public String handleInvalidTokenException(Exception e) {
        e.printStackTrace();
        return error(ResultCodeEnum.LOGIN_ERROR);
    }

    /**
     * 处理请求拦截器在用户认证时发生的异常
     * @param e
     * @return
     */
    @ExceptionHandler(InvalidTokenException.class)
    public String handleInsufficientAuthenticationException(Exception e) {
        e.printStackTrace();
        return error(ResultCodeEnum.AUTHENTICATION_EXPIRED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(Exception e) {
        e.printStackTrace();
        return error(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR);
    }
}
