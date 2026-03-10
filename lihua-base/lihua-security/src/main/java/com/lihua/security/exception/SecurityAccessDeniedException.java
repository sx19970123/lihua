package com.lihua.security.exception;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.exception.BaseException;

/**
 * 认证失效异常，由全局异常处理器统一处理
 */
public class SecurityAccessDeniedException extends BaseException {

    public SecurityAccessDeniedException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum, null);
    }

}
