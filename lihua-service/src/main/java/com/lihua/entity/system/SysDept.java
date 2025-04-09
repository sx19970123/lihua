package com.lihua.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import com.lihua.model.BaseEntity;
import com.lihua.utils.excel.annotation.ExcelWriteConverterDictTypeCode;
import com.lihua.utils.excel.converter.SysDictWriteConverter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ExcelModel(includeAllField = false)
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
    @ExcelColumn(order = 1, index = 1, title = "*部门名称", width = 6)
    @NotNull(message = "请输入部门名称")
    private String name;

    /**
     * 部门编码
     */
    @ExcelColumn(order = 2, index = 2, title = "*部门编码", width = 6)
    @NotNull(message = "请输入部门编码")
    private String code;

    /**
     * 状态
     */
    @ExcelColumn(order = 3, index = 3, title = "*状态", writeConverter = SysDictWriteConverter.class)
    @ExcelWriteConverterDictTypeCode("sys_status")
    @NotNull(message = "请选择状态")
    private String status;

    /**
     * 排序
     */
    @NotNull(message = "请输入排序")
    private Integer sort;

    /**
     * 负责人
     */
    @ExcelColumn(order = 4, index = 4, title = "负责人")
    private String manager;

    /**
     * 联系电话
     */
    @ExcelColumn(order = 5, index = 5, title = "联系电话", width = 8)
    @Pattern(regexp = "^(|1[3-9]\\d{9})$",
            message = "请输入正确的手机号码")
    private String phoneNumber;

    /**
     * 邮箱
     */
    @ExcelColumn(order = 6, index = 6, title = "邮箱", width = 10)
    @Pattern(regexp = "^(|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",
            message = "请输入正确的邮箱地址")
    private String email;

    /**
     * 传真
     */
    @ExcelColumn(order = 7, index = 7, title = "传真", width = 6)
    private String fax;

    /**
     * 逻辑删除标志
     */
    private String delFlag;

    /**
     * 备注
     */
    @ExcelColumn(order = 8, index = 8, title = "备注", width = 6)
    private String remark;

    /**
     * 子集
     */
    @TableField(exist = false)
    private List<SysDept> children;
}
