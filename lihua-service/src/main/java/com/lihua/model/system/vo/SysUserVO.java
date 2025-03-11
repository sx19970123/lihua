package com.lihua.model.system.vo;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import com.github.liaochong.myexcel.core.annotation.MultiColumn;
import com.lihua.annotation.sensitive.Sensitive;
import com.lihua.enums.DesensitizedTypeEnum;
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
@ExcelModel(sheetName = "用户信息",includeAllField = false)
public class SysUserVO extends BaseEntity {
    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    @ExcelColumn(index = 0, title = "用户信息->*用户名", width = 6)
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 用户名称
     */
    @ExcelColumn(order = 1, index = 1, title = "用户信息->*用户昵称", width = 6)
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别
     */
    @ExcelColumn(order = 2, index = 2,title = "用户信息->*性别", writeConverter = SysDictWriteConverter.class)
    @ExcelWriteConverterDictTypeCode("user_gender")
    private String gender;

    /**
     * 用户状态
     */
    @ExcelColumn(order = 3, index = 3, title = "用户信息->*状态", writeConverter = SysDictWriteConverter.class)
    @ExcelWriteConverterDictTypeCode("sys_status")
    private String status;

    /**
     * 用户应用系统主题
     */
    private String theme;

    /**
     * 手机号码
     */
    @Sensitive(type = DesensitizedTypeEnum.PHONE_NUMBER, ignoreRoleCodes = {})
    @ExcelColumn(order = 4, index = 4, title = "用户信息->手机号码", width = 8)
    private String phoneNumber;

    /**
     * 邮箱
     */
    @ExcelColumn(order = 5, index = 5, title = "用户信息->邮箱", width = 10)
    private String email;

    /**
     * 备注
     */
    @ExcelColumn(order = 6, index = 6, title = "用户信息->备注", width = 10)
    private String remark;

    /**
     * 角色名称
     */
    @ExcelColumn(order = 7, index = 7, title = "用户信息->角色", width = 15)
    private String roleName;

    /**
     * 所属部门名称集合
     */
    @MultiColumn(classType = String.class)
    @ExcelColumn(order = 8, index = 8, title = "部门信息->部门名称", width = 10)
    private List<String> deptLabelList;

    /**
     * 所属部门编码集合
     */
    @MultiColumn(classType = String.class)
    @ExcelColumn(order = 9, index = 9, title = "部门信息->部门编码", width = 10)
    private List<String> deptCodeList;

    /**
     * 所属部门下的岗位名称
     */
    @MultiColumn(classType = String.class)
    @ExcelColumn(order = 10, index = 10, title = "部门信息->岗位", width = 10)
    private List<String> postLabelList;

    /**
     * 所属部门id集合
     */
    private List<String> deptIdList;

    /**
     * 默认单位id
     */
    private String defaultDeptId;

    /**
     * 默认单位集合（用于sql接收数据）
     */
    private List<String> defaultDeptIdList;

    /**
     * 所属角色id集合
     */
    private List<String> roleIdList;

    /**
     * 所属岗位id集合
     */
    private List<String> postIdList;

    /**
     * 角色名称集合
     */
    private List<String> roleNameList;

    /**
     * excel 批量导入异常说明
     * 数据导入后，因异常无法入库的数据错误描述
     */
    @ExcelColumn(order = 11, title = "系统提示", style = {"cell->color:red"})
    private String importErrorMsg;

    /**
     * 用户注册类型
     */
    private String registerType;
}
