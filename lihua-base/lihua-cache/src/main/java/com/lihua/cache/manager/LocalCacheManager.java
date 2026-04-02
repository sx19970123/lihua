package com.lihua.cache.manager;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;
import java.util.function.BiFunction;

@Component
public class LocalCacheManager {

    @Resource
    private Cache<String, Object> localCache;

    @Resource
    private JsonMapper jsonMapper;

    /**
     * 设置缓存
     * key 前缀为 RedisKeyPrefixEnum 维护的值，主动过期时间根据设置，
     * 否则10s自动过期（LocalCacheConfig 中进行设置）
     * @param key 缓存key
     * @param value 缓存value
     */
    public <T> void setCache(String key, T value) {
        localCache.put(key, value);
    }

    /**
     * 获取缓存数据
     * @param key 缓存key
     * @param clazz 缓存数据类型
     * @return 缓存value
     */
    public <T> T getCache(String key, Class<T> clazz) {
        Object ifPresent = localCache.getIfPresent(key);
        if (ifPresent == null) {
            return null;
        }
        return jsonMapper.convertValue(ifPresent, clazz);
    }

    /**
     * 降级获取缓存数据，优先获取本地缓存数据，本地缓存不存在，通过 fallback 获取数据，获取到后再回填到本地缓存
     * @param key 缓存key
     * @param clazz 缓存数据类型
     * @param fallback 降级获取缓存逻辑
     * @return 缓存value
     */
    public <T> T getWithFallback(String key, Class<T> clazz, BiFunction<String, Class<T>, T> fallback) {
        // 本地缓存获取
        T cache = getCache(key, clazz);
        if (cache != null) {
            return cache;
        }
        // 降级获取
        T apply = fallback.apply(key, clazz);
        // 获取后回填本地缓存
        if (apply != null) {
            setCache(key, apply);
        }
        return apply;
    }

    /**
     * 删除缓存
     * @param key 缓存key
     */
    public void remove(String key) {
        localCache.invalidate(key);
    }

}
