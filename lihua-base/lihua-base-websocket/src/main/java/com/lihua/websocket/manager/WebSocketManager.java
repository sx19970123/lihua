package com.lihua.websocket.manager;

import com.lihua.common.utils.date.DateUtils;
import com.lihua.common.utils.json.JsonUtils;
import com.lihua.websocket.model.WebSocketResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketManager extends TextWebSocketHandler {

    // 连接实例集合，外层key为userId，内层key为user_clientId_clientType
    private final Map<String, Map<String, WebSocketSession>> sessionMap = new ConcurrentHashMap<>();

    // 单条消息允许的最长发送时间与发送缓冲上限，超限的慢消费端连接将被自动断开（缓冲上限对齐 WebSocketConfig 的消息 buffer 配置）
    private static final int SEND_TIME_LIMIT_MS = 5 * 1000;
    private static final int BUFFER_SIZE_LIMIT = 512 * 1024;

    /**
     * 向指定用户群发消息
     */
    @Async
    @SneakyThrows
    public <T> void send(List<String> userIdList, WebSocketResult<T> result) {
        String json = toJson(result);
        userIdList.forEach(userId -> send(userId, json));
        log.info("WebSocket 向指定用户消息发送完成");
    }

    /**
     * 向某一用户发送消息
     */
    @Async
    @SneakyThrows
    public <T> void send(String userId, WebSocketResult<T> result) {
        send(userId, toJson(result));
    }

    /**
     * 向所有用户发送消息
     */
    @Async
    @SneakyThrows
    public <T> void send(WebSocketResult<T> result) {
        String json = toJson(result);
        sessionMap
                .values()
                .stream()
                .flatMap(innerMap -> innerMap.values().stream())
                .forEach(session -> {
                    try {
                        session.sendMessage(new TextMessage(json));
                    } catch (IOException e) {
                        log.error("WebSocket消息发送失败: sessionKey={}，异常信息 {}", getSessionKey(session.getAttributes()), e.getMessage(), e);
                    }
                });
    }

    /**
     * 接收消息
     * 目前框架没有此业务需要
     * 可自行使用Spring Event 或 消息队列向业务分发
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("WebSocket收到消息: {}", message.getPayload());
    }

    /**
     * 建立连接
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        String userId = attributes.get("userId").toString();
        String sessionKey = getSessionKey(attributes);

        // 原子性操作，外层userId，内层sessionKey
        sessionMap.compute(userId, (uid, map) -> {
            if (map == null) {
                map = new ConcurrentHashMap<>();
            }
            // 存储前装饰为线程安全的 session：并发写在装饰器内排队缓冲，
            // 发送超时或缓冲超限的慢消费端连接由装饰器自动断开，避免阻塞推送线程
            WebSocketSession decoratedSession = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT_MS, BUFFER_SIZE_LIMIT);
            WebSocketSession oldSession = map.put(sessionKey, decoratedSession);
            if (oldSession != null && oldSession.isOpen()) {
                try {
                    oldSession.close();
                } catch (IOException e) {
                    log.error("WebSocket旧session关闭异常 {}" ,e.getMessage(), e);
                }
            }
            return map;
        });

        log.info("WebSocket连接建立成功: userId={}, sessionKey={}", userId, sessionKey);
    }

    /**
     * 关闭连接
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        Map<String, Object> attributes = session.getAttributes();
        String userId = attributes.get("userId").toString();
        String sessionKey = getSessionKey(attributes);

        // 原子性清理：断连移除与并发的新连接写入竞争时，分两步"先 remove 再判空移除 userId"
        // 会把新写入的 session 一并丢弃，导致该连接在断开重连前收不到任何推送
        sessionMap.compute(userId, (uid, map) -> {
            if (map == null) {
                return null;
            }
            map.remove(sessionKey);
            return map.isEmpty() ? null : map;
        });

        log.info("WebSocket连接断开成功: userId={}, sessionKey={}", userId, sessionKey);
    }

    /**
     * 发送消息
     */
    private void send(String userId, String msg) {
        Map<String, WebSocketSession> map = sessionMap.get(userId);
        if (map != null) {
            // 根据userId群发消息
            map.values().forEach(webSocketSession -> {
                try {
                    if (webSocketSession.isOpen()) {
                        webSocketSession.sendMessage(new TextMessage(msg));
                    }
                } catch (IOException e) {
                    log.error("WebSocket消息发送失败: userId={}, sessionKey={}，异常信息 {}", userId, getSessionKey(webSocketSession.getAttributes()), e.getMessage(), e);
                }
            });
        }
    }

    /**
     * 获取sessionKey
     */
    private String getSessionKey(Map<String, Object> attributes) {
        String userId = String.valueOf(attributes.get("userId"));
        String clientId = String.valueOf(attributes.get("clientId"));
        String clientType = String.valueOf(attributes.get("clientType"));
        return userId + "_" + clientId + "_" + clientType;
    }

    /**
     * websocket对象转为json
     */
    private <T> String toJson(WebSocketResult<T> result) {
        result.setTimestamp(DateUtils.nowTimeStamp());
        return JsonUtils.toJson(result);
    }
}
