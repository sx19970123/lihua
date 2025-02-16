package com.lihua.model.system.dto;

import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDataDTO extends BaseDTO {
    private String dictTypeCode;
    private String label;
    private String value;
    private String type;
    private String status;
}
