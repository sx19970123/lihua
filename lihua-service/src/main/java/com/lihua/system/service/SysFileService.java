package com.lihua.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysFileService {

    /**
     * 文件单上传
     * @param file
     * @return
     */
    String upload(MultipartFile file);

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
     * 图片预览
     * @param fileName
     */
    byte[] imagePreview(String fileName);
}
