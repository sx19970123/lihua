package com.lihua.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lihua.utils.spring.SpringUtils;
import lombok.SneakyThrows;

/**
 *
 * 简单的json 工具类，
 * 通过调用 Spring 容器中的 jackson 进行 json 和对象的相互转换
 *
 */
public class JsonUtils {
    private static ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);

    /**
     *  对象转为 JSON
     *  需注意，在进行转换时，被转换对象应提供 get 方法，
     *  无 get 方法时请使用 @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY) 注解
     * @param data
     * @return
     * @param <T>
     */
    @SneakyThrows
    public static <T> String toJson(T data) {
        return objectMapper.writeValueAsString(data);
    }

    /**
     * json转为任意集合
     * @param json
     * @param clazz
     * @return
     * @param <T>
     */
    @SneakyThrows
    public static <T> T toObject(String json, Class<T> clazz) {
        return objectMapper.readValue(json,clazz);
    }

    /**
     * 判断字符串是否为json
     * @param json
     */
    @SneakyThrows
    public static void isJson(String json) {
        objectMapper.readTree(json);
    }

}
