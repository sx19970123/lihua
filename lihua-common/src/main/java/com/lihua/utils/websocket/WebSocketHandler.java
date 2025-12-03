package com.lihua.utils.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * 建立连接
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionKey = getSessionKey(session.getAttributes());
        sessionMap.put(sessionKey, session);
        log.info("websocket {} 连接建立成功，当前连接数为 {}", sessionKey, sessionMap.size());
    }

    /**
     * 接收消息
     * 目前框架没有此业务需要
     * 可自行使用Spring Event 或 消息队列向业务分发
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("收到消息: {}", message.getPayload());
    }

    /**
     * 关闭连接
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String sessionKey = getSessionKey(session.getAttributes());
        sessionMap.remove(sessionKey);
        log.info("websocket {} 连接断开成功，当前连接数为 {}", sessionKey, sessionMap.size());
    }

    /**
     * 获取sessionKey
     */
    private String getSessionKey(Map<String, Object> attributes) {
        String userId = attributes.get("userId").toString();
        String clientId = attributes.get("clientId").toString();
        String clientType = attributes.get("clientType").toString();
        return userId + "_" + clientId + "_" + clientType;
    }
}
