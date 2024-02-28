package com.lihua.cache;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    /**
     * 缓存基本对象：Integer、String、Object
     * @param key 缓存key
     * @param value 缓存值
     * @param <T>
     */
    public <T> void setCacheObject(String key,T value) {
        redisTemplate.opsForValue().set(key,value);
    }

    /**
     * 缓存基本对象：Integer、String、Object
     * @param key 缓存key
     * @param value 缓存值
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     * @param <T>
     */
    public <T> void setCacheObject(String key,T value,Long timeout,TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    /**
     * 根据key 获取基本对象
     * @param key
     * @return
     */
    public <T> T getCacheObject(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 缓存list 集合对象
     * @param key
     * @param value
     * @return
     * @param <T>
     */
    public <T> Long setCacheList(String key,List<T> value) {
        Long count = redisTemplate.opsForList().rightPushAll(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 获取list 集合对象
     * @param key
     * @return
     * @param <T>
     */
    public <T> List<T> getCacheList(String key) {
        return (List<T>) redisTemplate.opsForList().range(key,0,-1);
    }

    /**
     * 设置set 集合对象
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void setCacheSet(String key, Set<T> value) {
        BoundSetOperations<String, T> operations =
                (BoundSetOperations<String, T>) redisTemplate.boundSetOps(key);
        value.forEach(item -> {
            operations.add(item);
        });
    }

    /**
     * 获取缓存中的set 集合
     * @param key
     * @return
     * @param <T>
     */
    public <T> Set<T> getCacheSet(String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }


    /**
     * 缓存Map
     * @param key
     * @param value
     */
    public <T> void setCacheMap(String key, Map<String, T> value) {
        if (value != null) {
            redisTemplate.opsForHash().putAll(key, value);
        }
    }

    /**
     * 获得缓存的Map
     * @param key
     * @return
     */
    public <T> Map<Object, Object> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据 key 获取过期期间（分钟）
     * @param key
     * @return
     */
    public Long getExpireMinutes(String key) {
        return redisTemplate.getExpire(key,TimeUnit.MINUTES);
    }

    /**
     * 根据 key 获取过期期间（小时）
     * @param key
     * @return
     */
    public Long getExpireHours(String key) {
        return redisTemplate.getExpire(key,TimeUnit.HOURS);
    }

    /**
     * 根据 key 获取过期期间（天）
     * @param key
     * @return
     */
    public Long getExpireDays(String key) {
        return redisTemplate.getExpire(key,TimeUnit.DAYS);
    }

    /**
     * 根据 key 获取过期期间
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 指定key的过期时间（分钟）
     * @param key
     * @param time
     * @return
     */
    public Boolean setExpireMinutes(String key, Long time) {
        return setExpire(key, time, TimeUnit.MINUTES);
    }

    /**
     * 指定key的过期时间（小时）
     * @param key
     * @param time
     * @return
     */
    public Boolean setExpireHours(String key, Long time) {
        return setExpire(key, time, TimeUnit.HOURS);
    }

    /**
     * 指定key的过期时间（天）
     * @param key
     * @param time
     * @return
     */
    public Boolean setExpireDays(String key, Long time) {
        return setExpire(key, time, TimeUnit.DAYS);
    }

    /**
     * 指定key的过期时间
     * @param key
     * @param time
     * @param unit
     * @return
     */
    public Boolean setExpire(String key,Long time,TimeUnit unit) {
        return redisTemplate.expire(key, time, unit);
    }

    /**
     * 根据key删除缓存数据
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 根据多个key批量删除缓存数据
     * @param keys
     * @return
     */
    public Long delete(String... keys) {
        return redisTemplate.delete(List.of(keys));
    }

    /**
     * 指定 key 递增 val
     * @param key
     * @param val
     * @return
     */
    public Long increment(String key, long val) {
        return redisTemplate.opsForValue().increment(key,val);
    }
}
