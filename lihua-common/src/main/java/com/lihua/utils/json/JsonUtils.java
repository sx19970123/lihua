package com.lihua.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lihua.utils.spring.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 * 简单的json 工具类，
 * 通过调用 Spring 容器中的 jackson 进行 json 和对象的相互转换
 *
 */
@Slf4j
public class JsonUtils {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper =  SpringUtils.getBean(ObjectMapper.class);
    }

    /**
     *  对象转为 JSON
     *  需注意，在进行转换时，被转换对象应提供 get 方法，
     *  无 get 方法时请使用 @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY) 注解
     * @param data 待转为 json 的对象
     * @return json 数据
     */
    public static <T> String toJson(T data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
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
            log.error(e.getMessage(), e);
            return data.getClass().getCanonicalName();
        }
    }

    /**
     * 对象转为json并忽略null值
     */
    public static <T> String toJsonIgnoreNulls(T data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // 配置忽略 null 值
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(data);
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
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return json;
        }
        removeKeyRecursively(jsonNode, excludeKeys);

        return toJsonOrCanonicalName(jsonNode);
    }

    /**
     * json 转为对象
     * @param json json字符串
     * @param clazz 指定对象
     */
    @SneakyThrows
    public static <T> T toObject(String json, Class<T> clazz) {
        return objectMapper.readValue(json,clazz);
    }

    /**
     * 判断字符串是否为json
     */
    public static void isJson(String json) throws JsonProcessingException {
        objectMapper.readTree(json);
    }

    // 递归方法，遍历整个 JSON 结构并移除指定的键
    private static void removeKeyRecursively(JsonNode node, List<String> excludeKeys) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            objectNode.remove(excludeKeys);

            // 递归处理对象中的子节点
            objectNode.fields().forEachRemaining(entry -> removeKeyRecursively(entry.getValue(), excludeKeys));
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;

            // 递归处理数组中的每个元素
            for (JsonNode item : arrayNode) {
                removeKeyRecursively(item, excludeKeys);
            }
        }
    }
}
