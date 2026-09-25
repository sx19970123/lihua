package com.lihua.ws.receive.impl;

import com.lihua.common.enums.WebSocketMsgTypeEnum;
import com.lihua.ws.receive.WsMessageReceiver;
import com.lihua.ws.receive.WsReply;
import org.springframework.stereotype.Component;

/**
 * 心跳上行处理器（连接保活，二开上行处理器的参考实现）
 * <p>
 * 契约：客户端每 30s 发一跳（type=WS_HEARTBEAT，data="ping"），本处理器回 pong 确认链路双向可用。
 * 服务端不基于心跳做超时踢线——断连由容器连接回调驱动客户端自动重连（≤3 次）；
 * 该帧为连接层内置语义，在连接所在进程内闭环，不经 Redis 扇出
 * <p>
 * 二开新上行消息照此三步：①type 取新值（内置枚举或自定义字符串）②实现 {@link WsMessageReceiver}
 * 加 @Component ③在 receive 内消费 data / 经 reply 回写
 */
@Component
public class HeartbeatWsMessageReceiver implements WsMessageReceiver {

    @Override
    public String type() {
        return WebSocketMsgTypeEnum.WS_HEARTBEAT.name();
    }

    @Override
    public void receive(String userId, Object data, WsReply reply) {
        reply.send("pong");
    }
}
