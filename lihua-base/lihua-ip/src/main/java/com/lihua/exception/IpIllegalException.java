package com.lihua.exception;

import enums.ResultCodeEnum;

/**
 * ip 非法异常
 */
public class IpIllegalException extends RuntimeException {
    public IpIllegalException() {
        super(ResultCodeEnum.IP_ILLEGAL_ERROR.getDefaultMsg());
    }
}
