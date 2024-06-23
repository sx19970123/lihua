package com.lihua.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface SysFileService {

    /**
     * 用户头像上传
     * @param file
     * @return
     */
    String uploadAvatar(MultipartFile file);

    /**
     * 多文件上传
     * @param files
     * @return
     */
    String upload(MultipartFile[] files);

    /**
     * 单文件下载
     * @param fileName
     */
    void download(String fileName);

    /**
     * 多文件下载
     * @param fileNames
     */
    void download(List<String> fileNames);

    /**
     * 图片预览，直接返回二进制文件
     * @param fileName
     */
    File imagePreview(String fileName);

    /**
     * 图片预览，返回链接
     * @param fileName
     * @return
     */
    String imagePreviewByPath(String fileName);
}
