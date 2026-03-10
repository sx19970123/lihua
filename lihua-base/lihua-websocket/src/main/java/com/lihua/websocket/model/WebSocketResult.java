package com.lihua.websocket.model;
import com.lihua.websocket.enums.WebSocketMsgTypeEnum;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class WebSocketResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 类型枚举
    private final WebSocketMsgTypeEnum type;

    // 发送数据data
    private final T data;

    // 时间戳
    private Long timestamp;
}
