package com.lihua.model.dto;

import com.lihua.mybatis.model.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDataDTO extends BaseDTO {
    @NotBlank(message = "字典编码不能为空")
    private String dictTypeCode;
    private String label;
    private String value;
    private String type;
    private String status;
}
