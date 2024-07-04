package com.lihua.utils.dict;

import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.model.dict.SysDictDataVO;
import com.lihua.utils.spring.SpringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典工具类
 */
public class DictUtils {

    private static final String DICT_DATA_REDIS_PREFIX = SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue();

    /**
     * 根据字典 value 和 字典type_code 获取字典label
     */
    public static String getLabel(String dictTypeCode,String value) {
        List<SysDictDataVO> dictDataList = getDictData(dictTypeCode);
        if (dictDataList == null || dictDataList.isEmpty()) {
            return null;
        }

        for (SysDictDataVO dictData : dictDataList) {
            if (dictData.getValue().equals(value)) {
                return dictData.getLabel();
            }
        }

        return null;
    }

    /**
     * 设置字典缓存
     */
    public static <T> void setDictCache(String dictTypeCode, List<SysDictDataVO> dictValue) {
        RedisCache redisCache = initRedis();
        redisCache.setCacheObject(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode, dictValue);
    }


    /**
     * 删除字典缓存
     */
    public static void removeDictCache(String dictTypeCode) {
        RedisCache redisCache = initRedis();
        redisCache.delete(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode);
    }

    /**
     * 获取字典缓存数据
     */
    public static List<SysDictDataVO> getDictData(String dictTypeCode) {
        RedisCache redisCache = initRedis();
        Object dictCache = redisCache.getCacheObject(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode);
        if (dictCache == null) {
            return new ArrayList<>();
        }
        return (List<SysDictDataVO>) dictCache;
    }

    /**
     * 加载redis缓存
     */
    private static RedisCache initRedis() {
        return  SpringUtils.getBean(RedisCache.class);
    }
}
