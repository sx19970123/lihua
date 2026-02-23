package com.lihua.model.vo;

import com.lihua.entity.SysDept;
import com.lihua.entity.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptVO extends SysDept {

    /**
     * 部门名称路径
     */
    private String namePath;

    /**
     * 岗位名称
     */
    private String postNames;

    /**
     * 岗位信息
     */
    private List<SysPost> sysPostList;
}
