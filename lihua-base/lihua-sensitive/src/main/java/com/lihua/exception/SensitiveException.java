package com.lihua.exception;


import enums.ResultCodeEnum;

/**
 * 数据脱敏异常
 */
public class SensitiveException extends RuntimeException {
    public SensitiveException(String message) {
        super(ResultCodeEnum.SENSITIVE_ERROR.getDefaultMsg() + "，" + message);
    }
}
