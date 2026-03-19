package com.lihua.dict.utils;

import com.lihua.common.utils.spring.SpringUtils;
import com.lihua.dict.mapper.DictDataMapper;
import com.lihua.dict.model.DictDataModel;
import com.lihua.redis.cache.RedisCache;
import com.lihua.redis.enums.RedisKeyPrefixEnum;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典工具类
 */
public class DictUtils {

    private static final DictDataMapper DICT_DATA_MAPPER = SpringUtils.getBean(DictDataMapper.class);

    private static final RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 根据字典 value 和 字典type_code 获取字典label
     */
    public static String getLabel(String dictTypeCode, String value) {
        List<DictDataModel> dictDataList = getDictData(dictTypeCode);
        if (dictDataList.isEmpty()) {
            return null;
        }

        for (DictDataModel dictData : dictDataList) {
            if (dictData.getValue().equals(value)) {
                return dictData.getLabel();
            }
        }

        return null;
    }

    /**
     * 根据字典 label 和 字典type_code 获取字典value
     */
    public static String getValue(String dictTypeCode, String label) {
        List<DictDataModel> dictDataList = getDictData(dictTypeCode);
        if (dictDataList.isEmpty()) {
            return null;
        }

        for (DictDataModel dictData : dictDataList) {
            if (dictData.getLabel().equals(label)) {
                return dictData.getValue();
            }
        }

        return null;
    }

    /**
     * 设置字典缓存
     */
    public static void setDictCache(String dictTypeCode, List<DictDataModel> dictValue) {
        redisCache.setCacheList(RedisKeyPrefixEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode, dictValue);
    }


    /**
     * 删除字典缓存
     */
    public static void removeDictCache(String dictTypeCode) {
        redisCache.delete(RedisKeyPrefixEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode);
    }

    /**
     * 获取字典缓存数据
     */
    public static List<DictDataModel> getDictData(String dictTypeCode) {
        List<DictDataModel> dictCache = redisCache.getCacheList(RedisKeyPrefixEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode, DictDataModel.class);
        // 缓存数据为空时，尝试从数据库再次获取，数据库未查询到数据时，返回空集合
        // 查询到数据时，再次调用自身返回字典数据
        if (dictCache == null || dictCache.isEmpty()) {
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
     * @return 查询到的字典数量
     */
    public static int resetCacheDict(String dictTypeCode) {
        if (!StringUtils.hasText(dictTypeCode)) {
            return 0;
        }
        return resetCacheDict(Collections.singletonList(dictTypeCode));
    }

    /**
     * 重新缓存字典
     */
    public static int resetCacheDict(List<String> dictTypeCodeList) {

        if (dictTypeCodeList == null || dictTypeCodeList.isEmpty()) {
            return 0;
        }

        // 查询数据添加缓存
        List<DictDataModel> dictDataModelVOList = DICT_DATA_MAPPER.queryByDictTypeCode(dictTypeCodeList);

        // 删除缓存
        dictTypeCodeList.forEach(DictUtils::removeDictCache);

        // 根据编码分组
        Map<String, List<DictDataModel>> groupByCode = dictDataModelVOList.stream().collect(Collectors.groupingBy(DictDataModel::getDictTypeCode));

        groupByCode.forEach((dictTypeCode, dictDataVOList) -> {
            // 设置缓存
            if (!dictDataVOList.isEmpty()) {
                DictUtils.setDictCache(dictTypeCode, dictDataVOList);
            }
        });

        // 查询到的总数
        return dictDataModelVOList.size();
    }


}
