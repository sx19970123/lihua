package com.lihua.ws.receive;

/**
 * 上行消息的回写通道：向当前 WebSocket 连接回写同 type 帧（结构与下行一致：{type, data, timestamp}），
 * 由连接层在分发时构造、捕获当前 session——实现方不接触 session/序列化细节。
 * 连接已断时回写失败由连接层兜底记日志，实现方无需处理
 */
@FunctionalInterface
public interface WsReply {

    /**
     * 向当前连接回写数据（帧 type 与上行帧一致）
     */
    void send(Object data);
}
