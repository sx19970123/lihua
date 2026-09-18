package com.lihua.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流
 * <p>
 * 按「客户端 ip + 接口」维度限流：每 interval 秒内最多允许 rate 次调用，超出即拒绝（429）。
 * 配额经 Redisson RRateLimiter 在 Redis 侧令牌桶计数，多实例部署共享同一配额。
 * 适用于高消耗公开接口（验证码生成等）的刷接口防护；限流主体为 ip，无法防分布式多源攻击（那属 WAF 层）。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 窗口内允许的调用次数
     */
    int rate() default 1;

    /**
     * 窗口时长（秒）
     */
    int interval() default 1;

}
