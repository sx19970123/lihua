package com.lihua.model.vo;

import com.lihua.excel.annotation.ExcelComment;
import com.lihua.excel.annotation.ExcelDictType;
import com.lihua.excel.converter.ExcelDictConverter;
import com.lihua.excel.enums.CommentUseEnum;
import com.lihua.mybatis.model.BaseEntity;
import com.lihua.sensitive.annotation.Sensitive;
import com.lihua.sensitive.enums.DesensitizedTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.fesod.sheet.annotation.ExcelIgnoreUnannotated;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;
import org.apache.fesod.sheet.annotation.write.style.ContentStyle;
import org.apache.fesod.sheet.enums.poi.HorizontalAlignmentEnum;
import org.apache.fesod.sheet.enums.poi.VerticalAlignmentEnum;

import java.util.List;

/**
 * 系统用户
 */
@ExcelIgnoreUnannotated
@ContentStyle(
    horizontalAlignment = HorizontalAlignmentEnum.CENTER,
    verticalAlignment = VerticalAlignmentEnum.CENTER
)
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserVO extends BaseEntity {

    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @ExcelProperty({"用户信息", "用户名"})
    @ColumnWidth(20)
    @ExcelComment(value = "用户名，全局唯一", headRowNum = 1)
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 用户名称
     */
    @ExcelProperty({"用户信息", "昵称"})
    @ColumnWidth(20)
    @ExcelComment(value = "昵称，必填", use = CommentUseEnum.HEAD, headRowNum = 1)
    private String nickname;

    /**
     * 性别
     */
    @ExcelProperty(value = {"用户信息", "性别"}, converter = ExcelDictConverter.class)
    @ExcelDictType("user_gender")
    @ColumnWidth(10)
    @ExcelComment(value = "性别，字典数据", use = CommentUseEnum.BODY)
    private String gender;

    /**
     * 用户状态
     */
    @ExcelProperty(value = {"用户信息", "状态"}, converter = ExcelDictConverter.class)
    @ExcelDictType("sys_status")
    @ColumnWidth(10)
    private String status;

    /**
     * 用户注册类型
     */
    @ExcelProperty(value = {"用户信息", "注册方式"}, converter = ExcelDictConverter.class)
    @ExcelDictType("sys_user_register_type")
    @ColumnWidth(20)
    private String registerType;

    /**
     * 手机号码
     */
    @Sensitive(type = DesensitizedTypeEnum.PHONE_NUMBER, ignoreRoleCodes = {})
    @ExcelProperty({"用户信息", "手机号码"})
    @ColumnWidth(20)
    private String phoneNumber;

    /**
     * 邮箱
     */
    @ExcelProperty({"用户信息", "邮箱"})
    @ColumnWidth(30)
    private String email;

    /**
     * 角色名称
     */
    @ExcelProperty({"用户信息", "角色名称"})
    @ColumnWidth(30)
    private String roleName;

    /**
     * 备注
     */
    @ExcelProperty({"用户信息", "备注"})
    @ColumnWidth(20)
    private String remark;

    /**
     * 部门编码
     */
    @ExcelProperty({"所属部门", "部门名称"})
    @ColumnWidth(30)
    private String deptName;

    /**
     * 部门名称
     */
    @ExcelProperty({"所属部门", "部门编码"})
    @ColumnWidth(30)
    private String deptCode;

    /**
     * 岗位名称
     */
    @ExcelProperty({"所属部门", "岗位"})
    @ColumnWidth(30)
    private String postName;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户应用系统主题
     */
    private String theme;

    /**
     * 默认单位id
     */
    private String defaultDeptId;

    /**
     * 默认单位集合（用于sql接收数据）
     */
    private List<String> defaultDeptIdList;

    /**
     * 部门id列表
     */
    private List<String> deptIdList;

    /**
     * 岗位id列表
     */
    private List<String> postIdList;

    /**
     * 角色id列表
     */
    private List<String> roleIdList;

    /**
     * 角色名称列表
     */
    private List<String> roleNameList;

    /**
     * 部门名称集合
     */
    private List<String> deptLabelList;
}
