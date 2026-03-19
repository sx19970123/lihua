package com.lihua.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.codec.TypedJsonJacksonCodec;

/**
 * Redisson 序列化器，在yml文件中指定，通过反射调用
 */
public class TypedRedissonJsonCodec extends TypedJsonJacksonCodec {

    /**
     * 继承TypedJsonJacksonCodec，序列化时移除 @class 标记
     */
    public TypedRedissonJsonCodec() {
        super(Object.class, create());
    }

    /**
     * 自定义序列化配置
     */
    private static ObjectMapper create() {

        // 序列化设置
        ObjectMapper objectMapper = new ObjectMapper();
        // redis存入对象时过滤null值
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
        // 支持java8的时间日期
        objectMapper.registerModule(new JavaTimeModule());
        // 处理LocalDateTime序列化和反序列化的特殊格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 对私有字段也进行序列化
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        return objectMapper;
    }
}
