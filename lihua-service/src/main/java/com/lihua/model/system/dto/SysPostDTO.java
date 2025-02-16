package com.lihua.model.system.dto;

import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysPostDTO extends BaseDTO {
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 岗位名称
     */
    private String name;
    /**
     * 岗位编码
     */
    private String code;
    /**
     * 岗位状态
     */
    private String status;
}
