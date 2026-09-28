package com.lihua.web.exception;

/**
 * 触发接口限流（{@code @RateLimit} 窗口内调用次数超限）
 */
public class RateLimitException extends RuntimeException {

    public RateLimitException() {
        super();
    }

}
