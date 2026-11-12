package com.lihua.common.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装返回统一响应数据
 * @param <T>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
// 序列化忽略 null 字段：对齐围栏层手写路径（JsonUtils.toJsonIgnoreNulls）与容器 non_null 配置，保证响应体各服务一致
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {
    /**
     * code 编码
     */
    private Integer code;

    /**
     * msg 消息
     */
    private String msg;

    /**
     * data 对象
     */
    private T data;
}
