package com.lihua.ip.exception;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.exception.BaseException;

/**
 * ip 非法异常
 */
public class IpIllegalException extends BaseException {
    public IpIllegalException() {
        super(ResultCodeEnum.IP_ILLEGAL_ERROR, null);
    }
}
