package com.lihua.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * ws 消息发送类型枚举
 * 帮助了解发送的消息类型
 * <p>
 * 定义在 base-common：推送投递方（base-security/lihua-system 等）与订阅消费方（base-websocket）
 * 共同依赖，经 Redis pub/sub 传递时以 {@link #name()} 字符串序列化
 */
@Getter
@AllArgsConstructor
public enum WebSocketMsgTypeEnum implements Serializable {
    /**
     * 通知
     */
    WS_NOTICE,

    /**
     * 心跳，客户端每 30s 向服务器发送（data="ping"），
     * 服务端经 lihua-base-ws 的 HeartbeatWsMessageReceiver 处理并回 pong
     */
    WS_HEARTBEAT,

    /**
     * 权限数据更新提示：角色/菜单变更后定向推送给受影响在线用户，
     * 前端置「数据更新」红点（web），App 端提示重新登录生效；红点事实源在 Redis 标记
     */
    WS_REFRESH_PERMISSION
}
