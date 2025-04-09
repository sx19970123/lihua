package com.lihua.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.lihua.model.BaseEntity;
import com.lihua.utils.excel.annotation.ExcelWriteConverterDictTypeCode;
import com.lihua.utils.excel.converter.SysDictWriteConverter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class SysPost extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 部门主键
     */
    @NotNull(message = "请选择所属部门")
    private String deptId;

    /**
     * 部门编码
     */
    @NotNull(message = "请选择所属部门")
    private String deptCode;

    /**
     * 岗位名称
     */
    @ExcelColumn(index = 0, title = "*岗位名称", width = 12)
    @NotNull(message = "请输入岗位名称")
    private String name;

    /**
     * 岗位编码
     */
    @ExcelColumn(order = 1, index = 1, title = "*岗位编码", width = 12)
    @NotNull(message = "请输入岗位编码")
    private String code;

    /**
     * 岗位排序
     */
    @NotNull(message = "请输入岗位排序")
    private Integer sort;

    /**
     * 岗位状态
     */
    @ExcelColumn(order = 3, index = 3, title = "*状态", writeConverter = SysDictWriteConverter.class)
    @ExcelWriteConverterDictTypeCode("sys_status")
    @NotNull(message = "请选择状态")
    private String status;

    /**
     * 岗位负责人姓名
     */
    @ExcelColumn(order = 4, index = 4, title = "负责人")
    private String manager;

    /**
     * 岗位联系电话
     */
    @Pattern(regexp = "^(|1[3-9]\\d{9})$",
            message = "请输入正确的手机号码")
    @ExcelColumn(order = 5, index = 5, title = "联系电话", width = 6)
    private String phoneNumber;

    /**
     * 岗位邮件
     */
    @Pattern(regexp = "^(|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",
            message = "请输入正确的邮箱地址")
    @ExcelColumn(order = 6, index = 6, title = "邮箱", width = 6)
    private String email;

    /**
     * 岗位传真号码
     */
    @ExcelColumn(order = 7, index = 7, title = "传真", width = 6)
    private String fax;

    /**
     * 备注
     */
    @ExcelColumn(order = 8, index = 8, title = "备注", width = 6)
    private String remark;

    /**
     * 逻辑删除标志
     */
    private String delFlag;
}
