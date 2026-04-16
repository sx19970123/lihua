package com.lihua.common.exception;


import com.lihua.common.enums.ResultCodeEnum;

/**
 * 通用服务异常
 */
public class ServiceException extends BaseException {

    public ServiceException() {
        super(ResultCodeEnum.ERROR, null);
    }

    public ServiceException(String message) {
        super(ResultCodeEnum.ERROR, message, null);
    }

    public ServiceException(String message, Object data) {
        super(ResultCodeEnum.ERROR, message, data);
    }
}
