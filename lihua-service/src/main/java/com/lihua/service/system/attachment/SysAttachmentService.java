package com.lihua.service.system.attachment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.entity.system.SysAttachment;
import com.lihua.model.system.dto.SysAttachmentDTO;
import com.lihua.model.system.vo.SysAttachmentVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.util.List;

public interface SysAttachmentService {

    /**
     * 分页查询附件
     */
    IPage<SysAttachment> queryPage(SysAttachmentDTO sysAttachmentDTO);

    /**
     * 根据主键查询附件详情
     */
    SysAttachmentVO queryById(String id);

    /**
     * 附件批量删除
     */
    void deleteByIds(List<String> ids);

    /**
     * 保存附件信息
     * @return 附件id
     */
    String saveAttachment(SysAttachment sysAttachment);

    /**
     * 批量保存附件信息
     * @return 附件id集合
     */
    List<String> batchSaveAttachment(List<SysAttachment> sysAttachmentList);

    /**
     * 附件是否存在（md5和原附件名均相同）
     * @return 是否存在
     */
    boolean existsAttachmentByMd5(String md5, String originFileName);

    /**
     * 附件秒传
     * @return 附件id
     */
    String fastUpload(SysAttachment sysAttachment);

    /**
     * 根据路径查询附件信息，用于附件组件数据回显
     * @param ids 附件id集合
     * @return 对应的附件信息
     */
    List<SysAttachment> queryAttachmentInfoByIds(List<String> ids);

    /**
     * 附件上传
     * @param file 附件
     * @return 附件全路径
     */
    String upload(MultipartFile file);

    /**
     * 通过 uploadId值获取已上传分片附件的索引值
     * @param uploadId 附件上传id
     * @return 已上传的附件索引集合
     */
    List<Integer> chunksUploadedIndex(String uploadId);

    /**
     * 获取分片附件上传id
     * @param fullFilePath 附件全路径
     * @return uploadId
     */
    String chunksGetUploadId(String fullFilePath);

    /**
     * 分片上传
     * @param file 分片附件
     * @param uploadId 前端生成uploadId
     * @param index 附件索引
     */
    void chunksUpload(MultipartFile file, String uploadId, String index);

    /**
     * 分片合并
     */
    void chunksMerge(String md5, String uploadId, Integer total);

    /**
     * 业务删除附件（仅做状态的修改）
     */
    void deleteFromBusiness(String id);

    /**
     * 获取附件下载链接
     * @param id 附件id
     * @param expireTime 过期时间，为 null 则取配置附件指定时间
     * @return 下载链接
     */
    String getDownloadURL(String id, Integer expireTime);

    /**
     * 公开附件下载
     * @param id 附件id
     * @return 附件
     */
    ResponseEntity<StreamingResponseBody> publicDownload(String id, String fileName);

    /**
     * 本地附件下载（仅LOCAL模式下使用，其他存储方式中直接通过附件服务器获取）
     * @param key 附件路径及过期时间密文
     * @return 附件
     */
    File localDownload(String key);
}
