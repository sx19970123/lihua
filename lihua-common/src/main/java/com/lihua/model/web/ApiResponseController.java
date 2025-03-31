package com.lihua.model.web;

import com.lihua.enums.ResultCodeEnum;
import org.springframework.util.StringUtils;

import static com.lihua.enums.ResultCodeEnum.SUCCESS;

/**
 * 统一返回 ApiResponse<T>
 * 需要 swagger 接口显示详细返回信息时使用
 * 使用时controller中定义返回值为ApiResponse<T>，并注明泛型
 */
public class ApiResponseController extends BaseResponseController {

    public static <T> ApiResponse<T> success() {
        return success(SUCCESS.getDefaultMsg(), null);
    }

    public static <T> ApiResponse<T> success(ResultCodeEnum resultCodeEnum, T data) {
        return response(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }

    public static <T> ApiResponse<T> success(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return response(resultCodeEnum, msg, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(SUCCESS.getDefaultMsg(), data);
    }

    public static <T> ApiResponse<T> success(String msg, T data) {
        return response(SUCCESS, msg, data);
    }

    public static <T> ApiResponse<T> error(ResultCodeEnum resultCodeEnum, String message) {
        return error(resultCodeEnum, StringUtils.hasText(message) ? message : resultCodeEnum.getDefaultMsg(), null);
    }

    public static <T> ApiResponse<T> error(ResultCodeEnum resultCodeEnum) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), null);
    }

    public static <T> ApiResponse<T> error(ResultCodeEnum resultCodeEnum, T data) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }

    public static <T> ApiResponse<T> error(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return response(resultCodeEnum, msg, data);
    }
}
