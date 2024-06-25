package com.lihua.exception;

import com.lihua.enums.ResultCodeEnum;

public class ServiceException extends RuntimeException {

    public ServiceException() {}

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getDefaultMsg());
    }

}
