package com.lihua.model.sse;

import com.lihua.enums.ServerSentEventsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 标准化sse发送数据格式
 * serverSentEventsEnum 表示功能模块
 * data 为发送的数据
 * @param <T>
 */

@Data
@AllArgsConstructor
public class ServerSentEventsResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 类型枚举
    private ServerSentEventsEnum type;

    // 发送数据data
    private T data;

}
