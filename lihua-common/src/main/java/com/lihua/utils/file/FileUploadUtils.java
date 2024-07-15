package com.lihua.utils.file;

import com.lihua.config.LihuaConfig;
import com.lihua.exception.FileException;
import com.lihua.utils.spring.SpringUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *  文件上传工具类
 */
public class FileUploadUtils {

    private static LihuaConfig lihuaConfig;

    /**
     * 单文件上传
     * @param file
     * @return
     */
    public static String upload(MultipartFile file) {
        return upload(file, false);
    }

    public static String upload(MultipartFile file, boolean isEditor) {
        String fullFilePath = handleFullFilePath(file.getOriginalFilename(), file.getContentType(), isEditor);
        try {
            InputStream inputStream = file.getInputStream();
            return upload(inputStream, fullFilePath);
        } catch (IOException e) {
            throw new FileException("文件上传异常");
        }
    }

    /**
     * 多文件上传
     * @param files
     * @return
     */
    public static List<String> upload(MultipartFile[] files) {
        List<String> fileFullPathList = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            fileFullPathList.add(upload(file));
        }
        return fileFullPathList;
    }

    public static List<String> upload(MultipartFile[] files, boolean isEditor) {
        List<String> fileFullPathList = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            fileFullPathList.add(upload(file, isEditor));
        }
        return fileFullPathList;
    }


    private static String upload(InputStream inputStream, String fullFilePath) {
        File file = new File(fullFilePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new FileException("上传文件失败" + e.getMessage());
        }

        return file.getAbsolutePath();
    }

    // 处理返回文件全路径名称
    private static String handleFullFilePath(String fileName, String contentType, boolean isEditor) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // file name 不存在，通过 contentType 生成对应格式文件
        if (StringUtils.hasText(fileName)) {
            int lastIndex = fileName.lastIndexOf(".");

            if (lastIndex == -1) {
                fileName = uuid + "." + Objects.requireNonNull(contentType).split("/")[1];
            } else {
                String fileType = fileName.substring(lastIndex);
                String subFileName = fileName.substring(0, lastIndex);
                fileName = uuid + "_" + subFileName + "_" + fileType;
            }

        } else {
            fileName = uuid + "." + Objects.requireNonNull(contentType).split("/")[1];
        }

        if (lihuaConfig == null) {
            lihuaConfig = SpringUtils.getBean(LihuaConfig.class);
        }

        // 由富文本上传文件时，路径添加 editor
        if (isEditor) {
            return lihuaConfig.getUploadFilePath() + "editor/editor_" + fileName;
        }

        return lihuaConfig.getUploadFilePath() + fileName;
    }

}
