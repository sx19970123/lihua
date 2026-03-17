package com.lihua.ip.interceptor;

import com.lihua.ip.exception.IpIllegalException;
import com.lihua.ip.utils.IpUtils;
import com.lihua.redis.cache.RedisCache;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lihua.redis.enums.RedisKeyPrefixEnum.SYSTEM_IP_BLACKLIST_REDIS_PREFIX;

@Component
@Slf4j
public class RequestIpInterceptor implements HandlerInterceptor {

    @Resource
    private RedisCache redisCache;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        ipMatch();
        return true;
    }

    // ip 匹配
    private void ipMatch() {
        List<String> prohibitIpList = redisCache.getCacheList(SYSTEM_IP_BLACKLIST_REDIS_PREFIX.getValue(), String.class);
        if (!prohibitIpList.isEmpty()) {
            prohibitIpList.forEach(ip -> {

                String regex = ip
                        .replace(".", "\\.")
                        .replace("*", ".*")
                        .replace("?", ".");

                regex = "^" + regex + "$";

                String currentIp = IpUtils.getIpAddress();
                Pattern compiledPattern = Pattern.compile(regex);
                Matcher matcher = compiledPattern.matcher(currentIp);

                if (matcher.matches()) {
                    log.error("异常ip【{}】请求已拒绝", currentIp);
                    throw new IpIllegalException();
                }
            });
        }
    }
}
