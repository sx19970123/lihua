package com.lihua.model.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.lihua.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;

/**
 * 封装返回统一响应数据
 * @param <T>
 */
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ControllerResponse<T> {

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

    /**
     * 将 状态码、状态信息、响应数据 进行封装返回
     */
    public static <T> ControllerResponse wrap(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return new ControllerResponse<>(resultCodeEnum.getCode(), msg, data);
    }
}
