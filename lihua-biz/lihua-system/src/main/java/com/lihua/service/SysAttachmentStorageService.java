package com.lihua.service;

import com.lihua.entity.SysAttachment;
import com.lihua.model.dto.AttachmentChunkMergeDTO;
import com.lihua.model.dto.AttachmentChunkStartDTO;
import com.lihua.model.dto.AttachmentFastUploadDTO;
import com.lihua.model.dto.AttachmentUploadDTO;
import com.lihua.model.vo.AttachmentUploadVO;
import com.lihua.model.vo.FastUploadResultVO;
import com.lihua.model.vo.SysAttachmentChunkVO;
import com.lihua.model.vo.SysAttachmentVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

public interface SysAttachmentStorageService {

    /**
     * 附件是否存在（仅按 md5 匹配，与秒传命中条件一致）
     * @return 是否存在
     */
    boolean existsAttachmentByMd5(String md5);

    /**
     * 根据路径查询附件信息，用于附件组件数据回显（对外字段白名单；缺失 id 回填占位行）
     * @param ids 附件id集合
     * @return 对应的附件信息
     */
    List<SysAttachmentVO> queryAttachmentInfoByIds(List<String> ids);

    /**
     * 上传附件（单管线：公开性由参数显式声明，业务附件恒私密）
     * @param uploadDTO 上传参数
     * @return 上传结果统一 VO（id/path/isPublic/url/originalName/type）
     */
    AttachmentUploadVO uploadAttachment(AttachmentUploadDTO uploadDTO);

    /**
     * 附件秒传（命中同 md5 且物理文件存在的附件行即复制建行，未命中返回 uploaded=false）
     * @param fastUploadDTO 秒传参数
     * @return 秒传结果（uploaded + 命中时平铺上传 VO 字段）
     */
    FastUploadResultVO fastUpload(AttachmentFastUploadDTO fastUploadDTO);

    /**
     * 分片上传开始（建行 status=2 并返回 uploadId 与附件id）
     * @param chunkStartDTO 分片启动参数
     * @return 分片上传唯一uploadId和附件表id对象
     */
    SysAttachmentChunkVO chunksUploadAttachmentStart(AttachmentChunkStartDTO chunkStartDTO);

    /**
     * 通过 uploadId值获取已上传分片附件的索引值
     * @param uploadId 附件上传id
     * @return 已上传的附件索引集合
     */
    List<Integer> chunksUploadedIndex(String uploadId);

    /**
     * 分片上传
     * @param file 分片附件
     * @param uploadId 分片上传会话id
     * @param index 分片索引
     */
    void chunksUpload(MultipartFile file, String uploadId, Integer index);

    /**
     * 分片合并（按 uploadId 定位分片行，返回上传统一 VO）
     * @param chunkMergeDTO 合并参数
     * @param total 分片总数
     */
    AttachmentUploadVO chunksMerge(AttachmentChunkMergeDTO chunkMergeDTO, Integer total);

    /**
     * 业务删除附件（仅做状态的修改）
     */
    void deleteFromBusiness(List<String> ids);

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
     * 附件下载单路由（key 私密签名链 / fullPath 公开链，二选一；permitAll 链接即凭证）
     * @param key 私密链签名令牌
     * @param fullPath 公开链对象键
     * @param originName 可选，下载展示文件名
     * @return 附件响应（LOCAL 流式 / OSS 302 预签名）
     */
    ResponseEntity<StreamingResponseBody> download(String key, String fullPath, String originName);
}
