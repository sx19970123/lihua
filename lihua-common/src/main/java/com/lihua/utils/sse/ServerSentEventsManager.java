package com.lihua.utils.sse;

import com.lihua.config.LihuaConfig;
import com.lihua.model.sse.ServerSentEventsResult;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServerSentEventsManager {

    private static LihuaConfig lihuaConfig;

    private final static Map<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();

    /**
     * 创建连接
     */
    public static SseEmitter create(String userId) {

        if (!SSE_CACHE.containsKey(userId)) {
            // 实例化 SseEmitter 
            SseEmitter sseEmitter = getSseEmitter(userId);
            // 缓存 SseEmitter
            SSE_CACHE.put(userId, sseEmitter);
            log.info("Server-Sent Events：用户：{} 连接成功，当前连接总数为：{}", userId, SSE_CACHE.size());
            return sseEmitter;
        }

        return SSE_CACHE.get(userId);
    }

    /**
     * 关闭连接
     */
    public static void close() {
        String userId = LoginUserContext.getUserId();
        if (SSE_CACHE.containsKey(userId)) {
            SSE_CACHE.get(userId).complete();
            SSE_CACHE.remove(userId);
            log.info("Server-Sent Events：用户：{} 由客户端关闭连接，当前连接总数为：{}", userId, SSE_CACHE.size());
        }
    }

    /**
     * 发送消息
     */
    public static <T> void send(String userId, ServerSentEventsResult<T> serverSentEventsResult) {
        if (SSE_CACHE.containsKey(userId)) {
            SseEmitter sseEmitter = SSE_CACHE.get(userId);
            try {
                sseEmitter.send(serverSentEventsResult);
            } catch (Exception e) {
                SSE_CACHE.remove(userId);
            }
        }
    }

    /**
     * 发送消息
     */
    public static <T> void send(List<String> userIds, ServerSentEventsResult<T> serverSentEventsResult) {
        userIds.forEach(userId -> {
            send(userId, serverSentEventsResult);
        });
    }

    /**
     * 创建 SseEmitter
     * @param userId
     * @return
     */
    private static SseEmitter getSseEmitter(String userId) {
        lihuaConfig();
        SseEmitter sseEmitter = new SseEmitter(lihuaConfig.getSseExpireTime());

        // 指定 SseEmitter 生命周期，完成/超时时释放cache
        sseEmitter.onCompletion(() -> {
            SSE_CACHE.remove(userId);
            log.info("Server-Sent Events：用户：{} 断开连接，当前连接总数为：{}", userId, SSE_CACHE.size());
        });
        sseEmitter.onTimeout(() -> {
            SSE_CACHE.remove(userId);
            log.info("Server-Sent Events：用户：{} 连接超时，当前连接总数为：{}", userId, SSE_CACHE.size());
        });
        return sseEmitter;
    }

    /**
     * 加载配置文件
     */
    private static void lihuaConfig() {
        if (lihuaConfig == null) {
            lihuaConfig = SpringUtils.getBean(LihuaConfig.class);
        }
    }
}
