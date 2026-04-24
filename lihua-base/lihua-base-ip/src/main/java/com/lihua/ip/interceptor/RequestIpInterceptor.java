package com.lihua.ip.interceptor;

import com.lihua.cache.manager.LocalCacheManager;
import com.lihua.ip.exception.IpIllegalException;
import com.lihua.ip.utils.IpUtils;
import com.lihua.cache.manager.RedisCacheManager;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tools.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static com.lihua.cache.enums.RedisKeyPrefixEnum.SYSTEM_IP_BLACKLIST_REDIS_PREFIX;

@Component
@Slf4j
public class RequestIpInterceptor implements HandlerInterceptor {

    @Resource
    private RedisCacheManager redisCacheManager;

    @Resource
    private LocalCacheManager localCacheManager;

    // ip匹配缓存
    private final Map<String, Pattern> patternCache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        ipMatch();
        return true;
    }

    // ip 匹配
    private void ipMatch() {
        String key = SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue();
        List<String> prohibitIpList = localCacheManager.getWithFallback(key, new TypeReference<>(){}, () -> redisCacheManager.getCacheList(SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue(), String.class));

        if (prohibitIpList == null || prohibitIpList.isEmpty()) {
            return;
        }

        String currentIp = IpUtils.getIpAddress();
        if (currentIp == null) {
            return;
        }

        for (String ipRule : prohibitIpList) {
            // 匹配并缓存黑名单ip
            Pattern pattern = patternCache.computeIfAbsent(ipRule, rule -> {
                String regex = rule
                        .replace(".", "\\.")
                        .replace("*", ".*")
                        .replace("?", ".");
                regex = "^" + regex + "$";
                return Pattern.compile(regex);
            });
            // 匹配到的黑名单直接抛出异常
            if (pattern.matcher(currentIp).matches()) {
                log.error("异常ip【{}】请求已拒绝", currentIp);
                throw new IpIllegalException();
            }
        }
    }
}
