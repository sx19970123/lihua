package com.lihua.common.model.response.basecontroller;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.utils.json.JsonUtils;
import lombok.SneakyThrows;

/**
 * 统一返回 Controller 基础类
 */
public class BaseResponseController {

    @SneakyThrows
    protected static <T> String responseToJson(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return JsonUtils.toJsonIgnoreNulls(response(resultCodeEnum, msg, data));
    }

    protected static <T> ApiResponseModel<T> response(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return ApiResponseModel.<T>builder().code(resultCodeEnum.getCode()).msg(msg).data(data).build();
    }
}
