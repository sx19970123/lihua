package com.lihua.utils.security;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.exception.ServiceException;
import com.lihua.utils.spring.SpringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TemporaryTokenUtils {


    /**
     * 创建临时token
     * @param fileFullPath
     * @return
     */
    public static String createTemporaryToken(String fileFullPath) {
        String uuid = UUID.randomUUID().toString();
        String jwt = JwtUtils.create(uuid);

        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);

        // 设置 redis 缓存
        if (lihuaConfig.getTemporaryTokenExpireTime() <= 0) {
            redisCache.setCacheObject(SysBaseEnum.TEMPORARY_TOKEN_REDIS_PREFIX + uuid, fileFullPath);
        } else {
            redisCache.setCacheObject(SysBaseEnum.TEMPORARY_TOKEN_REDIS_PREFIX + uuid, fileFullPath, lihuaConfig.getTemporaryTokenExpireTime(), TimeUnit.MINUTES);
        }

        return jwt;
    }


    /**
     * 根据临时token获取文件全路径
     * @param temporaryToken
     * @return
     */
    public static String getFileFullPathByTemporaryToken(String temporaryToken) {
        // 验证 token
        try {
            JwtUtils.verify(temporaryToken);
        } catch (Exception e) {
            throw new ServiceException("无效的token");
        }

        // token 解码获得redis key
        String decode = JwtUtils.decode(temporaryToken);

        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);

        Object cacheObject = redisCache.getCacheObject(SysBaseEnum.TEMPORARY_TOKEN_REDIS_PREFIX + decode);

        // 配置了过期时间小<=0后立即删除缓存
        if (lihuaConfig.getTemporaryTokenExpireTime() <= 0) {
            redisCache.delete(temporaryToken);
        }

        // 缓存取出的内容为空时，抛出token失效异常
        if (cacheObject == null) {
            throw new FileException(ResultCodeEnum.ACCESS_EXPIRED_ERROR);
        }

        // 返回文件全路径
        return cacheObject.toString();
    }

}
