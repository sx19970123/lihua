package com.lihua.cache.publisher;

import jakarta.annotation.Resource;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * Redis pub/sub 发布器
 * <p>
 * 消息一律传 JSON 字符串——全局 codec（TypedJsonJacksonCodec）无类型信息，
 * POJO 直发时订阅端解码退化为 Map 且 cast 失败被 Redisson 吞掉（回调不执行）；
 * 复杂载荷由调用方自行序列化（如 WS 推送的 JsonUtils.toJson(WsPushMessage)）
 */
@Component
public class RedisPublisher {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 发布消息
     * @param topic 主题
     * @param msg 消息（JSON 字符串）
     */
    public void send(String topic, String msg) {
        RTopic rTopic = redissonClient.getTopic(topic);
        rTopic.publish(msg);
    }
}
