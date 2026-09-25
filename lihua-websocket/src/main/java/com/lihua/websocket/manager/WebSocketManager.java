package com.lihua.websocket.manager;

import com.lihua.common.utils.date.DateUtils;
import com.lihua.common.utils.json.JsonUtils;
import com.lihua.common.utils.string.StringUtils;
import com.lihua.websocket.model.WebSocketResult;
import com.lihua.ws.receive.WsClientMessage;
import com.lihua.ws.receive.WsMessageReceiver;
import com.lihua.ws.receive.WsReply;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketManager extends TextWebSocketHandler {

    // 连接实例集合，外层key为userId，内层key为user_clientId_clientType
    private final Map<String, Map<String, WebSocketSession>> sessionMap = new ConcurrentHashMap<>();

    // 上行消息处理器表（lihua-base-ws 的 WsMessageReceiver，type → 处理器；内置心跳处理器为二开参考实现）
    private final Map<String, WsMessageReceiver> receiverMap = new HashMap<>();

    // 单条消息允许的最长发送时间与发送缓冲上限，超限的慢消费端连接将被自动断开（缓冲上限对齐 WebSocketConfig 的消息 buffer 配置）
    private static final int SEND_TIME_LIMIT_MS = 5 * 1000;
    private static final int BUFFER_SIZE_LIMIT = 512 * 1024;

    // 会话表存装饰后的 session；handleTextMessage 回调持有原始 session，
    // 上行回写须经同一装饰实例发送（与下行推送共享排队锁，规避底层 session 并发写）
    private static final String DECORATED_SESSION = "decoratedSession";

    @Resource
    private List<WsMessageReceiver> wsMessageReceivers;

    /**
     * 注册上行消息处理器（type 重复时保留先注册者并告警）
     */
    @PostConstruct
    public void initReceivers() {
        wsMessageReceivers.forEach(receiver -> {
            WsMessageReceiver old = receiverMap.putIfAbsent(receiver.type(), receiver);
            if (old != null) {
                log.warn("WebSocket上行处理器 type={} 重复注册，保留先注册者: {}", receiver.type(), old.getClass().getName());
            }
        });
    }

    /**
     * 发送消息：userIdList 为 null 时全员广播，否则向指定用户的全部连接推送
     * （仅订阅端 WsPushSubscriber 消费，null 语义与 WsPushMessage 贯通；业务投递入口在 lihua-base-ws 的 WebSocketPushUtils。
     * 序列化一次、单次异步任务内完成批量，勿在调用侧循环本方法）
     */
    @Async
    @SneakyThrows
    public <T> void send(List<String> userIdList, WebSocketResult<T> result) {
        String json = toJson(result);
        if (userIdList == null) {
            // 全员广播
            sessionMap.values().stream()
                    .flatMap(innerMap -> innerMap.values().stream())
                    .forEach(session -> {
                        try {
                            session.sendMessage(new TextMessage(json));
                        } catch (IOException e) {
                            log.error("WebSocket消息发送失败: sessionKey={}，异常信息 {}", getSessionKey(session.getAttributes()), e.getMessage(), e);
                        }
                    });
        } else {
            userIdList.forEach(userId -> send(userId, json));
        }
        log.info("WebSocket 消息发送完成: 范围={}", userIdList == null ? "全员" : "定向 " + userIdList.size() + " 人");
    }

    /**
     * 接收消息（上行）：解析帧后按 type 分发给 lihua-base-ws 的 {@link WsMessageReceiver}
     * （内置心跳处理器为二开参考实现，扩展指引见其 javadoc）；
     * 解析失败/无处理方静默降级为 debug 日志——上行是低频控制面，非业务数据面
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        WsClientMessage frame;
        try {
            frame = JsonUtils.toObject(message.getPayload(), WsClientMessage.class);
        } catch (Exception e) {
            log.debug("WebSocket上行帧解析失败: {}", e.getMessage());
            return;
        }
        if (frame == null || !StringUtils.hasText(frame.getType())) {
            return;
        }
        WsMessageReceiver receiver = receiverMap.get(frame.getType());
        if (receiver == null) {
            log.debug("WebSocket上行帧无处理方: type={}", frame.getType());
            return;
        }
        String userId = String.valueOf(session.getAttributes().get("userId"));
        try {
            receiver.receive(userId, frame.getData(), replyTo(session, frame.getType()));
        } catch (Exception e) {
            log.error("WebSocket上行消息处理失败: type={}, userId={}, 异常信息 {}", frame.getType(), userId, e.getMessage(), e);
        }
    }

    /**
     * 构造回写通道：向当前连接回写同 type 帧（{type, data, timestamp}，与下行帧结构一致）
     */
    private WsReply replyTo(WebSocketSession session, String type) {
        return data -> {
            try {
                // 经装饰实例发送：与下行推送共享排队锁，避免底层 session 并发写
                WebSocketSession out = (WebSocketSession) session.getAttributes().get(DECORATED_SESSION);
                if (out == null) {
                    out = session;
                }
                Map<String, Object> frame = new LinkedHashMap<>();
                frame.put("type", type);
                frame.put("data", data);
                frame.put("timestamp", DateUtils.nowTimeStamp());
                out.sendMessage(new TextMessage(JsonUtils.toJson(frame)));
            } catch (IOException e) {
                log.error("WebSocket上行回写失败: type={}, sessionKey={}，异常信息 {}", type, getSessionKey(session.getAttributes()), e.getMessage(), e);
            }
        };
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
            // 发送超时或缓冲超限的慢消费端连接由装饰器自动断开，避免阻塞推送线程；
            // 同时放入 attributes，供上行回写（handleTextMessage 持原始 session）取用同一实例
            WebSocketSession decoratedSession = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT_MS, BUFFER_SIZE_LIMIT);
            attributes.put(DECORATED_SESSION, decoratedSession);
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
