package com.lihua.common.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * 简单的json 工具类，
 * 通过调用 Spring 容器中的 jackson 进行 json 和对象的相互转换
 *
 */
@Slf4j
public class JsonUtils {

    // 无特殊配置的jsonMapper
    private static final JsonMapper jsonMapper = SpringUtils.getBean(JsonMapper.class);

    // 序列化仅排除 null 值的 jsonMapper（自建实例，不含 Spring 容器对 JsonMapper 的定制配置）
    private static final JsonMapper excludeNullWriter = JsonMapper.builder()
            .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
            .build();

    /**
     *  对象转为 JSON
     *  需注意，在进行转换时，被转换对象应提供 get 方法，
     *  无 get 方法时请使用 @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY) 注解
     * @param data 待转为 json 的对象
     * @return json 数据
     */
    public static <T> String toJson(T data) {
        return jsonMapper.writeValueAsString(data);
    }

    /**
     * 对象转为json并忽略null值
     */
    public static <T> String toJsonIgnoreNulls(T data) {
        return excludeNullWriter.writeValueAsString(data);
    }

    /**
     * 对象转为json，无法转换的对象将返回全限定类名
     * 对象中的null值忽略
     * @param data 待转为 json 的对象
     * @return json字符串或全限定类名
     */
    public static <T> String toJsonOrCanonicalName(T data) {
        try {
            return toJsonIgnoreNulls(data);
        } catch (Exception e) {
            String canonicalName = data.getClass().getCanonicalName();
            log.error("此对象无法转换为Json数据，返回全限定类名：{}，error message：{}", canonicalName, e.getMessage());
            return canonicalName;
        }
    }

    /**
     * 排除 json 字符串中指定的 key
     * @param json json字符串
     * @param excludeKeys 要排除的 key 集合
     */
    public static String excludeJsonKey(String json, List<String> excludeKeys) {
        if (excludeKeys == null || excludeKeys.isEmpty()) {
            return json;
        }
        if (json == null || json.isEmpty()) {
            return json;
        }
        try {
            JsonNode jsonNode = jsonMapper.readTree(json);
            removeKeyRecursively(jsonNode, excludeKeys);
            return jsonMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            // 非 json 字符串（如 toJsonOrCanonicalName 降级返回的全限定类名）无键可排除，原样返回
            log.debug("排除 json key 时解析失败，原样返回：{}", e.getMessage());
            return json;
        }
    }

    /**
     * json 转为对象
     * @param json json字符串
     * @param clazz 指定对象
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return jsonMapper.readValue(json, clazz);
    }

    /**
     * 校验字符串是否为合法 json（不合法时抛出异常，由全局异常处理统一提示）
     */
    public static void validateJson(String json) {
        jsonMapper.readTree(json);
    }

    /**
     * 对象深拷贝（单个 POJO 或 List/Set/Map 容器）
     * 容器按逐元素拷贝——元素类型取自各元素自身，天然支持嵌套容器与混合元素多态，
     * 重建为标准实现：List→ArrayList、Set→LinkedHashSet、Map→LinkedHashMap；
     * TreeMap/TreeSet 的 comparator 与不可变包装类不保真，有此需求请自行处理；
     * Map 的 key 约定不可变，复用引用仅深拷 value
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T item) {
        if (item == null) {
            return null;
        }
        // 不可变标量与枚举无需拷贝
        if (item instanceof String || item instanceof Number || item instanceof Boolean
                || item instanceof Character || item instanceof Enum<?> || item instanceof Temporal) {
            return item;
        }
        if (item instanceof List<?> list) {
            List<Object> copy = new ArrayList<>(list.size());
            list.forEach(element -> copy.add(deepCopy(element)));
            return (T) copy;
        }
        if (item instanceof Set<?> set) {
            Set<Object> copy = new LinkedHashSet<>(set.size());
            set.forEach(element -> copy.add(deepCopy(element)));
            return (T) copy;
        }
        if (item instanceof Map<?, ?> map) {
            Map<Object, Object> copy = new LinkedHashMap<>(map.size());
            map.forEach((key, value) -> copy.put(key, deepCopy(value)));
            return (T) copy;
        }
        try {
            return jsonMapper.readValue(jsonMapper.writeValueAsString(item), (Class<T>) item.getClass());
        } catch (Exception e) {
            throw new ServiceException("深拷贝执行异常：" + e.getMessage());
        }
    }

    // 递归方法，遍历整个 JSON 结构并移除指定的键
    private static void removeKeyRecursively(JsonNode node, List<String> excludeKeys) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            // 移除指定的 key
            excludeKeys.forEach(objectNode::remove);
            // 递归处理子节点
            objectNode.forEach(entry -> removeKeyRecursively(entry, excludeKeys));
        } else if (node.isArray()) {
            for (JsonNode item : node) {
                removeKeyRecursively(item, excludeKeys);
            }
        }
    }
}
