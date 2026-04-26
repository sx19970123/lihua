package com.lihua.model.vo;

import com.lihua.entity.SysDept;
import com.lihua.entity.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.fesod.sheet.annotation.ExcelIgnoreUnannotated;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

import java.util.List;

@ExcelIgnoreUnannotated
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptVO extends SysDept {

    /**
     * 部门名称路径
     */
    @ExcelProperty("部门路径名称")
    @ColumnWidth(40)
    private String namePath;

    /**
     * 岗位名称
     */
    @ExcelProperty("岗位名称")
    @ColumnWidth(40)
    private String postNames;

    /**
     * 岗位信息
     */
    private List<SysPost> sysPostList;
}
