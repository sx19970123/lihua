package com.lihua.model.system.dto;

import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysAttachmentDTO extends BaseDTO {
    // 创建时间集合
    private List<LocalDate> createTimeList;
    // 附件名称
    private String originalName;
    // 业务名称
    private String businessName;
    // 附件状态
    private String status;
    // 上传方式
    private String uploadMode;
}
