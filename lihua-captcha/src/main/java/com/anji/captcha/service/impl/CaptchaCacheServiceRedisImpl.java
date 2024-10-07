package com.anji.captcha.service.impl;

import com.anji.captcha.service.CaptchaCacheService;
import com.lihua.cache.RedisCache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
public class CaptchaCacheServiceRedisImpl implements CaptchaCacheService {

    @Resource
    private RedisCache redisCache;

    @Override
    public void set(String s, String s1, long l) {
        redisCache.setCacheObject(s,s1,l, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String s) {
        return redisCache.hasKey(s);
    }

    @Override
    public void delete(String s) {
        redisCache.delete(s);
    }

    @Override
    public String get(String s) {
        return redisCache.getCacheObject(s, String.class);
    }

    @Override
    public String type() {
        return "redis";
    }

    @Override
    public Long increment(String key, long val) {
        return redisCache.increment(key,val);
    }
}
