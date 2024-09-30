package com.lihua.cache;

import jakarta.annotation.Resource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * 缓存数据
     * @param key 缓存key
     * @param value 缓存值
     */
    public void setCacheObject(String key,T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存数据
     * @param key 缓存key
     * @param value 缓存值
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     */
    public void setCacheObject(String key,T value,Long timeout,TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    /**
     * 根据key 获取基本对象
     * @param key
     * @return
     */
    public T getCacheObject(String key) {
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        return operations.get(key);
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
     * 根据前缀获取存在的keys
     * @param prefix
     * @return
     */
    public Set<String> keys(String prefix) {
        return redisTemplate.keys(prefix + "*");
    }

    /**
     * 获取redis中所有key
     * @return
     */
    public Set<String> keys() {
       return redisTemplate.keys("*");
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

    /**
     * 获取内存占用情况
     * @return
     */
    public String memoryInfo() {
        RedisConnection connection = redisTemplate.getRequiredConnectionFactory().getConnection();
        Properties memory = connection.commands().info("memory");
        if (memory != null) {
            return new DecimalFormat("#.##").format(Double.parseDouble(String.valueOf(memory.get("used_memory")))/1024/1024);
        }
        return "-";
    }
}
