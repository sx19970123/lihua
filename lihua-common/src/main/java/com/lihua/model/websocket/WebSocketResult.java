package com.lihua.model.websocket;
import com.lihua.enums.WebSocketMsgTypeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class WebSocketResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 类型枚举
    private WebSocketMsgTypeEnum type;

    // 发送数据data
    private T data;

    // 时间戳
    private Long timestamp;
}
