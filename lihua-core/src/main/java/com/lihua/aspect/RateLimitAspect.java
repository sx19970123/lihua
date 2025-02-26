package com.lihua.aspect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.lihua.exception.RateLimiterException;
import com.lihua.utils.crypt.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 系统限流处理
 * @RateLimiter 注解的限流逻辑实现
 */
@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    // 限流器缓存，作用类似ConcurrentHashMap，超过指定时间会移除对应的key
    private final Cache<String, RateLimiter> RATE_LIMITER_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)  // 限流器1小时未被访问则过期
            .build();

    @Before("@annotation(rateLimiterAnnotation)")
    public void before(JoinPoint joinPoint, com.lihua.annotation.RateLimiter rateLimiterAnnotation) throws ExecutionException {
        // 获取限流器名称，作为map的key，取用全限定路径+方法名+参数
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String rateLimiterName =
                methodSignature.getDeclaringTypeName() + "." + methodSignature.getName() +
                "(" + Arrays.stream(methodSignature.getParameterTypes()).map(Class::getTypeName).collect(Collectors.joining(", ")) + ")";

        // 使用MD5 将key压缩
        rateLimiterName = HashUtils.generateMD5(rateLimiterName);

        // 获取限流器，不存在则进行创建
        RateLimiter rateLimiter = RATE_LIMITER_CACHE.get(rateLimiterName,
                () -> RateLimiter.create(rateLimiterAnnotation.value()));

        // 尝试获取令牌，获取失败即被限流
        if (!rateLimiter.tryAcquire()){
            throw new RateLimiterException();
        }
    }

}
