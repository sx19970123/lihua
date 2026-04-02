package com.lihua.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.lihua.cache.enums.RedisKeyPrefixEnum;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置
 */
@Component
@Configurable
public class LocalCacheConfig {

    // 初始化时排除本地缓存TTL为 null 的 redis Key
    private final List<RedisKeyPrefixEnum> REDIS_KEY_PREFIX = RedisKeyPrefixEnum.getRedisKeyPrefix()
            .stream()
            .filter(redisKeyPrefixEnum -> redisKeyPrefixEnum.getLocalTTL() != null)
            .toList();

    @Bean
    public Cache<String, Object> initCaffeine() {
        return Caffeine.newBuilder()
                .expireAfter(new Expiry<>() {
                    // 设置缓存时指定ttl
                    @Override
                    public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
                        return setExpirationTime(key.toString());
                    }
                    // 更新缓存时指定ttl
                    @Override
                    public long expireAfterUpdate(@NonNull Object key, @NonNull Object value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                    // 读取缓存时指定ttl
                    @Override
                    public long expireAfterRead(@NonNull Object key, @NonNull Object value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .initialCapacity(100)
                .maximumSize(10000)
                .build();
    }

    /**
     * 设置过期时间
     * @param key 缓存key
     * @return 缓存过期时间
     */
    private long setExpirationTime(String key) {
        for (RedisKeyPrefixEnum prefix : REDIS_KEY_PREFIX) {
            if (key.startsWith(prefix.getValue())) {
                return TimeUnit.SECONDS.toNanos(prefix.getLocalTTL());
            }
        }
        return TimeUnit.SECONDS.toNanos(10);
    }

}
