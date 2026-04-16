package com.lihua.common.model.response.response;


import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.BaseResponseController;
import org.springframework.util.StringUtils;

import static com.lihua.common.enums.ResultCodeEnum.SUCCESS;

/**
 * 统一返回 ApiResponse<T>
 * 需要 swagger 接口显示详细返回信息时使用
 * 使用时controller中定义返回值为ApiResponse<T>，并注明泛型
 */
public class ApiResponse extends BaseResponseController {
    public static <T> ApiResponseModel<T> success() {
        return success(SUCCESS.getDefaultMsg(), null);
    }

    public static <T> ApiResponseModel<T> success(ResultCodeEnum resultCodeEnum, T data) {
        return response(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }

    public static <T> ApiResponseModel<T> success(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return response(resultCodeEnum, msg, data);
    }

    public static <T> ApiResponseModel<T> success(T data) {
        return success(SUCCESS.getDefaultMsg(), data);
    }

    public static <T> ApiResponseModel<T> success(String msg, T data) {
        return response(SUCCESS, msg, data);
    }

    public static <T> ApiResponseModel<T> error(ResultCodeEnum resultCodeEnum, String message) {
        return error(resultCodeEnum, StringUtils.hasText(message) ? message : resultCodeEnum.getDefaultMsg(), null);
    }

    public static <T> ApiResponseModel<T> error(ResultCodeEnum resultCodeEnum) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), null);
    }

    public static <T> ApiResponseModel<T> error(ResultCodeEnum resultCodeEnum, T data) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }

    public static <T> ApiResponseModel<T> error(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return response(resultCodeEnum, msg, data);
    }
}
