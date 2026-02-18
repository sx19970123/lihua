package com.lihua.global;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.FileException;
import com.lihua.exception.IpIllegalException;
import com.lihua.exception.SensitiveException;
import com.lihua.exception.ServiceException;
import com.lihua.model.web.basecontroller.StrResponseController;
import com.lihua.utils.web.WebUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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
     * 捕获全局 RuntimeException 异常
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(Exception e) {
        log.error(e.getMessage(),e);
        return error(ResultCodeEnum.ERROR, e.getMessage());
    }

    /**
     * 捕获全局 ServiceException 异常
     */
    @ExceptionHandler(ServiceException.class)
    public String handleServiceException(Exception e) {
        log.error(e.getMessage(),e);
        return error(ResultCodeEnum.ERROR, e.getMessage());
    }

    /**
     * 捕获全局 FileException 异常
     */
    @ExceptionHandler(FileException.class)
    public String handleFileException(Exception e) {
        log.error(e.getMessage(),e);
        return error(ResultCodeEnum.FILE_ERROR, e.getMessage());
    }

    /**
     * 捕获全局 SensitiveException 异常
     */
    @ExceptionHandler(SensitiveException.class)
    public String handleSensitiveException(Exception e) {
        log.error(e.getMessage(),e);
        return error(ResultCodeEnum.SENSITIVE_ERROR, e.getMessage());
    }

    /**
     * 上传附件尺寸超过系统设置
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void handelMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletResponse response) {
        log.error(e.getMessage(),e);
        WebUtils.renderJson(response, error(ResultCodeEnum.MAX_UPLOAD_SIZE_EXCEEDED_ERROR));
    }

    /**
     * 捕获全局 IpIllegalException 异常
     */
    @ExceptionHandler(IpIllegalException.class)
    public String handleIpIllegalException() {
        return error(ResultCodeEnum.IP_ILLEGAL_ERROR);
    }

    /**
     * 捕获全局spring validation 异常信息
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
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
    @ExceptionHandler({ConstraintViolationException.class})
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
    public void handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletResponse response) {
        log.error(e.getMessage(),e);
        WebUtils.renderJson(response, error(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR));
    }

    /**
     * 处理405请求方法异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
        log.error(e.getMessage(),e);
        WebUtils.renderJson(response, error(ResultCodeEnum.REQUEST_METHOD_ERROR));
    }

    /**
     * 权限不足全局处理
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public String handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error(e.getMessage(),e);
        return error(ResultCodeEnum.ACCESS_ERROR);
    }
}
