package com.lihua.web.handle;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.exception.BaseException;
import com.lihua.common.model.response.ResponseModel;
import com.lihua.common.model.response.Responses;
import com.lihua.web.exception.RateLimitException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
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
public class GlobalExceptionHandle {

    /**
     * 通用业务异常处理
     */
    @ExceptionHandler(BaseException.class)
    public ResponseModel<?> handleBaseException(BaseException e) {
        log.error(e.getMessage(),e);
        return Responses.error(e.getResultCodeEnum(), e.getMessage(), e.getData());
    }

    /**
     * 捕获全局spring validation 异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseModel<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errMessages = e
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining("；"));
        return Responses.error(ResultCodeEnum.PARAMS_MISSING, errMessages);
    }

    /**
     * 全局捕获直接在controller中校验的 validation 异常信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseModel<?> handleConstraintViolationException(ConstraintViolationException e) {
        String errMessages = Arrays.stream(e.getMessage().split(","))
                .map(item -> item.split(":"))
                .filter(item -> item.length > 1)
                .map(item -> item[1].trim())
                .distinct()
                .collect(Collectors.joining("；"));
        return Responses.error(ResultCodeEnum.PARAMS_MISSING, String.join("、", errMessages));
    }

    /**
     * 处理spring mvc 参数格式异常信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseModel<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(),e);
        return Responses.error(ResultCodeEnum.PARAMS_ERROR,e.getMessage());
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseModel<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage(),e);
        return Responses.error(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR);
    }

    /**
     * 处理405请求方法异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseModel<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(),e);
        return Responses.error(ResultCodeEnum.REQUEST_METHOD_ERROR);
    }


    /**
     * 处理认证失败异常——凭据失败固定中文文案：getMessage() 为 Spring Security 默认英文
     * （Bad credentials），且用户不存在已被防枚举掩盖为同类异常，不透传
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseModel<?> handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(),e);
        return Responses.error(ResultCodeEnum.AUTHENTICATION_EXPIRED, "用户名或密码错误");
    }

    /**
     * 处理接口限流异常（@RateLimit 窗口内超限，与重复提交同为频控 429）
     */
    @ExceptionHandler(RateLimitException.class)
    public ResponseModel<?> handleRateLimitException(RateLimitException e) {
        log.warn("接口限流触发: {}", e.getMessage());
        return Responses.error(ResultCodeEnum.REPEAT_SUBMIT_ERROR);
    }

    /**
     * 处理认证过程中的系统级故障（如数据库故障）——与凭据失败分离，
     * msg 固定枚举文案不透传 e.getMessage()（防底层细节外泄客户端），根因经日志排查
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseModel<?> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        log.error(e.getMessage(),e);
        return Responses.error(ResultCodeEnum.SERVER_BAD_ERROR);
    }

    /**
     * 处理接口权限不足（@PreAuthorize 方法级鉴权失败，AuthorizationDeniedException 为其子类）
     * ——固定枚举文案不透传内部细节（原兜底包装成系统异常）
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseModel<?> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("接口权限不足: {}", e.getMessage());
        return Responses.error(ResultCodeEnum.ACCESS_ERROR);
    }

    /**
     * 系统异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseModel<?> handleException(Exception e) {
        log.error(e.getMessage(),e);
        // 固定枚举文案：e.getMessage 可能携带 NPE/SQL/文件路径等内部细节，不透传客户端（与 InternalAuthenticationServiceException 同口径），根因经日志排查
        return Responses.error(ResultCodeEnum.SYSTEM_ERROR);
    }
}
