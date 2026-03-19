package com.lihua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.excel.annotation.ExcelDictType;
import com.lihua.excel.converter.ExcelDictConverter;
import com.lihua.mybatis.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDept extends BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 父级id
     */
    @NotNull(message = "请选择上级节点")
    private String parentId;

    /**
     * 部门名称
     */
    @NotNull(message = "请输入部门名称")
    @ExcelProperty("部门名称")
    @ColumnWidth(20)
    private String name;

    /**
     * 部门编码
     */
    @NotNull(message = "请输入部门编码")
    @ExcelProperty("部门编码")
    @ColumnWidth(20)
    private String code;

    /**
     * 状态
     */
    @NotNull(message = "请选择状态")
    @ExcelProperty(value = "状态", converter = ExcelDictConverter.class)
    @ExcelDictType("sys_status")
    private String status;

    /**
     * 排序
     */
    @NotNull(message = "请输入排序")
    private Integer sort;

    /**
     * 负责人
     */
    @ExcelProperty("负责人")
    @ColumnWidth(15)
    private String manager;

    /**
     * 联系电话
     */
    @Pattern(regexp = "^(|1[3-9]\\d{9})$",
            message = "请输入正确的手机号码")
    @ExcelProperty("联系电话")
    @ColumnWidth(20)
    private String phoneNumber;

    /**
     * 邮箱
     */
    @Pattern(regexp = "^(|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",
            message = "请输入正确的邮箱地址")
    @ExcelProperty("邮箱")
    @ColumnWidth(20)
    private String email;

    /**
     * 传真
     */
    @ExcelProperty("传真")
    @ColumnWidth(20)
    private String fax;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    @ColumnWidth(40)
    private String remark;

    /**
     * 子集
     */
    @TableField(exist = false)
    private List<SysDept> children;
}
