package com.lihua.common.model.response.response;
import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.basecontroller.BaseResponseController;
import org.springframework.util.StringUtils;

import static com.lihua.common.enums.ResultCodeEnum.SUCCESS;

/**
 * 统一返回 Json
 * 不需要 swagger 接口显示详细返回信息时使用
 * 使用时controller中直接返回String即可
 */
public class StrResponse extends BaseResponseController {

    public static String success() {
        return success(SUCCESS.getDefaultMsg(), null);
    }

    public static <T> String success(ResultCodeEnum resultCodeEnum, T data) {
        return responseToJson(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }

    public static <T> String success(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return responseToJson(resultCodeEnum, msg, data);
    }

    public static <T> String success(T data) {
        return success(SUCCESS.getDefaultMsg(), data);
    }

    public static <T> String success(String msg, T data) {
        return responseToJson(SUCCESS, msg, data);
    }

    public static String error(ResultCodeEnum resultCodeEnum, String message) {
        return error(resultCodeEnum, StringUtils.hasText(message) ? message : resultCodeEnum.getDefaultMsg(), null);
    }

    public static String error(ResultCodeEnum resultCodeEnum) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), null);
    }

    public static <T> String error(ResultCodeEnum resultCodeEnum, T data) {
        return error(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }

    public static <T> String error(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return responseToJson(resultCodeEnum, msg, data);
    }
}
