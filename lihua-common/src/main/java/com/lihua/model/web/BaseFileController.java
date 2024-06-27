package com.lihua.model.web;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.FileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件下载相关操作，使用 StreamingResponseBody 避免将文件一次性全部加载到内存中
 * 而是以流的形式进行传输，避免内存被大文件所占用，而且可以提高传输效率
 */
@Slf4j
public class BaseFileController {

    public static ResponseEntity<StreamingResponseBody> success(File file) {
        if (file == null || !file.exists()) {
            throw new FileException(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR);
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return success(fileInputStream, file.getName());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new FileException(e.getMessage());
        }
    }

    public static ResponseEntity<StreamingResponseBody> success(List<File> fileList) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        try {
            // 将文件循环打包到ZIP输入流
            for (File file : fileList) {
                addToZipFile(file, zipOutputStream);
            }

            // 关闭 ZIP 输出流
            zipOutputStream.finish();
            zipOutputStream.close();

            // 创建输入流资源
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);

            return success(inputStreamResource.getInputStream(), "files.zip");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException(e.getMessage());
        }
    }


    public static ResponseEntity<StreamingResponseBody> success(InputStream inputStream, String fileName) throws UnsupportedEncodingException {
        if (!StringUtils.hasText(fileName)) {
            throw new FileException("请指定下载文件的名称");
        }
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        StreamingResponseBody stream = out -> {
          try {
              byte[] buffer = new byte[4096];
              int bytesRead;
              while ((bytesRead = inputStream.read(buffer)) != -1) {
                  out.write(buffer, 0, bytesRead);
              }
          } catch (IOException e) {
              log.error(e.getMessage(), e);
              throw new FileException(e.getMessage());
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=UTF-8''" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }

    /**
     * 将多个文件打包到zip流中
     * @param file 单个文件
     * @param zipOutputStream zip输出流
     * @throws IOException io异常
     */
    private static void addToZipFile(File file, ZipOutputStream zipOutputStream) throws IOException {
        if (file.exists()) {
            // 创建 ZIP 文件条目
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

            // 将文件内容写入 ZIP 文件条目中
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }

            // 关闭当前 ZIP 文件条目
            zipOutputStream.closeEntry();
            fileInputStream.close();
        } else {
            throw new FileException("文件不存在");
        }
    }
}
