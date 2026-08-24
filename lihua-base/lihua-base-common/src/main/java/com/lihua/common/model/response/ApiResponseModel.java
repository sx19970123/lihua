package com.lihua.common.model.response;

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
public class ApiResponseModel<T> {
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
