package com.lihua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.excel.annotation.ExcelComment;
import com.lihua.excel.annotation.ExcelDropdown;
import com.lihua.excel.annotation.ExcelEnableComment;
import com.lihua.excel.annotation.ExcelEnableDropdown;
import com.lihua.excel.converter.ExcelDictConverter;
import com.lihua.excel.enums.DropdownTypeEnum;
import com.lihua.model.validation.ProfileValidation;
import com.lihua.mybatis.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.fesod.sheet.annotation.ExcelIgnoreUnannotated;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

import java.time.LocalDateTime;

@ExcelEnableComment
@ExcelEnableDropdown
@ExcelIgnoreUnannotated
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUser extends BaseEntity {

    /**
     * 用户id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    @ColumnWidth(20)
    @ExcelComment(value = "用户名必填且系统唯一")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    @Size(max = 20,
        message = "用户昵称最大不能超过20字符",
        groups = ProfileValidation.ProfileSaveValidation.class)
    @ExcelProperty("昵称")
    @ColumnWidth(20)
    @ExcelComment(value = "用户昵称必填")
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户性别
     */
    @ExcelProperty(value = "性别", converter = ExcelDictConverter.class)
    @ColumnWidth(20)
    @ExcelDropdown(type = DropdownTypeEnum.DICT, value = "user_gender")
    @ExcelComment(value = "性别必填，且只能使用下拉选项值")
    private String gender;

    /**
     * 用户状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConverter.class)
    @ColumnWidth(20)
    @ExcelDropdown(type = DropdownTypeEnum.DICT, value = "sys_status")
    @ExcelComment(value = "状态必填，且只能使用下拉选项值")
    private String status;

    /**
     * 用户应用系统主题
     */
    @NotNull(message = "主题描述字符串为空",
            groups = ProfileValidation.ProfileThemeValidation.class)
    private String theme;

    /**
     * 邮箱
     */
    @Pattern(regexp = "^(|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",
            message = "请输入正确的邮箱地址",
            groups = ProfileValidation.ProfileSaveValidation.class)
    @ExcelProperty("邮箱")
    @ColumnWidth(20)
    @ExcelComment(value = "邮箱选填，请注意格式")
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^(|1[3-9]\\d{9})$",
            message = "请输入正确的手机号码",
            groups = ProfileValidation.ProfileSaveValidation.class)
    @ExcelProperty("手机号码")
    @ColumnWidth(20)
    @ExcelComment(value = "手机号码选填，请注意格式")
    private String phoneNumber;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    @ColumnWidth(30)
    @ExcelComment(value = "备注选填")
    private String remark;

    /**
     * 密码更新时间
     */
    private LocalDateTime passwordUpdateTime;

    /**
     * 注册类型
     */
    private String registerType;

}
