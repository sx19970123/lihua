package com.lihua.web.aspect;

import com.lihua.cache.enums.RedisKeyPrefixEnum;
import com.lihua.web.annotation.RateLimit;
import com.lihua.web.exception.RateLimitException;
import com.lihua.web.utils.WebUtils;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 接口限流切面
 * <p>
 * 限流键 = 前缀 + 接口签名 + 客户端 ip，经 Redisson RRateLimiter 令牌桶在 Redis 侧计数（多实例共享配额）。
 * trySetRate 仅在键首次创建时生效——注解参数调整后需删除存量键（或换键）方对新调用生效，防误改线上限流值即时重置。
 * 键每次访问续期（窗口 + 60s 空闲清理），闲置自动过期删除，防止高频多样化 ip 键常驻累积。
 */
@Aspect
@Component
public class RateLimitAspect {

    /**
     * 限流键空闲过期附加时长（秒）：在限流窗口基础上加宽，保证窗口语义完整又让闲置键及时清理
     */
    private static final int IDLE_EXTRA_SECONDS = 60;

    @Resource
    private RedissonClient redissonClient;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = buildKey(joinPoint);
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        limiter.trySetRate(RateType.OVERALL, rateLimit.rate(), rateLimit.interval(), RateIntervalUnit.SECONDS);
        if (!limiter.tryAcquire()) {
            throw new RateLimitException();
        }
        limiter.expireAsync(Duration.ofSeconds(rateLimit.interval() + IDLE_EXTRA_SECONDS));
        return joinPoint.proceed();
    }

    private String buildKey(ProceedingJoinPoint joinPoint) {
        String signature = joinPoint.getSignature().toShortString();
        String ip = WebUtils.getIpAddress();
        return RedisKeyPrefixEnum.RATE_LIMIT_REDIS_PREFIX.getValue()
                + signature + ":" + (ip == null ? "" : ip);
    }

}
