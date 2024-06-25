package com.lihua.system.model;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import com.github.liaochong.myexcel.core.annotation.IgnoreColumn;
import com.github.liaochong.myexcel.core.annotation.MultiColumn;
import com.lihua.utils.excel.annotation.ExcelWriteConverterDictTypeCode;
import com.lihua.model.BaseEntity;
import com.lihua.utils.excel.converter.SysDictWriteConverter;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ExcelModel(sheetName = "用户信息",excludeParent = true)
public class SysUserVO extends BaseEntity {
    /**
     * 主键
     */
    @IgnoreColumn
    private String id;

    /**
     * 用户名
     */
    @ExcelColumn(order = 0, title = "用户信息->用户名")
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @IgnoreColumn
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 用户名称
     */
    @ExcelColumn(order = 1, title = "用户信息->用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @IgnoreColumn
    private String avatar;

    /**
     * 性别
     */
    @ExcelColumn(order = 2,title = "用户信息->性别", writeConverter = SysDictWriteConverter.class)
    @ExcelWriteConverterDictTypeCode("user_gender")
    private String gender;

    /**
     * 用户状态
     */
    @ExcelColumn(order = 3, title = "用户信息->状态", writeConverter = SysDictWriteConverter.class)
    @ExcelWriteConverterDictTypeCode("sys_status")
    private String status;

    /**
     * 用户应用系统主题
     */
    @IgnoreColumn
    private String theme;

    /**
     * 手机号码
     */
    @ExcelColumn(order = 4, title = "用户信息->手机号码")
    private String phoneNumber;

    /**
     * 邮箱
     */
    @ExcelColumn(order = 5, title = "用户信息->邮箱")
    private String email;

    /**
     * 备注
     */
    @ExcelColumn(order = 6, title = "用户信息->备注")
    private String remark;

    /**
     * 所属部门名称集合
     */
    @MultiColumn(classType = String.class)
    @ExcelColumn(order = 7, title = "部门信息->部门名称")
    private List<String> deptLabelList;

    /**
     * 所属部门编码集合
     */
    @MultiColumn(classType = String.class)
    @ExcelColumn(order = 8, title = "部门信息->部门编码")
    private List<String> deptCodeList;

    /**
     * 所属部门下的岗位名称
     */
    @MultiColumn(classType = String.class)
    @ExcelColumn(order = 9, title = "部门信息->岗位")
    private List<String> postLabelList;

    /**
     * 所属部门id集合
     */
    @IgnoreColumn
    private List<String> deptIdList;

    /**
     * 默认单位id
     */
    @IgnoreColumn
    private String defaultDeptId;

    /**
     * 默认单位集合（用于sql接收数据）
     */
    @IgnoreColumn
    private List<String> defaultDeptIdList;

    /**
     * 所属角色id集合
     */
    @IgnoreColumn
    private List<String> roleIdList;

    /**
     * 所属岗位id集合
     */
    @IgnoreColumn
    private List<String> postIdList;

}
