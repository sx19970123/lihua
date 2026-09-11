package com.lihua.attachment.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 附件上传方式字典（sys_attachment_upload_mode）枚举
 */
@Getter
@AllArgsConstructor
public enum AttachmentUploadModeEnum implements DictEnum {

    /**
     * 一般上传
     */
    NORMAL("0"),

    /**
     * 分片上传
     */
    CHUNK("1"),

    /**
     * 文件秒传
     */
    FAST("2");

    private final String value;

    @Override
    public String getType() {
        return "sys_attachment_upload_mode";
    }
}
