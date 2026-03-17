package com.lihua.redis.cache;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.redisson.api.options.KeysScanOptions;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisCache {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JsonMapper jsonMapper;

    /**
     * 缓存数据
     * @param key 缓存key
     * @param value 缓存值
     */
    public <T> void setCacheObject(String key,T value) {
        redissonClient.getBucket(key).set(value);
    }

    /**
     * 缓存数据
     * @param key 缓存key
     * @param value 缓存值
     * @param duration 过期时间
     */
    public <T> void setCacheObject(String key, T value, Duration duration) {
        redissonClient.getBucket(key).set(value, duration);
    }

    /**
     * 缓存集合
     * @param key 缓存key
     * @param valueList 集合数据
     */
    public <T> void setCacheList(String key, List<T> valueList) {
        RList<Object> list = redissonClient.getList(key);
        list.clear();
        list.addAll(valueList);
    }

    /**
     * 缓存整个 map
     * @param key RedisKey
     * @param map map集合
     */
    public <T> void setCacheMap(String key, Map<String, T> map) {
        RMap<Object, Object> rMap = redissonClient.getMap(key);
        rMap.clear();
        rMap.putAll(map);
    }

    /**
     * 缓存map中单条数据
     * @param key RedisKey
     * @param mapKey MapKey
     * @param mapValue MapValue
     */
    public <T> void setCacheMapItem(String key, String mapKey, T mapValue) {
        redissonClient.getMap(key).put(mapKey, mapValue);
    }

    /**
     * 获取redis hash key 对应的 value
     * @param key redis key
     * @param mapKey hash key
     * @param clazz key 对应 value 的类型
     */
    public <T> T getCacheMapItem(String key, String mapKey, Class<T> clazz) {
        Object o = redissonClient.getMap(key).get(mapKey);
        if (o == null) {
            return null;
        }

        return jsonMapper.convertValue(o, clazz);
    }

    /**
     * 删除 redis hash 指定的 key
     * @param key redis key
     * @param mapKey map对应的key
     */
    public void removeMapItem(String key, String mapKey) {
        redissonClient.getMap(key).remove(mapKey);
    }


    /**
     * 根据 key 获取基本对象
     * @param key redisKey
     * @param clazz 对象类型
     * @return 目标对象
     */
    public <T> T getCacheObject(String key, Class<T> clazz) {
        Object value = redissonClient.getBucket(key).get();

        // 检查获取的值是否为 null
        if (value == null) {
            return null;
        }

        // 使用 jsonMapper 转换为目标类型
        return jsonMapper.convertValue(value, clazz);
    }


    /**
     * 根据 key 获取集合对象
     * @param key redisKey
     * @param clazz 对象类型
     * @return 返回的集合值
     */
    public <T> List<T> getCacheList(String key, Class<T> clazz) {
        RList<Object> list = redissonClient.getList(key);
        // 格式转换
        List<T> resultList = new ArrayList<>(list.size());
        list.forEach(item -> resultList.add(jsonMapper.convertValue(item, clazz)));
        return resultList;
    }

    /**
     * 根据 redisKey 获取 Map
     * @param key redisKey
     * @param clazz MapKey
     * @return 获取的Map
     * @param <T> MapValue类型
     */
    public <T> Map<String, T> getCacheMap(String key, Class<T> clazz) {
        RMap<Object, Object> map = redissonClient.getMap(key);
        // 格式转换
        Map<String, T> resultMap = new HashMap<>(map.size());
        map.forEach((k, v) -> resultMap.put(k.toString(), jsonMapper.convertValue(v, clazz)));
        return resultMap;
    }

    /**
     * 根据前缀获取存在的keys
     * @param prefix redisKey前缀
     */
    public Set<String> keys(String prefix) {
        return scanKeys(prefix + "*");
    }

    /**
     * 获取redis中所有key
     */
    public Set<String> keys() {
        return scanKeys("*");
    }

    /**
     * 按规则匹配 redisKeys
     * @param pattern redis 匹配规则
     * @return 目标keys
     */
    public Set<String> scanKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        RKeys resultKeys = redissonClient.getKeys();
        // 目标keys放入set集合
        for (String key : resultKeys.getKeys(KeysScanOptions.defaults().pattern(pattern).chunkSize(100))) {
            keys.add(key);
        }
        return keys;
    }

    /**
     * 根据 key 获取剩余过期时间（分钟）
     * @param key redisKey
     */
    public Long getExpireMinutes(String key) {
        RExpirable expirable = redissonClient.getBucket(key);
        // 返回剩余过期时间（毫秒）
        long expireTime = expirable.remainTimeToLive();

        // -1 永不过期；-2不存在
        if (expireTime < 0) {
            return expireTime;
        }

        // 转为分钟
        return TimeUnit.MILLISECONDS.toMinutes(expireTime);
    }

    /**
     * 指定key的过期时间
     * @param key redisKey
     * @param duration 过期时间
     */
    public void setExpire(String key, Duration duration) {
        RExpirable expirable = redissonClient.getBucket(key);
        expirable.expire(duration);
    }

    /**
     * 根据key删除缓存数据
     * @param key redisKey
     */
    public Boolean delete(String key) {
       return redissonClient.getKeys().delete(key) == 1;
    }

    /**
     * 根据多个key批量删除缓存数据
     * @param keys redisKey
     */
    public Long delete(String... keys) {
        return redissonClient.getKeys().delete(keys);
    }

    /**
     * 根据多个key批量删除缓存数据
     * @param keys redisKey
     */
    public Long delete(Collection<String> keys) {
        return delete(keys.toArray(new String[0]));
    }

    /**
     * 获取 key 对应的value在redis中对应的数据类型
     * @return 返回值包括：string、list、set、hash、zset等
     */
    public String getRedisType(String key) {
        RKeys rKeys = redissonClient.getKeys();
        return rKeys.getType(key).name().toLowerCase();
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

}
