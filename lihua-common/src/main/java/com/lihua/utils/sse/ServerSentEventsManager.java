package com.lihua.utils.sse;

import com.lihua.config.LihuaConfig;
import com.lihua.model.sse.ServerSentEventsResult;
import com.lihua.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServerSentEventsManager {

    private final static LihuaConfig lihuaConfig;

    private static final Map<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();

    static {
        lihuaConfig = SpringUtils.getBean(LihuaConfig.class);
    }


    // 创建或获取 SseEmitter 实例
    public static SseEmitter create(String clientKey) {
        // clientKey 已连接情况下直接返回 SseEmitter， 否则创建新的连接
        return SSE_CACHE.computeIfAbsent(clientKey, (key) -> {
            SseEmitter sseEmitter = initializeSseEmitter(key);
            log.info("Server-Sent Events: 客户端：{} 连接成功，当前连接总数为：{}",
                    key, SSE_CACHE.size() + 1);
            return sseEmitter;
        });
    }

    // 关闭指定客户端的连接
    public static void close(String clientKey) {
        SseEmitter sseEmitter = SSE_CACHE.remove(clientKey);
        if (sseEmitter != null) {
            sseEmitter.complete();
            log.info("Server-Sent Events: 客户端：{} 由客户端发起关闭连接，当前连接总数为：{}",
                    clientKey, SSE_CACHE.size());
        }
    }

    // 向所有已连接的客户端发送消息
    public static <T> void send(ServerSentEventsResult<T> serverSentEventsResult) {
        SSE_CACHE.forEach((key, sseEmitter) -> sendMessage(sseEmitter, key, serverSentEventsResult));
    }

    // 向指定的用户列表发送消息
    public static <T> void send(List<String> userIds, ServerSentEventsResult<T> serverSentEventsResult) {
        userIds.forEach(userId -> SSE_CACHE.forEach((key, sseEmitter) -> {
            if (key.startsWith(userId)) {
                sendMessage(sseEmitter, key, serverSentEventsResult);
            }
        }));
    }

    // 向指定单个用户发送消息
    public static <T> void send(String userId, ServerSentEventsResult<T> serverSentEventsResult) {
        send(Collections.singletonList(userId), serverSentEventsResult);
    }

    // 发送消息的辅助方法，处理异常
    private static <T> void sendMessage(SseEmitter sseEmitter, String clientKey, ServerSentEventsResult<T> result) {
        try {
            sseEmitter.send(result);
        } catch (IOException e) {
            SSE_CACHE.remove(clientKey);
            log.error("Server-Sent Events: 向客户端：{} 发送消息失败", clientKey, e);
        }
    }

    // 初始化 SseEmitter 实例并设置生命周期事件
    private static SseEmitter initializeSseEmitter(String clientKey) {

        SseEmitter sseEmitter = new SseEmitter(lihuaConfig.getSseExpireTime());

        sseEmitter.onCompletion(() -> {
            SSE_CACHE.remove(clientKey);
            log.info("Server-Sent Events: 客户端：{} 断开连接，当前连接总数为：{}", clientKey, SSE_CACHE.size());
        });

        sseEmitter.onTimeout(() -> {
            SSE_CACHE.remove(clientKey);
            log.info("Server-Sent Events: 客户端：{} 连接超时，当前连接总数为：{}", clientKey, SSE_CACHE.size());
        });

        return sseEmitter;
    }
}
