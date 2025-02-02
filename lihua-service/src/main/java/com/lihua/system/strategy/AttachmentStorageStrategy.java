package com.lihua.system.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 不同附件存储方式策略接口
 */
public interface AttachmentStorageStrategy {

    /**
     * 文件上传
     * @param file 文件
     * @param businessCode 业务编码
     * @return 文件上传完成后保存到数据库业务表的返回值，一般是存储路径
     */
    String uploadFile(MultipartFile file, String businessCode);

    /**
     * 通过网络链接下载文件
     * @param url 网络文件链接
     * @param businessCode 业务编码
     * @return 文件上传完成后保存到数据库业务表的返回值，一般是存储路径
     */
    String uploadFile(String url, String businessCode);

    /**
     * 上传临时文件（分片上传）
     * @param file 附件片段
     * @param fullFilePath 附件临时路径即名称
     */
    void uploadTempFile(MultipartFile file, String fullFilePath);

    /**
     * 通过md5获取临时目录下已上传的文件索引
     * @param md5 附件md5编码
     * @return 对应已上传的
     */
    List<Integer> getTempChunksIndex(String md5);

    /**
     * 删除文件
     * @param path 文件路径
     */
    void delete(String path);

    /**
     * 获取下载地址（针对限时链接）
     * @param path 文件路径
     * @param expiryInMinutes 过期时间（分钟）
     * @return 下载地址
     */
    String getDownloadURL(String path, long expiryInMinutes);

    /**
     * 文件下载（针对永久有效链接，只针对公开文件夹下的文件）
     * @param path 文件路径
     * @param originalName 原文件名
     * @return 下载的文件流
     */
    File download(String path, String originalName);
}
