package com.lihua.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 附件秒传结果：uploaded=false 表示无该 md5 记录，客户端转普通上传
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class FastUploadResultVO extends AttachmentUploadVO {

    /**
     * 是否秒传命中；命中时其余 VO 字段非空平铺返回
     */
    private Boolean uploaded;
}
