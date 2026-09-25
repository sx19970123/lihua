package com.lihua.ws.receive;

/**
 * 上行消息处理器 SPI（二开扩展点）：连接层收到客户端帧后按 {@link #type()} 分发到对应实现，
 * 实现放 @Component 即被宿主扫描注册
 * <p>
 * <b>生效范围</b>：处理器运行在连接所在进程（引 lihua-websocket 的 JVM——mono 为 admin、cloud 为
 * lihua-ws）。业务服务（cloud 的 system 等）进程内注册的处理器收不到调用——跨服务上行业务处理
 * 须经 WS 上行 Redis topic 桥接（{@code com.lihua.ws.receive} 预留方向），勿用进程内事件
 * （event 不跨进程）。内置参考实现：{@link com.lihua.ws.receive.impl.HeartbeatWsMessageReceiver}
 * <p>
 * receive 内异常由连接层兜底捕获记日志，不影响连接存活；上行是低频控制面，处理器内勿做重活
 */
public interface WsMessageReceiver {

    /**
     * 声明处理的帧类型（内置值取 WebSocketMsgTypeEnum.name()，二开可自定义新值）
     */
    String type();

    /**
     * 处理上行消息
     *
     * @param userId 握手鉴权通过的用户 id
     * @param data   上行载荷
     * @param reply  向当前连接回写同 type 帧的通道
     */
    void receive(String userId, Object data, WsReply reply);
}
