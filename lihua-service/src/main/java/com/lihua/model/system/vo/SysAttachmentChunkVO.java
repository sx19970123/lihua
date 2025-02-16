package com.lihua.model.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SysAttachmentChunkVO {

    // 上传id
    private String uploadId;

    // 附件id
    private String attachmentId;
}
