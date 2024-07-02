package com.lihua.system.model.vo;

import com.lihua.system.entity.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysPostVO extends SysPost {

    // 所属部门名称
    private String deptName;

}
