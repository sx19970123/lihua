package com.lihua.handle;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.web.BaseController;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

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
     * 捕获全局spring validation 异常信息
     * @param e
     * @return
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

        return error(ResultCodeEnum.PARAMS_ERROR, errMessages);
    }

}
