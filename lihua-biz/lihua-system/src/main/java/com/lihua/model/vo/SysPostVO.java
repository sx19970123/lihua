package com.lihua.model.vo;

import com.lihua.entity.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysPostVO extends SysPost {

    // 所属部门名称
    private String deptName;
}
