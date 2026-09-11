package com.lihua.attachment.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 附件上传状态字典（sys_attachment_status）枚举
 */
@Getter
@AllArgsConstructor
public enum AttachmentStatusEnum implements DictEnum {

    /**
     * 上传成功
     */
    SUCCESS("0"),

    /**
     * 上传失败
     */
    FAIL("1"),

    /**
     * 分片上传中
     */
    CHUNK_UPLOADING("2"),

    /**
     * 业务删除
     */
    BUSINESS_DELETED("3");

    private final String value;

    @Override
    public String getType() {
        return "sys_attachment_status";
    }
}
