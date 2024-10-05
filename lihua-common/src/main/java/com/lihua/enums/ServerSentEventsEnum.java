package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * sse 消息发送类型枚举
 * 帮助了解发送的消息类型
 */
@Getter
@AllArgsConstructor
public enum ServerSentEventsEnum implements Serializable {
    /**
     * 通知
     */
    SSE_NOTICE,
    /**
     * 保持心跳
     */
    SSE_HEART_BEAT
}
