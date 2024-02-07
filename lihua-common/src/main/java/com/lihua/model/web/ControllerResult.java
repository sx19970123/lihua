package com.lihua.model.web;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.utils.json.JsonUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.http.HttpResponse;

import static com.lihua.enums.ResultCodeEnum.SUCCESS;

public class ControllerResult {

    @SneakyThrows
    public static void fileSuccess(HttpServletResponse response, byte[] buffer) {
        response.setContentType("image/jpeg");
        response.getOutputStream().write(buffer);
    }
    
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
