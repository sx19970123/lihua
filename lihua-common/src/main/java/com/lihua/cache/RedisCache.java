package com.lihua.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RedisCache {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 缓存数据
     * @param key 缓存key
     * @param value 缓存值
     */
    public <T> void setCacheObject(String key,T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存数据
     * @param key 缓存key
     * @param value 缓存值
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     */
    public <T> void setCacheObject(String key,T value,Long timeout,TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    /**
     * 缓存集合
     * @param key 缓存key
     * @param valueList 集合数据
     * @param <T> 集合类型
     */
    public <T> void setCacheList(String key, List<T> valueList) {
        redisTemplate.opsForList().rightPushAll(key, valueList.toArray());
    }

    /**
     * 根据 key 获取基本对象
     * @param key redisKey
     * @param clazz 对象类型
     * @return 目标对象
     */
    public <T> T getCacheObject(String key, Class<T> clazz) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        Object value = operations.get(key);

        // 检查获取的值是否为 null
        if (value == null) {
            return null;
        }

        // 使用 ObjectMapper 转换为目标类型
        return objectMapper.convertValue(value, clazz);
    }


    /**
     * 根据 key 获取集合对象
     * @param key redisKey
     * @param clazz 对象类型
     * @return 返回的集合值
     */
    @SneakyThrows
    public <T> List<T> getCacheList(String key, Class<T> clazz) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        Long size = listOperations.size(key);

        if (size == null || size == 0) {
            return null; // 或者抛出自定义异常
        }

        List<Object> range = listOperations.range(key, 0,  - 1);

        if (range == null) {
            return new ArrayList<>();
        }

        return range.stream()
                .map(value -> objectMapper.convertValue(value, clazz))
                .collect(Collectors.toList());
    }

    /**
     * 判断key是否存在
     * @param key redisKey
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据前缀获取存在的keys
     * @param prefix redisKey前缀
     */
    public Set<String> keys(String prefix) {
        return redisTemplate.keys(prefix + "*");
    }

    /**
     * 获取redis中所有key
     */
    public Set<String> keys() {
       return redisTemplate.keys("*");
    }

    /**
     * 根据 key 获取过期期间（分钟）
     * @param key redisKey
     */
    public Long getExpireMinutes(String key) {
        return redisTemplate.getExpire(key,TimeUnit.MINUTES);
    }

    /**
     * 指定key的过期时间
     * @param key redisKey
     * @param time 时间
     * @param unit 单位
     */
    public void setExpire(String key,Long time,TimeUnit unit) {
        redisTemplate.expire(key, time, unit);
    }

    /**
     * 根据key删除缓存数据
     * @param key redisKey
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 根据多个key批量删除缓存数据
     * @param keys redisKey
     */
    public Long delete(String... keys) {
        return redisTemplate.delete(List.of(keys));
    }

    /**
     * 指定 key 递增 val
     * @param key redisKey
     * @param val 递增数值
     */
    public Long increment(String key, long val) {
        return redisTemplate.opsForValue().increment(key,val);
    }

    /**
     * 获取内存占用情况
     */
    public String memoryInfo() {
        RedisConnection connection = redisTemplate.getRequiredConnectionFactory().getConnection();
        Properties memory = connection.commands().info("memory");
        if (memory != null) {
            return new DecimalFormat("#.##").format(Double.parseDouble(String.valueOf(memory.get("used_memory")))/1024/1024);
        }
        return "-";
    }

    /**
     * 获取 key 对应的value在redis中对应的数据类型
     * @return 返回值包括：string、list、set、hash、zset等
     */
    public String getRedisType(String key) {
        return Objects.requireNonNull(redisTemplate.type(key)).code();
    }
}
