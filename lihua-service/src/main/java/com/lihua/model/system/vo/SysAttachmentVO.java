package com.lihua.model.system.vo;

import com.lihua.entity.system.SysAttachment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysAttachmentVO extends SysAttachment {

    // 上传用户昵称
    private String uploadName;
}
