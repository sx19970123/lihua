package com.lihua.common.model.response;


import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.ResponseModel;
import org.springframework.util.StringUtils;

import static com.lihua.common.enums.ResultCodeEnum.SUCCESS;

/**
 * 统一返回 ResponseModel<T> 的唯一响应工厂
 * controller 返回值与围栏层（filter/interceptor/Security handler）响应均使用
 */
public class Responses {
    public static <T> ResponseModel<T> success() {
        return success(SUCCESS.getDefaultMsg(), null);
    }

    public static <T> ResponseModel<T> success(ResultCodeEnum resultCodeEnum, T data) {
        return response(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }

    public static <T> ResponseModel<T> success(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return response(resultCodeEnum, msg, data);
    }

    public static <T> ResponseModel<T> success(T data) {
        return success(SUCCESS.getDefaultMsg(), data);
    }

    public static <T> ResponseModel<T> success(String msg, T data) {
        return response(SUCCESS, msg, data);
    }

    public static <T> ResponseModel<T> error(ResultCodeEnum resultCodeEnum, String message) {
        return error(resultCodeEnum, StringUtils.hasText(message) ? message : resultCodeEnum.getDefaultMsg(), null);
    }

    public static <T> ResponseModel<T> error(ResultCodeEnum resultCodeEnum) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), null);
    }

    public static <T> ResponseModel<T> error(ResultCodeEnum resultCodeEnum, T data) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }

    public static <T> ResponseModel<T> error(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return response(resultCodeEnum, msg, data);
    }

    private static <T> ResponseModel<T> response(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return new ResponseModel<>(resultCodeEnum.getCode(), msg, data);
    }
}
