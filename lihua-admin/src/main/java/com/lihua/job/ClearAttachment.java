package com.lihua.job;

import com.lihua.config.LihuaConfig;
import com.lihua.entity.system.SysAttachment;
import com.lihua.model.BaseDTO;
import com.lihua.model.system.dto.SysAttachmentDTO;
import com.lihua.service.system.attachment.SysAttachmentService;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ClearAttachment {

    @Resource
    private SysAttachmentService sysAttachmentService;

    @Resource
    private LihuaConfig lihuaConfig;

    /**
     * 清除附件
     * 通过定时任务定期执行
     */
    @XxlJob("clearAttachment")
    public void clear () {
        SysAttachmentDTO sysAttachmentDTO = new SysAttachmentDTO();
        sysAttachmentDTO.setPageSize(BaseDTO.MAX_PAGE_NUM);
        // 查询所有附件
        List<SysAttachment> sysAttachmentList =
                sysAttachmentService.queryPage(sysAttachmentDTO).getRecords();

        List<String> uploadPublicBusinessCode = lihuaConfig.getUploadPublicBusinessCode();

        // 过滤编码
        List<String> attachmentIds = sysAttachmentList
                .stream()
                .filter(sysAttachment -> !uploadPublicBusinessCode.contains(sysAttachment.getBusinessCode()))
                .map(SysAttachment::getId)
                .toList();

        // 执行附件强制删除
        sysAttachmentService.forceDelete(attachmentIds);

        log.info("附件清理完成");

    }
}
