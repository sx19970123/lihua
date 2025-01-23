package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysAttachment;
import com.lihua.system.model.dto.SysAttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

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
     * 保存附件信息
     * @return 附件id
     */
    String save(MultipartFile file);

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

}
