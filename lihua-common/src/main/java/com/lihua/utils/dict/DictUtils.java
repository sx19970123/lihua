package com.lihua.utils.dict;

import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.mapper.CommonMapper;
import com.lihua.model.dict.SysDictDataVO;
import com.lihua.utils.spring.SpringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典工具类
 */
public class DictUtils {

    private static final CommonMapper commonMapper;

    private static final RedisCache redisCache;

    static {
        redisCache = SpringUtils.getBean(RedisCache.class);
        commonMapper = SpringUtils.getBean(CommonMapper.class);
    }

    /**
     * 根据字典 value 和 字典type_code 获取字典label
     */
    public static String getLabel(String dictTypeCode,String value) {
        List<SysDictDataVO> dictDataList = getDictData(dictTypeCode);
        if (dictDataList.isEmpty()) {
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
        redisCache.setCacheList(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode, dictValue);
    }


    /**
     * 删除字典缓存
     */
    public static void removeDictCache(String dictTypeCode) {
        redisCache.delete(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode);
    }

    /**
     * 获取字典缓存数据
     */
    public static List<SysDictDataVO> getDictData(String dictTypeCode) {
        List<SysDictDataVO> dictCache = redisCache.getCacheList(SysBaseEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode, SysDictDataVO.class);
        // 缓存数据为空时，尝试从数据库再次获取，数据库未查询到数据时，返回空集合
        // 查询到数据时，再次调用自身返回字典数据
        if (dictCache == null) {
            int i = resetCacheDict(dictTypeCode);
            if (i == 0) {
                return new ArrayList<>();
            }
            return getDictData(dictTypeCode);
        }

        return dictCache;
    }

    /**
     * 重新缓存字典
     */
    public static int resetCacheDict(String dictTypeCode) {
        // 删除缓存
        removeDictCache(dictTypeCode);
        // 查询数据添加缓存
        List<SysDictDataVO> sysDictDataVOList = commonMapper.queryByDictTypeCode(dictTypeCode);
        if (!sysDictDataVOList.isEmpty()) {
            DictUtils.setDictCache(dictTypeCode,sysDictDataVOList);
        }
        return sysDictDataVOList.size();
    }


}
