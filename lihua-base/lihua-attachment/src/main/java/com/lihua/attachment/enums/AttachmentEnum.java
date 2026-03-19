package com.lihua.attachment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttachmentEnum {
    /**
     * 附件临时路径信息加密key
     */
    ATTACHMENT_URL_KEY("AtTaChMeNtUrLkEy");

    private final String value;
}
