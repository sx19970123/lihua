package com.lihua.cache.manager;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Component
public class LocalCacheManager {

    @Resource
    private Cache<String, Object> localCache;

    @Resource
    private JsonMapper jsonMapper;

    /**
     * 设置缓存
     */
    public <T> void setCache(String key, T value) {
        localCache.put(key, value);
    }

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T getCache(String key) {
        return (T) localCache.getIfPresent(key);
    }

    /**
     * 获取缓存（需要类型转换时使用）
     */
    public <T> T getCache(String key, Class<T> clazz) {
        Object val = localCache.getIfPresent(key);
        if (val == null) return null;

        if (clazz.isInstance(val)) {
            return clazz.cast(val);
        }

        return jsonMapper.convertValue(val, clazz);
    }

    /**
     * 泛型类型支持
     */
    public <T> T getCache(String key, JavaType javaType) {
        Object val = localCache.getIfPresent(key);
        if (val == null) return null;

        return jsonMapper.convertValue(val, javaType);
    }

    /**
     * 类型安全 fallback
     */
    public <T> T getWithFallback(String key, Class<T> clazz, BiFunction<String, Class<T>, T> fallback) {

        T cache = getCache(key, clazz);
        if (cache != null) {
            return cache;
        }

        T result = fallback.apply(key, clazz);

        if (result != null) {
            setCache(key, result);
        }

        return result;
    }

    /**
     * 泛型 fallback（List / Map）
     */
    public <T> T getWithFallback(String key, TypeReference<T> typeRef, Supplier<T> fallback) {

        JavaType javaType = jsonMapper.getTypeFactory().constructType(typeRef);

        T cache = getCache(key, javaType);
        if (cache != null) {
            return cache;
        }

        T result = fallback.get();

        if (result != null) {
            setCache(key, result);
        }

        return result;
    }

    /**
     * 删除缓存
     */
    public void remove(String key) {
        localCache.invalidate(key);
    }
}