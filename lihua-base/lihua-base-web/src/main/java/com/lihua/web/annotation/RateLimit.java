package com.lihua.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流
 * <p>
 * 按「客户端 ip + 接口」维度限流：滑动窗口语义——任意 interval 秒窗口内最多放行 rate 次，超出即拒绝（429）。
 * 配额经 Redisson RRateLimiter 在 Redis 侧计数（每次放行记时间戳，早于 now-interval 的记录滚动退还配额），
 * 多实例部署共享同一配额。触发后的复原是滚动的：窗口内最早一次调用满 interval 秒出窗即放行下一次，
 * 最长等待不超过 interval 秒，无需等整个窗口清空。
 * 适用于高消耗公开接口（验证码生成等）的刷接口防护；限流主体为 ip，无法防分布式多源攻击（那属 WAF 层）。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 窗口内允许的调用次数（默认 10：容人类连点与小型 NAT 共享 ip，拦持续刷接口）
     */
    int rate() default 10;

    /**
     * 窗口时长（秒），亦是触发后最长复原等待；默认 10 即限速 1 次/秒、可突发 10 次
     */
    int interval() default 10;

}
