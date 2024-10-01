package com.lihua.exception;

import com.lihua.enums.ResultCodeEnum;

/**
 * 系统限流异常
 */
public class RateLimiterException extends RuntimeException {
    public RateLimiterException() {
        super(ResultCodeEnum.RATE_LIMITER_ERROR.getDefaultMsg());
    }
}
