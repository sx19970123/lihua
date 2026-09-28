package com.lihua.model.dto;

import com.lihua.model.validation.ProfileValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 个人中心基础信息接收实体
 */
@Data
public class SysProfileBasicDTO {
    /**
     * 用户昵称
     */
    @Size(max = 20,
        message = "用户昵称最大不能超过20字符",
        groups = ProfileValidation.ProfileSaveValidation.class)
    private String nickname;

    /**
     * 用户头像
     */
    @Size(max = 4000,
        message = "用户头像最大不能超过4000字符",
        groups = ProfileValidation.ProfileSaveValidation.class)
    private String avatar;

    /**
     * 用户性别
     */
    @Pattern(regexp = "^(|[012])$",
        message = "性别不合法",
        groups = ProfileValidation.ProfileSaveValidation.class)
    private String gender;

    /**
     * 邮箱
     */
    @Pattern(regexp = "^(|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",
        message = "请输入正确的邮箱地址",
        groups = ProfileValidation.ProfileSaveValidation.class)
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^(|1[3-9]\\d{9})$",
        message = "请输入正确的手机号码",
        groups = ProfileValidation.ProfileSaveValidation.class)
    private String phoneNumber;

    /**
     * 用户应用系统主题
     */
    @NotNull(message = "主题描述字符串为空",
        groups = ProfileValidation.ProfileThemeValidation.class)
    @Size(max = 4000,
        message = "主题描述最大不能超过4000字符",
        groups = ProfileValidation.ProfileThemeValidation.class)
    private String theme;

}
