package com.lihua.exception;

import com.lihua.enums.ResultCodeEnum;

/**
 * 通用服务异常
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {}

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getDefaultMsg());
    }

}
