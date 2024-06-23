package com.lihua.model.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

/**
 * 文件下载相关操作，使用 StreamingResponseBody 避免将文件一次性全部加载到内存中
 * 而是以流的形式进行传输，避免内存被大文件所占用，而且可以提高传输效率
 */
@Slf4j
public class BaseFileController {

    public static ResponseEntity<StreamingResponseBody> success(File file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        return success(fileInputStream, file.getName());
    }

    public static ResponseEntity<StreamingResponseBody> success(InputStream inputStream, String fileName) {
        StreamingResponseBody stream = out -> {
          try {
              byte[] buffer = new byte[4096];
              int bytesRead;
              while ((bytesRead = inputStream.read(buffer)) != -1) {
                  out.write(buffer, 0, bytesRead);
              }
          } catch (IOException e) {
              log.error(e.getMessage(), e);
          } finally {
              try {
                  inputStream.close();
              } catch (IOException e) {
                  log.error(e.getMessage(),e);
              }
          }
        };

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }
}
