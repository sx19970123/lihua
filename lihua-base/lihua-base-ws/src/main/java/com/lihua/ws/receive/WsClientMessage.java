package com.lihua.ws.receive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 客户端上行帧模型（客户端→服务端，结构与下行帧对称）：连接层解析后按 {@link #type} 分发给
 * {@link WsMessageReceiver}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WsClientMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息类型（内置值见 WebSocketMsgTypeEnum；二开可自定义新值，配套实现 WsMessageReceiver）
     */
    private String type;

    /**
     * 载荷
     */
    private Object data;

    /**
     * 时间戳（客户端生成，服务端不消费）
     */
    private Long timestamp;
}
