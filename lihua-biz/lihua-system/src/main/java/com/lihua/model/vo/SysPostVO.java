package com.lihua.model.vo;

import com.lihua.entity.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.fesod.sheet.annotation.ExcelIgnoreUnannotated;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

@ExcelIgnoreUnannotated
@EqualsAndHashCode(callSuper = true)
@Data
public class SysPostVO extends SysPost {

    // 所属部门名称
    @ExcelProperty("所属部门")
    @ColumnWidth(20)
    private String deptName;

    // 对应用户id
    private String userId;
}
