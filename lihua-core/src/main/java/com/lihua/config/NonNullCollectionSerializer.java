package com.lihua.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Collection;

/**
 * 自定义序列化器，将集合中对象null值过滤
 */
public class NonNullCollectionSerializer extends JsonSerializer<Collection> {
    @Override
    public void serialize(Collection value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (Object element : value) {
            if (element != null) {
                gen.writeObject(element);
            }
        }
        gen.writeEndArray();
    }
}
