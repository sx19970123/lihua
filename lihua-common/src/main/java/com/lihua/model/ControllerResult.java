package com.lihua.model;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.utils.json.JsonUtils;
import static com.lihua.enums.ResultCodeEnum.SUCCESS;

public class ControllerResult {
    
    public static <T> String success() {
        return success(null);
    }

    public static <T> String success(T data) {
        return success(SUCCESS.getDefaultMsg(), data);
    }

    public static <T> String success(String msg, T data) {
        return response(SUCCESS, msg, data);
    }

    public static <T> String error(ResultCodeEnum resultCodeEnum, String message) {
        return error(resultCodeEnum, message, null);
    }

    public static <T> String error(ResultCodeEnum resultCodeEnum) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), null);
    }
    
    public static <T> String error(ResultCodeEnum resultCodeEnum, T data) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }
    
    public static <T> String error(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return response(resultCodeEnum, msg, data);
    }

    private static <T> String response(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return JsonUtils.toJson(ControllerResponse.wrap(resultCodeEnum, msg, data));
    }
}
