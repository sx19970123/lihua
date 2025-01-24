package com.lihua.model.web;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.utils.file.FileUtils;
import com.lihua.utils.json.JsonUtils;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.List;

import static com.lihua.enums.ResultCodeEnum.SUCCESS;

public class BaseController {

    public static String success() {
        return success(SUCCESS.getDefaultMsg(), null);
    }

    public static <T> String success(ResultCodeEnum resultCodeEnum, T data) {
        return response(resultCodeEnum, resultCodeEnum.getDefaultMsg(), data);
    }

    public static <T> String success(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return response(resultCodeEnum, msg, data);
    }

    public static <T> String success(T data) {
        return success(SUCCESS.getDefaultMsg(), data);
    }

    public static <T> String success(String msg, T data) {
        return response(SUCCESS, msg, data);
    }

    public static ResponseEntity<StreamingResponseBody> success(File file) {
        return FileUtils.download(file);
    }

    public static ResponseEntity<StreamingResponseBody> success(List<File> fileList) {
        return FileUtils.download(fileList);
    }

    public static ResponseEntity<StreamingResponseBody> success(InputStream inputStream, String fileName) throws UnsupportedEncodingException {
        return FileUtils.download(inputStream, fileName);
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

    @SneakyThrows
    private static <T> String response(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return JsonUtils.toJson(ControllerResponse.wrap(resultCodeEnum, msg, data));
    }
}
