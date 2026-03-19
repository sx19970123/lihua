package com.lihua.model.dto;

import com.lihua.mybatis.model.BaseDTO;
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
