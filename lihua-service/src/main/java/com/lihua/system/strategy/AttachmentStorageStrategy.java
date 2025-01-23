package com.lihua.system.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 不同附件存储方式策略接口
 */
public interface AttachmentStorageStrategy {

    /**
     * 文件上传
     * @param file 文件
     * @param isPublic 是是否为公开文件
     * @return 文件上传完成后保存到数据库业务表的返回值，一般是存储路径
     */
    String uploadFile(MultipartFile file, boolean isPublic);

    /**
     * 通过网络链接下载文件
     * @param url 网络文件链接
     * @param isPublic 是是否为公开文件
     * @return 文件上传完成后保存到数据库业务表的返回值，一般是存储路径
     */
    String uploadFile(String url, boolean isPublic);

    /**
     * 文件分片上传
     * @param file 单片文件
     * @param MD5 文件MD5值，用于标记是否为同一文件
     * @param index 每个分片的索引值
     * @param total 总分片数量
     * @return 片段上传完成返回索引
     */
    int uploadChunk(MultipartFile file, String MD5, int index, int total);

    /**
     * 文件合并
     * @param MD5 文件MD5值
     * @param total 总分片数量
     * @param isPublic 是是否为公开文件
     * @return 文件上传完成后保存到数据库业务表的返回值，一般是存储路径
     */
    String mergeChunks(String MD5, int total, boolean isPublic);

    /**
     * 删除文件
     * @param path 文件路径
     * @return 删除结果
     */
    boolean delete(String path);

    /**
     * 获取下载地址（针对限时链接）
     * @param path 文件路径
     * @param expiryInSeconds 过期时间（秒）
     * @return 下载地址
     */
    String getDownloadURL(String path, int expiryInSeconds);

    /**
     * 文件下载（针对永久有效链接，只针对公开文件夹下的文件）
     * @param path 文件路径
     * @return 下载的文件流
     */
    InputStream download(String path);
}
