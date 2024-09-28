package com.lihua.exception;

/**
 * ip 非法异常
 */
public class IpIllegalException extends RuntimeException {
    public IpIllegalException() {
        super("异常ip请求已拒绝");
    }
}
