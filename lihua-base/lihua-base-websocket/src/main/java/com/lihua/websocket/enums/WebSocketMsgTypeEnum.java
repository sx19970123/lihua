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
    WS_HEARTBEAT,

    /**
     * 权限数据更新提示：角色/菜单变更后定向推送给受影响在线用户，
     * 前端置「数据更新」红点（web），App 端提示重新登录生效；红点事实源在 Redis 标记
     */
    WS_REFRESH_PERMISSION
}
