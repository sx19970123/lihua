package com.lihua.exception;

import com.lihua.enums.ResultCodeEnum;

/**
 * 认证失效异常，由全局异常处理器统一处理
 */
public class SecurityAccessDeniedException extends BaseException {

    public SecurityAccessDeniedException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum, null);
    }

}
