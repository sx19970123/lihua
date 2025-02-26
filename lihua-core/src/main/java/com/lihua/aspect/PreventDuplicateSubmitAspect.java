package com.lihua.aspect;

import com.lihua.annotation.PreventDuplicateSubmit;
import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.DuplicateSubmitException;
import com.lihua.utils.crypt.HashUtils;
import com.lihua.utils.web.WebUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * 系统限流处理
 * RateLimiter 注解的限流逻辑实现
 */
@Aspect
@Component
@Slf4j
public class PreventDuplicateSubmitAspect {

    @Resource
    private RedisCache redisCache;

    @Before("@annotation(preventDuplicateSubmit)")
    public void before(JoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) {
        // 获取请求
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String rateLimiterName =
                methodSignature.getDeclaringTypeName() + "." + methodSignature.getName() +
                "(" + Arrays.stream(methodSignature.getParameterTypes()).map(Class::getTypeName).collect(Collectors.joining(", ")) + ")";
        // 获取请求 token
        String token = WebUtils.getToken(WebUtils.getCurrentRequest());
        // 使用MD5 将key压缩
        rateLimiterName = HashUtils.generateMD5(rateLimiterName + token);

        // key 拼接前缀
        String key = SysBaseEnum.PREVENT_DUPLICATE_SUBMIT_REDIS_PREFIX.getValue() + rateLimiterName;
        // redis 中key存在则抛出重复提交异常
        if (redisCache.hasKey(key)) {
            throw new DuplicateSubmitException();
        }

        // 向redis 中插入临时数据
        redisCache.setCacheObject(key,
                "",
                preventDuplicateSubmit.value(),
                preventDuplicateSubmit.timeUnit());
    }


}
