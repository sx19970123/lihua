package com.lihua.web.aspect;

import com.lihua.cache.enums.RedisKeyPrefixEnum;
import com.lihua.common.utils.json.JsonUtils;
import com.lihua.web.annotation.PreventDuplicateSubmit;
import com.lihua.web.exception.DuplicateSubmitException;
import com.lihua.web.utils.WebUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * 防重复提交切面
 * <p>
 * 幂等键 = 前缀 + 主体（token，匿名退客户端 ip）+ URI + 业务参数摘要，经 SET NX PX 原子占键：
 * 占键成功放行执行，占键失败即窗口期内重复提交。键不主动删除，TTL 到期自然放行——执行中的
 * 第二次双击同样被拦截，且参数变化会生成新键、不影响修正后重提。
 */
@Aspect
@Component
public class PreventDuplicateSubmitAspect {

    /**
     * 摘要截断长度（hex 字符数）：64bit 摘要容量对本场景碰撞强度足够，同时控制键长度
     */
    private static final int DIGEST_HEX_LENGTH = 16;

    @Resource
    private RedissonClient redissonClient;

    @Around("@annotation(preventDuplicateSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) throws Throwable {
        String key = buildKey(joinPoint, preventDuplicateSubmit);
        RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        if (!bucket.setIfAbsent("1", Duration.ofSeconds(preventDuplicateSubmit.interval()))) {
            throw new DuplicateSubmitException();
        }
        return joinPoint.proceed();
    }

    private String buildKey(JoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) {
        HttpServletRequest request = WebUtils.getCurrentRequest();
        String identity = resolveIdentity(request);
        String uri = request == null ? "" : request.getRequestURI();
        // excludeParams 与 @Log 同源：摘要前剔除敏感字段，避免明文密码等经幂等键被离线碰撞
        String argsJson = JsonUtils.excludeJsonKey(
                JsonUtils.toJsonOrCanonicalName(filterArgs(joinPoint)),
                Arrays.asList(preventDuplicateSubmit.excludeParams()));
        String argsDigest = md5Hex(argsJson);
        return RedisKeyPrefixEnum.PREVENT_DUPLICATE_SUBMIT_REDIS_PREFIX.getValue()
                + identity + ":" + uri + ":" + argsDigest;
    }

    /**
     * 主体段：已登录会话取 token 摘要（多端登录 token 不同、互不干扰拦截），匿名请求退客户端 ip
     */
    private String resolveIdentity(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String token = WebUtils.getToken(request);
        if (StringUtils.hasText(token)) {
            return md5Hex(token);
        }
        String ip = WebUtils.getIpAddress(request);
        return ip == null ? "" : ip;
    }

    /**
     * 剔除 servlet api、校验结果与文件等框架参数，只对业务参数摘要
     */
    private Object[] filterArgs(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .filter(arg -> !(arg instanceof ServletRequest || arg instanceof ServletResponse
                        || arg instanceof BindingResult || arg instanceof MultipartFile))
                .toArray();
    }

    private String md5Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes, 0, DIGEST_HEX_LENGTH / 2);
        } catch (NoSuchAlgorithmException e) {
            // JDK 规范保证 MD5 算法存在，此处不可达
            throw new IllegalStateException(e);
        }
    }

}
