package com.lihua.dict.utils;

import com.lihua.cache.enums.RedisTopicEnum;
import com.lihua.cache.manager.LocalCacheManager;
import com.lihua.cache.publisher.RedisPublisher;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.spring.SpringUtils;
import com.lihua.dict.loader.DictDataLoader;
import com.lihua.dict.model.DictDataModel;
import com.lihua.cache.manager.RedisCacheManager;
import com.lihua.cache.enums.RedisKeyPrefixEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;
import tools.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典工具类
 */
@Slf4j
public class DictUtils {

    // 字典回源通道：接口属于字典域，实现由持表方提供（system 本地 / api-system RPC）；未注册即显式失败
    private static final ObjectProvider<DictDataLoader> DICT_DATA_LOADER_PROVIDER =
            SpringUtils.getApplicationContext().getBeanProvider(DictDataLoader.class);

    private static final RedisCacheManager REDIS_CACHE_MANAGER = SpringUtils.getBean(RedisCacheManager.class);

    private static final LocalCacheManager LOCAL_CACHE_MANAGER = SpringUtils.getBean(LocalCacheManager.class);

    private static final RedisPublisher REDIS_PUBLISHER = SpringUtils.getBean(RedisPublisher.class);

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
     * 获取字典缓存数据
     * Redis 空结果视为未命中（防新部署场景 DB 有数据而 Redis 未预热时被空结果阻断回源）；
     * 查库也无数据时以空列表做本地负缓存，避免无数据字典每格回源 Redis/DB
     */
    public static List<DictDataModel> getDictData(String dictTypeCode) {
        String cacheKey = RedisKeyPrefixEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode;
        // 获取本地缓存（Redis 空集合按未命中处理）
        List<DictDataModel> dictCache = LOCAL_CACHE_MANAGER.getWithFallback(cacheKey, new TypeReference<>(){}, () -> {
            List<DictDataModel> redisList = REDIS_CACHE_MANAGER.getCacheList(cacheKey, DictDataModel.class);
            return redisList == null || redisList.isEmpty() ? null : redisList;
        });

        if (dictCache != null) {
            return dictCache;
        }

        // 本地未命中时查库重建缓存；查到数据时再次调用自身返回字典数据
        // （回源通道缺失时显式失败——空列表只允许表示「数据源确实无数据」）
        int i = resetCacheDict(dictTypeCode);
        if (i == 0) {
            // 查无数据：空列表负缓存（本地 TTL 内不再回源；字典数据变更经 resetCacheDict 的失效解除负缓存）
            LOCAL_CACHE_MANAGER.setCache(cacheKey, new ArrayList<>());
            return new ArrayList<>();
        }
        return getDictData(dictTypeCode);
    }

    /**
     * 重新缓存字典（管理侧主动刷新通道：未注册回源通道时显式失败，不与「查无数据」混淆）
     * @return 查询到的字典数量
     */
    public static int resetCacheDict(String dictTypeCode) {
        if (!StringUtils.hasText(dictTypeCode)) {
            return 0;
        }
        return resetCacheDict(Collections.singletonList(dictTypeCode));
    }

    /**
     * 重新缓存字典（管理侧主动刷新通道：未注册回源通道时显式失败）
     */
    public static int resetCacheDict(List<String> dictTypeCodeList) {
        if (dictTypeCodeList == null || dictTypeCodeList.isEmpty()) {
            return 0;
        }
        return doResetCacheDict(requireLoader(), dictTypeCodeList);
    }

    /**
     * 回源通道缺失即显式失败：getDictData/resetCacheDict 的调用方要的是数据源真实状态，
     * 静默返回空会把「无通道」伪装成「查无数据」（负缓存空列表只在真实查过库后成立）
     */
    private static DictDataLoader requireLoader() {
        DictDataLoader loader = DICT_DATA_LOADER_PROVIDER.getIfAvailable();
        if (loader == null) {
            throw new ServiceException("当前服务未注册字典回源通道（DictDataLoader），无法获取字典数据");
        }
        return loader;
    }

    private static int doResetCacheDict(DictDataLoader loader, List<String> dictTypeCodeList) {
        // 查询数据添加缓存
        List<DictDataModel> dictDataModelVOList = loader.queryByDictTypeCode(dictTypeCodeList);

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

    // 缓存写操作仅限回源/刷新内部使用（sys_dict_data 的数据 Owner 是 system，管理侧统一走 resetCacheDict 刷新通道）
    private static void setDictCache(String dictTypeCode, List<DictDataModel> dictValue) {
        String key = RedisKeyPrefixEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode;
        REDIS_PUBLISHER.send(RedisTopicEnum.INVALIDATE_LOCAL_CACHE.getValue(), key);
        REDIS_CACHE_MANAGER.setCacheList(key, dictValue);
    }

    private static void removeDictCache(String dictTypeCode) {
        String key = RedisKeyPrefixEnum.DICT_DATA_REDIS_PREFIX.getValue() + dictTypeCode;
        REDIS_PUBLISHER.send(RedisTopicEnum.INVALIDATE_LOCAL_CACHE.getValue(), key);
        REDIS_CACHE_MANAGER.delete(key);
    }
}
