package com.lihua.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.lihua.enums.AxiosResultCodeEnum;
import lombok.AllArgsConstructor;

/**
 * 封装返回统一响应数据
 * @param <T>
 */
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Wrap<T> {

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
     * @param resultCodeEnum
     * @param msg
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Wrap wrap(AxiosResultCodeEnum resultCodeEnum, String msg, T data) {
        return new Wrap<T>(resultCodeEnum.getCode(), msg, data);
    }
}
