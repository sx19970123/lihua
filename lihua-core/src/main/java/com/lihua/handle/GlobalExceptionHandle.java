package com.lihua.handle;

import com.lihua.enums.AxiosResultCodeEnum;
import com.lihua.model.AxiosResult;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Configuration
public class GlobalExceptionHandle extends AxiosResult {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        e.printStackTrace();
        return error(AxiosResultCodeEnum.ERROR, e.getMessage());
    }

    /**
     * 捕获全局 RuntimeException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(Exception e) {
        e.printStackTrace();
        return error(AxiosResultCodeEnum.ERROR, e.getMessage());
    }
}
