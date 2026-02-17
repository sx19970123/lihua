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
     * excel 批量导入异常说明
     * 数据导入后，因异常无法入库的数据错误描述
     */
    private String importErrorMsg;

    /**
     * 岗位信息
     */
    private List<SysPost> sysPostList;
}
