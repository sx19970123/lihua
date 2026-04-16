package com.lihua.cache.publisher;

import jakarta.annotation.Resource;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class RedisPublisher {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 发布消息
     * @param topic 主题
     * @param msg 消息
     */
    public void send(String topic, String msg) {
        RTopic rTopic = redissonClient.getTopic(topic);
        rTopic.publish(msg);
    }

    /**
     * 发布消息
     * @param topic 主题
     * @param msg 消息
     */
    public <T> void send(String topic, T msg) {
        RTopic rTopic = redissonClient.getTopic(topic);
        rTopic.publish(msg);
    }
}
