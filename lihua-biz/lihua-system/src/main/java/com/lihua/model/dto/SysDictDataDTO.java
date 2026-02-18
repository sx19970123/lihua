package com.lihua.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.lihua.model.BaseDTO;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDataDTO extends BaseDTO {
    private String dictTypeCode;
    private String label;
    private String value;
    private String type;
    private String status;
}
