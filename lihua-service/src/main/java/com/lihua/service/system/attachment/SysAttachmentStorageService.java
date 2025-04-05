package com.lihua.service.system.attachment;

import com.lihua.entity.system.SysAttachment;
import com.lihua.model.system.vo.SysAttachmentChunkVO;
import com.lihua.model.system.vo.SysAttachmentUrlVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

public interface SysAttachmentStorageService {

    /**
     * 附件是否存在（md5和原附件名均相同）
     * @return 是否存在
     */
    boolean existsAttachmentByMd5(String md5, String originFileName);

    /**
     * 根据路径查询附件信息，用于附件组件数据回显
     * @param ids 附件id集合
     * @return 对应的附件信息
     */
    List<SysAttachment> queryAttachmentInfoByIds(List<String> ids);

    /**
     * 上传附件
     * @param file 文件
     * @param sysAttachment 附件对象
     * @return 附件表id
     */
    String uploadAttachment(MultipartFile file, SysAttachment sysAttachment);

    /**
     * 附件上传（批量）
     * @param files 文件数组
     * @param attachmentList 附件对象集合
     * @return 附件id集合
     */
    List<String> batchUploadAttachment(MultipartFile[] files, List<SysAttachment> attachmentList);

    /**
     * 附件上传（URL）
     * @param sysAttachment 附件对象
     * @return 原URL和附件表ID对象
     */
    SysAttachmentUrlVO urlUploadAttachment(SysAttachment sysAttachment);

    /**
     * 附件秒传
     * @return 附件id
     */
    String fastUpload(SysAttachment sysAttachment);

    /**
     * 分片上传开始
     * @return 分片上传唯一uploadId和附件表id对象
     */
    SysAttachmentChunkVO chunksUploadAttachmentStart(SysAttachment sysAttachment);

    /**
     * 通过 uploadId值获取已上传分片附件的索引值
     * @param uploadId 附件上传id
     * @return 已上传的附件索引集合
     */
    List<Integer> chunksUploadedIndex(String uploadId);

    /**
     * 分片上传
     * @param file 分片附件
     * @param uploadId 前端生成uploadId
     * @param index 附件索引
     */
    void chunksUpload(MultipartFile file, String uploadId, String index);

    /**
     * 分片合并
     * @param sysAttachment 附件对象
     * @param total 分片总数
     */
    String chunksMerge(SysAttachment sysAttachment, Integer total);

    /**
     * 业务删除附件（仅做状态的修改）
     */
    void deleteFromBusiness(String id);

    /**
     * 根据路径删除附件
     * @param fullFilePathList 附件全路径集合
     */
    void deleteFiles(List<String> fullFilePathList);

    /**
     * 获取附件url
     * @param path 附件路径
     * @param originalName 原文件名
     * @param expireTime 过期时间，为 null 则取配置附件指定时间
     * @return 下载链接
     */
    String getAttachmentURL(String path,String originalName, Integer expireTime);

    /**
     * 本地附件下载（仅LOCAL模式下使用，其他存储方式中直接通过附件服务器获取）
     * @param key 附件路径及过期时间密文
     * @return 附件
     */
    ResponseEntity<StreamingResponseBody> localDownload(String key, String originName);

    /**
     * 公开附件下载
     * @param id 附件id
     * @return 附件
     */
    ResponseEntity<StreamingResponseBody> publicDownload(String id, String fileName);

    /**
     * 附件导出下载
     * @param path 导出附件路径
     * @param fileName 附件名称
     * @return 附件
     */
    ResponseEntity<StreamingResponseBody> exportDownload(String path, String fileName);
}
