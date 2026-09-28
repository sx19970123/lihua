package com.lihua.cache.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis 发布订阅模式topic
 */
@Getter
@AllArgsConstructor
public enum RedisTopicEnum {
    /**
     * 清除本地缓存
     */
    INVALIDATE_LOCAL_CACHE("invalidate_local_cache"),

    /**
     * WebSocket 推送扇出：业务方投递一条，所有持有 WS 连接的实例各收一次，
     * 各自推送本地会话（多实例天然扇出）
     */
    WS_PUSH("ws_push");

    private final String value;
}
