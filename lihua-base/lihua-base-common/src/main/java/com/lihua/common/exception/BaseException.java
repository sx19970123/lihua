package com.lihua.common.exception;

import com.lihua.common.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 基础异常，业务中自定义异常需要继承此异常
 * web模块会进行统一捕获并响应到客户端
 */
@Getter
public class BaseException extends RuntimeException {

    /**
     * 异常枚举
     */
    private final ResultCodeEnum resultCodeEnum;

    /**
     * 抛出异常时附加的数据
     */
    private final Object data;

    public BaseException(ResultCodeEnum resultCodeEnum, Object data) {
        super(resultCodeEnum.getDefaultMsg());
        this.resultCodeEnum = resultCodeEnum;
        this.data = data;
    }

    public BaseException(ResultCodeEnum resultCodeEnum, String message, Object data) {
        super(message);
        this.resultCodeEnum = resultCodeEnum;
        this.data = data;
    }
}
