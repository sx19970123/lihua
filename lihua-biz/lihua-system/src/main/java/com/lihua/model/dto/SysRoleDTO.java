package com.lihua.model.dto;

import com.lihua.mybatis.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDTO extends BaseDTO {
    private String name;
    private String code;
    private String status;
}
