package com.lihua.model.system.dto;

import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDTO extends BaseDTO {
    private String name;
    private String code;
    private String status;
}
