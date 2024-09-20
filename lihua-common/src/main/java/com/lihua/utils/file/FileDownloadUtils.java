package com.lihua.utils.file;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.FileException;
import com.lihua.utils.spring.SpringUtils;
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
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 将文件路径添加到可下载列表
 * 通过文件路径判断该文件是否可下载
 */
@Slf4j
public class FileDownloadUtils {

    /**
     * 将单个文件路径添加到可下载列表中
     *
     * @param fileFullPath 文件全路径
     * @return 原路径
     */
    public static String addToDownloadableList(String fileFullPath) {
        return addToDownloadableList(fileFullPath, ",");
    }

    /**
     * 将多个文件路径添加到可下载列表中
     *
     * @param fileFullPathList 文件全路径列表
     * @return 原路径列表
     */
    public static List<String> addToDownloadableList(List<String> fileFullPathList) {
        return addToDownloadableList(fileFullPathList, ",");
    }

    /**
     * 将多个文件路径添加到可下载列表中
     *
     * @param fileFullPathList 文件全路径列表
     * @param split 分隔符
     * @return 原路径列表
     */
    public static List<String> addToDownloadableList(List<String> fileFullPathList, String split) {
        if (fileFullPathList == null || fileFullPathList.isEmpty()) {
            return fileFullPathList;
        }

        fileFullPathList.forEach(path -> addToDownloadableList(path, split));
        return fileFullPathList;
    }

    /**
     * 将文件路径添加到可下载列表中
     *
     * @param fileFullPath 文件全路径，可以是单个路径，也可以按分隔符分割路径
     * @param split 多个文件的分隔符
     * @return 原路径
     */
    public static String addToDownloadableList(String fileFullPath, String split) {
        RedisCache<String> redisCache = SpringUtils.getBean(RedisCache.class);
        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);

        String[] pathArray = fileFullPath.split(split);

        // 根据配置设置 redis 缓存
        long expireTime = lihuaConfig.getFileDownloadExpireTime();
        for (String path : pathArray) {
            String key = SysBaseEnum.TEMPORARY_TOKEN_REDIS_PREFIX + path;
            if (expireTime == 0) {
                redisCache.setCacheObject(key, "");
            } else if (expireTime > 0) {
                redisCache.setCacheObject(key, "", expireTime, TimeUnit.MINUTES);
            }
        }

        return fileFullPath;
    }

    /**
     * 验证是否允许下载请求的文件
     * @param fileFullPath 文件全路径
     */
    public static void isDownloadable(String fileFullPath) {
        isDownloadable(fileFullPath, ",");
    }

    /**
     * 验证是否允许下载请求的文件
     *
     * @param fileFullPath 文件全路径
     * @param split 多个文件下的分隔符
     * @throws FileException 如果下载许可已过期
     */
    public static void isDownloadable(String fileFullPath, String split) {
        // 传入文件路径为空时，抛出文件不存在异常
        if (!StringUtils.hasText(fileFullPath)) {
            throw new FileException(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR);
        }

        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);

        if (lihuaConfig.getFileDownloadExpireTime() == -1) {
            return;
        }

        String[] pathArray = fileFullPath.split(split);

        for (String path : pathArray) {
            // 获取 redis 中的路径
            Object cacheObject = redisCache.getCacheObject(SysBaseEnum.TEMPORARY_TOKEN_REDIS_PREFIX + path);
            // 缓存取出的内容为空时，抛出 token 失效异常
            if (cacheObject == null) {
                throw new FileException(ResultCodeEnum.ACCESS_EXPIRED_ERROR);
            }
        }
    }

    /**
     * 文件下载
     * @param file 文件
     * @return
     */
    public static ResponseEntity<StreamingResponseBody> download(File file) {
        if (file == null || !file.exists()) {
            throw new FileException(ResultCodeEnum.RESOURCE_NOT_FOUND_ERROR);
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return download(fileInputStream, file.getName());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new FileException(e.getMessage());
        }
    }

    /**
     * 文件打包下载
     * @param fileList
     * @return
     */
    public static ResponseEntity<StreamingResponseBody> download(List<File> fileList) {
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

            return download(inputStreamResource.getInputStream(), "files.zip");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException(e.getMessage());
        }
    }

    /**
     * 文件下载
     * @param inputStream
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ResponseEntity<StreamingResponseBody> download(InputStream inputStream, String fileName) throws UnsupportedEncodingException {
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
