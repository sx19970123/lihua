package com.lihua.model.web.basecontroller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.attachment.AttachmentStreamAndInfoModel;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.utils.file.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 统一返回 Controller 基础类
 */
public class BaseResponseController {

    protected static <T> ApiResponseModel<T> response(ResultCodeEnum resultCodeEnum, String msg, T data) {
        return ApiResponseModel.<T>builder().code(resultCodeEnum.getCode()).msg(msg).data(data).build();
    }

    public static ResponseEntity<StreamingResponseBody> success(File file) {
        return FileUtils.download(file, null, false);
    }

    public static ResponseEntity<StreamingResponseBody> success(File file, String fileName) {
        return FileUtils.download(file, fileName, false);
    }

    public static ResponseEntity<StreamingResponseBody> success(File file, String fileName, boolean autoDelete) {
        return FileUtils.download(file, fileName, autoDelete);
    }

    public static ResponseEntity<StreamingResponseBody> success(List<AttachmentStreamAndInfoModel> fileList) {
        return FileUtils.download(fileList);
    }

    public static ResponseEntity<StreamingResponseBody> success(InputStream inputStream, String fileName, String size){
        return FileUtils.download(inputStream, fileName, size);
    }

    public static ResponseEntity<StreamingResponseBody> success(InputStream inputStream, String fileName){
        return FileUtils.download(inputStream, fileName);
    }
}
