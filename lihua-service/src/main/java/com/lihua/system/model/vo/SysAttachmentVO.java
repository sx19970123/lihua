package com.lihua.system.model.vo;

import com.lihua.system.entity.SysAttachment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysAttachmentVO extends SysAttachment {

    // 上传用户昵称
    private String uploadName;
}
