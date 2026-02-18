package com.lihua.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.lihua.model.BaseDTO;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDTO extends BaseDTO {
    private String name;
    private String code;
    private String status;
}
