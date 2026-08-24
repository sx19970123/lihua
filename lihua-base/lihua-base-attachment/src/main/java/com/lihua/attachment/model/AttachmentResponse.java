package com.lihua.attachment.model;

import com.lihua.attachment.utils.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 附件统一返回 Controller 基础类
 */
public class AttachmentResponse {
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
