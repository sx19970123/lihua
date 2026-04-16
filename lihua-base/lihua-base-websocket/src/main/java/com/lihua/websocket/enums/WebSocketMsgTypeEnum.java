package com.lihua.websocket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * ws 消息发送类型枚举
 * 帮助了解发送的消息类型
 */
@Getter
@AllArgsConstructor
public enum WebSocketMsgTypeEnum implements Serializable {
    /**
     * 通知
     */
    WS_NOTICE,

    /**
     * 心跳，客户端向服务器发送
     */
    WS_HEARTBEAT
}
