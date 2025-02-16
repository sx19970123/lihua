package com.lihua.utils.file;

import com.lihua.exception.FileException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class UrlFileUtils {

    /**
     * url附件上传
     * @param url 附件链接
     */
    public static String upload(String url) {
        // 判断url是否合法
        if (!StringUtils.hasText(url)) {
            throw new FileException("附件URL不存在");
        }
        MultipartFile multipartFile = urlToMultipartFile(url);
        return FileUtils.upload(multipartFile);
    }

    /**
     * 将 URL转为 MultipartFile
     * @param url 附件链接
     * @return MultipartFile对象
     */
    public static MultipartFile urlToMultipartFile(String url) {
        URL uri;
        try {
            uri = new URL(url);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            throw new FileException("读取URL失败");
        }

        String contentType;
        int size;
        // 通过读取InputStream获取附件size和contentType
        try (InputStream inputStream = uri.openStream()) {
            contentType = uri.openConnection().getContentType();
            size = inputStream.available();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("读取URL失败");
        }

        final URL finalUrl = uri;

        // 创建 MultipartFile 对象
        return new MultipartFile() {
            @NotNull
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return Paths.get(finalUrl.getPath()).getFileName().toString();
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return size == 0;
            }

            @Override
            public long getSize() {
                return size;
            }

            @NotNull
            @Override
            public byte[] getBytes() throws IOException {
                try (InputStream inputStream = finalUrl.openStream()) {
                    return inputStream.readAllBytes();
                }
            }

            @NotNull
            @Override
            public InputStream getInputStream() throws IOException {
                return finalUrl.openStream();
            }

            @Override
            public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
                try (InputStream inputStream = finalUrl.openStream()) {
                    Files.copy(inputStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        };
    }
}
