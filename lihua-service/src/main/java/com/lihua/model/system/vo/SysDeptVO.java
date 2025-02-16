package com.lihua.model.system.vo;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import com.lihua.entity.system.SysDept;
import com.lihua.entity.system.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ExcelModel(sheetName = "部门信息",includeAllField = false)
@Data
public class SysDeptVO extends SysDept {

    /**
     * 部门名称路径
     */
    @ExcelColumn(index = 0, title = "*部门名称路径", width = 12)
    private String namePath;

    /**
     * 岗位名称
     */
    @ExcelColumn(order = 9, title = "部门下岗位", width = 12)
    private String postNames;

    /**
     * excel 批量导入异常说明
     * 数据导入后，因异常无法入库的数据错误描述
     */
    @ExcelColumn(order = 10, title = "系统提示", style = {"cell->color:red"})
    private String importErrorMsg;

    /**
     * 岗位信息
     */
    private List<SysPost> sysPostList;
}
