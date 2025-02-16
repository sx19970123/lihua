package com.lihua.model.attachment;

import lombok.Data;
import java.io.InputStream;

/**
 * 附件附件与原附件名绑定对象
 */
@Data
public class AttachmentStreamAndInfoModel {
    // 附件对象
    private InputStream inputStream;
    // 附件原附件名
    private String originName;
    // 附件大小
    private long size;
}
