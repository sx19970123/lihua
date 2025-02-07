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
     * @return 文件上传完成后保存到数据库业务表的返回值，一般是存储路径
     */
    String uploadFile(MultipartFile file);

    /**
     * 通过网络链接下载文件
     * @param url 网络文件链接
     * @return 文件上传完成后保存到数据库业务表的返回值，一般是存储路径
     */
    String uploadFile(String url);

    /**
     * 通过路径判断文件是否存在
     * @param path 文件全路径
     * @return 文件是否存在
     */
    boolean isExists(String path);

    /**
     * 上传临时文件（分片上传）
     * @param file 附件片段
     * @param fullFilePath 附件临时路径即名称
     */
    void uploadTempFile(MultipartFile file, String fullFilePath);

    /**
     * 通过uploadId获取临时目录下已上传的文件索引
     * @param uploadId 前端生成uploadId
     * @return 对应已上传的
     */
    List<Integer> getTempChunksIndex(String uploadId);

    /**
     * 分片文件合并
     * @param uploadId 前端生成uploadId
     * @param fileName 文件名称，用于取后缀名
     * @param md5 文件md5，用于文件校验比对
     * @param total 总分片数量
     * @return 合并后的全路径
     */
    String chunksMerge(String fileName, String md5, String uploadId, Integer total);

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
     * 文件下载（针对永久有效链接，只针对公开 businessCode的附件使用）
     * @param path 文件路径
     * @return 下载的文件流
     */
    File download(String path);
}
