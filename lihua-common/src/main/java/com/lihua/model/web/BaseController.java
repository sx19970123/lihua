package com.lihua.model.web;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.utils.json.JsonUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;

import static com.lihua.enums.ResultCodeEnum.SUCCESS;

public class BaseController extends BaseFileController {


    public static void imageFileSuccess(HttpServletResponse response, byte[] buffer) throws IOException {
        fileSuccess(response,buffer,"image/jpeg");
    }


    public static void fileSuccess(HttpServletResponse response, byte[] buffer,String contentType) throws IOException {
        response.setContentType(contentType);
        try (OutputStream out = response.getOutputStream()) {
            out.write(buffer);
        }
    }

    public static String success() {
        return success(SUCCESS.getDefaultMsg(), null);
    }

    public static <T> String success(T data) {
        return success(SUCCESS.getDefaultMsg(), data);
    }

    public static <T> String success(String msg, T data) {
        return response(SUCCESS, msg, data);
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
        return response(resultCodeEnum, msg, data);
    }

    private static <T> String response(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return JsonUtils.toJson(ControllerResponse.wrap(resultCodeEnum, msg, data));
    }
}
