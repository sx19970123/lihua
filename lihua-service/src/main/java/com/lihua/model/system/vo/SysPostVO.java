package com.lihua.model.system.vo;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import com.lihua.entity.system.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ExcelModel(sheetName = "岗位信息",includeAllField = false)
@Data
public class SysPostVO extends SysPost {

    // 所属部门名称
    @ExcelColumn(order = 2, index = 2, title = "*所属部门", width = 12)
    private String deptName;

    /**
     * excel 批量导入异常说明
     * 数据导入后，因异常无法入库的数据错误描述
     */
    @ExcelColumn(order = 9, title = "系统提示", style = {"cell->color:red"})
    private String importErrorMsg;
}
