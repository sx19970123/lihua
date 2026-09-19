package com.lihua.web.handle;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.exception.BaseException;
import com.lihua.common.model.response.basecontroller.StrResponseController;
import com.lihua.web.exception.RateLimitException;
import com.lihua.web.utils.WebUtils;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Configuration
@Slf4j
public class GlobalExceptionHandle extends StrResponseController {

    /**
     * 通用业务异常处理
     */
    @ExceptionHandler(BaseException.class)
    public String handleBaseException(BaseException e) {
        log.error(e.getMessage(),e);
        return error(e.getResultCodeEnum(), e.getMessage(), e.getData());
    }

    /**
     * 捕获全局spring validation 异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errMessages = e
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining("；"));
        return error(ResultCodeEnum.PARAMS_MISSING, errMessages);
    }

    /**
     * 全局捕获直接在controller中校验的 validation 异常信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        String errMessages = Arrays.stream(e.getMessage().split(","))
                .map(item -> item.split(":"))
                .filter(item -> item.length > 1)
                .map(item -> item[1].trim())
                .distinct()
                .collect(Collectors.joining("；"));
        return error(ResultCodeEnum.PARAMS_MISSING, String.join("、", errMessages));
    }

    /**
     * 处理spring mvc 参数格式异常信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(),e);
        return error(ResultCodeEnum.PARAMS_ERROR,e.getMessage());
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage(),e);
        WebUtils.renderJson(error(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR));
    }

    /**
     * 处理405请求方法异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(),e);
        WebUtils.renderJson(error(ResultCodeEnum.REQUEST_METHOD_ERROR));
    }


    /**
     * 处理认证失败异常——凭据失败固定中文文案：getMessage() 为 Spring Security 默认英文
     * （Bad credentials），且用户不存在已被防枚举掩盖为同类异常，不透传
     */
    @ExceptionHandler(BadCredentialsException.class)
    public void handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(),e);
        WebUtils.renderJson(error(ResultCodeEnum.AUTHENTICATION_EXPIRED, "用户名或密码错误"));
    }

    /**
     * 处理接口限流异常（@RateLimit 窗口内超限，与重复提交同为频控 429）
     */
    @ExceptionHandler(RateLimitException.class)
    public void handleRateLimitException(RateLimitException e) {
        log.warn("接口限流触发: {}", e.getMessage());
        WebUtils.renderJson(error(ResultCodeEnum.REPEAT_SUBMIT_ERROR));
    }

    /**
     * 处理认证过程中的系统级故障（如数据库故障）——与凭据失败分离，
     * msg 固定枚举文案不透传 e.getMessage()（防底层细节外泄客户端），根因经日志排查
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public void handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        log.error(e.getMessage(),e);
        WebUtils.renderJson(error(ResultCodeEnum.SERVER_BAD_ERROR));
    }

    /**
     * 系统异常处理
     */
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        log.error(e.getMessage(),e);
        WebUtils.renderJson(500, error(ResultCodeEnum.SYSTEM_ERROR, e.getMessage()));
    }
}
