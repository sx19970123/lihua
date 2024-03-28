package com.lihua.system.model;

import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuDTO extends BaseDTO {
    private String label;
    private String status;
}
