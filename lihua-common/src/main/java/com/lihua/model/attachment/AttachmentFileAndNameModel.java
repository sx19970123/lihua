package com.lihua.model.attachment;

import lombok.Data;
import java.io.InputStream;

/**
 * 附件文件与原文件名绑定对象
 */
@Data
public class AttachmentFileAndNameModel {
    // 附件对象
    private InputStream inputStream;
    // 附件原文件名
    private String originName;
}
