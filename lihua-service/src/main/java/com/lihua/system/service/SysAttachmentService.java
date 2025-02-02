package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysAttachment;
import com.lihua.system.model.dto.SysAttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

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
    SysAttachment queryById(String id);

    /**
     * 根据路径查询文件信息，用于附件组件数据回显
     * @param pathList 附件存储路径
     * @return 对应的附件信息
     */
    List<SysAttachment> queryAttachmentInfoByPathList(List<String> pathList);

    /**
     * 保存附件信息
     * @return 附件id
     */
    String save(MultipartFile file, String businessCode, String businessName);

    /**
     * 保存附件信息（分片上传）
     * @param sysAttachment 附件信息
     * @return 附件id
     */
    String chunksSave(SysAttachment sysAttachment);

    /**
     * 通过 md5值获取已上传分片附件的索引值
     * @param md5 附件md5
     * @return 已上传的附件索引集合
     */
    List<Integer> chunksUploadedIndex(String md5);

    /**
     * 分片上传
     * @param file 分片附件
     * @param md5 完整文件的md5值
     * @param index 附件索引
     */
    void chunksUpload(MultipartFile file, String md5, String index);

    /**
     * 根据路径获取原文件名称
     * @param path 文件路径
     * @return 原文件名称
     */
    String queryOriginFileName(String path);

    /**
     * 附件批量删除
     */
    void deleteByIds(List<SysAttachment> sysAttachmentList);

    /**
     * 根据路径删除
     */
    void deleteByPath(String path);

    /**
     * 获取文件下载链接
     * @return 下载链接
     */
    String getDownloadURL(String path);

    /**
     * 获取文件下载链接
     * @return 下载链接
     */
    List<String> getDownloadURL(List<String> pathList);

    /**
     * 公开文件下载
     * @param path 文件路径
     * @return 文件
     */
    File publicDownload(String path);

    /**
     * 本地文件下载（仅LOCAL模式下使用，其他存储方式中直接通过文件服务器获取）
     * @param key 文件路径及过期时间密文
     * @return 文件
     */
    File localDownload(String key);
}
