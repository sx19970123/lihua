package com.lihua.service.system.attachment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.entity.system.SysAttachment;
import com.lihua.model.system.dto.SysAttachmentDTO;
import com.lihua.model.system.vo.SysAttachmentVO;
import jakarta.validation.constraints.NotNull;

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
     * 附件强制删除
     */
    void forceDelete(List<String> ids);

    /**
     * 获取附件下载链接
     */
    String getDownloadURL(String id, Integer expireTime);

}
