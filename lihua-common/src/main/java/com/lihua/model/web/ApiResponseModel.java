package com.lihua.model.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 封装返回统一响应数据
 * @param <T>
 */
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApiResponseModel<T> {
    /**
     * code 编码
     */
    @Schema(name = "code", description = "api请求正常默认值为200")
    private Integer code;

    /**
     * msg 消息
     */
    @Schema(name = "msg", description = "api请求正常默认值为成功")
    private String msg;

    /**
     * data 对象
     */
    @Schema(name = "data", description = "api请求响应对象")
    private T data;
}
