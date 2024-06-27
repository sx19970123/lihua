package com.lihua.utils.security;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.utils.spring.SpringUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileDownloadManager {

    /**
     * 将单个文件路径添加到可下载列表中
     *
     * @param fileFullPath 文件全路径
     * @return 原路径
     */
    public static String addToDownloadableList(String fileFullPath) {
        return addToDownloadableList(fileFullPath, ",");
    }

    /**
     * 将多个文件路径添加到可下载列表中
     *
     * @param fileFullPathList 文件全路径列表
     * @return 原路径列表
     */
    public static List<String> addToDownloadableList(List<String> fileFullPathList) {
        return addToDownloadableList(fileFullPathList, ",");
    }

    /**
     * 将多个文件路径添加到可下载列表中
     *
     * @param fileFullPathList 文件全路径列表
     * @param split 分隔符
     * @return 原路径列表
     */
    public static List<String> addToDownloadableList(List<String> fileFullPathList, String split) {
        if (fileFullPathList == null || fileFullPathList.isEmpty()) {
            return fileFullPathList;
        }

        fileFullPathList.forEach(path -> addToDownloadableList(path, split));
        return fileFullPathList;
    }

    /**
     * 将文件路径添加到可下载列表中
     *
     * @param fileFullPath 文件全路径，可以是单个路径，也可以按分隔符分割路径
     * @param split 多个文件的分隔符
     * @return 原路径
     */
    public static String addToDownloadableList(String fileFullPath, String split) {
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);

        String[] pathArray = fileFullPath.split(split);

        // 根据配置设置 redis 缓存
        long expireTime = lihuaConfig.getTemporaryTokenExpireTime();
        for (String path : pathArray) {
            String key = SysBaseEnum.TEMPORARY_TOKEN_REDIS_PREFIX + path;
            if (expireTime == 0) {
                redisCache.setCacheObject(key, "");
            } else if (expireTime > 0) {
                redisCache.setCacheObject(key, "", expireTime, TimeUnit.MINUTES);
            }
        }

        return fileFullPath;
    }

    /**
     * 验证是否允许下载请求的文件
     * @param fileFullPath 文件全路径
     */
    public static void isDownloadable(String fileFullPath) {
        isDownloadable(fileFullPath, ",");
    }

    /**
     * 验证是否允许下载请求的文件
     *
     * @param fileFullPath 文件全路径
     * @param split 多个文件下的分隔符
     * @throws FileException 如果下载许可已过期
     */
    public static void isDownloadable(String fileFullPath, String split) {
        // 传入文件路径为空时，抛出文件不存在异常
        if (!StringUtils.hasText(fileFullPath)) {
            throw new FileException(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR);
        }

        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);

        if (lihuaConfig.getTemporaryTokenExpireTime() == -1) {
            return;
        }

        String[] pathArray = fileFullPath.split(split);

        for (String path : pathArray) {
            // 获取 redis 中的路径
            Object cacheObject = redisCache.getCacheObject(SysBaseEnum.TEMPORARY_TOKEN_REDIS_PREFIX + path);
            // 缓存取出的内容为空时，抛出 token 失效异常
            if (cacheObject == null) {
                throw new FileException(ResultCodeEnum.ACCESS_EXPIRED_ERROR);
            }
        }

        // 删除本次下载许可
        if (lihuaConfig.getTemporaryTokenExpireTime() == 0) {
            for (String path : pathArray) {
                redisCache.delete(SysBaseEnum.TEMPORARY_TOKEN_REDIS_PREFIX + path);
            }
        }
    }


}
