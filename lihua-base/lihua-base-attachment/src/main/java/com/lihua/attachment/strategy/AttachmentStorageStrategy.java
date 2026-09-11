package com.lihua.attachment.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 不同附件存储方式策略接口
 */
public interface AttachmentStorageStrategy {

    /**
     * 附件上传
     * @param file 附件
     * @param fullFilePath 附件上传全路径
     */
    void uploadFile(MultipartFile file, String fullFilePath);

    /**
     * 通过路径判断附件是否存在
     * @param fullFilePath 附件全路径
     * @return 附件是否存在
     */
    boolean isExists(String fullFilePath);

    /**
     * 获取分片上传 uploadId
     * @param fullFilePath 附件全路径
     * @return uploadId
     */
    String getUploadId(String fullFilePath);

    /**
     * 获取已上传的分片 PartNumber
     * @param fullFilePath 附件全路径
     * @param uploadId 分片上传id
     * @return 已上传的PartNumber
     */
    List<Integer> getUploadedChunksIndex(String fullFilePath, String uploadId);

    /**
     * 分片附件上传
     * @param file 附件
     * @param fullFilePath 附件全路径
     * @param index 附件分片索引（PartNumber，从1开始）
     * @param uploadId 附件上传id
     */
    void chunksUploadFile(MultipartFile file, String fullFilePath, Integer index, String uploadId);

    /**
     * 分片附件合并
     * @param fullFilePath 生成的附件全路径
     * @param uploadId 前端生成uploadId
     * @param md5 附件md5，用于附件校验比对
     * @param total 总分片数量
     */
    void chunksMerge(String fullFilePath, String md5, String uploadId, Integer total);

    /**
     * 清理分片上传的临时资源（放弃合并或复用已有同 md5 文件跳过合并时调用）
     * <p>幂等：资源不存在时静默返回；清理失败不抛错（仅记录日志，残留由存储平台生命周期回收）
     * @param fullFilePath 分片上传的目标全路径
     * @param uploadId 分片上传id
     */
    void cleanChunks(String fullFilePath, String uploadId);

    /**
     * 删除附件
     * @param fullFilePath 附件全路径
     */
    void delete(String fullFilePath);

    /**
     * 获取下载地址（针对限时链接）
     * @param fullFilePath 附件全路径
     * @param expiryInMinutes 过期时间（分钟）
     * @return 下载地址
     */
    String getDownloadURL(String fullFilePath, String originName, int expiryInMinutes);

    /**
     * 通过路径进行附件下载
     * @param fullFilePath 附件路径
     * @return 下载的附件流
     */
    InputStream download(String fullFilePath);
}
