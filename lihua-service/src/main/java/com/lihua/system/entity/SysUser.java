package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import com.lihua.system.entity.validation.ProfileSaveValidation;
import com.lihua.system.entity.validation.ProfileThemeValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
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
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    @NotNull(message = "用户昵称不能为空",
            groups = ProfileSaveValidation.class)
    @Size(max = 20,
        message = "用户昵称最大不能超过20字符",
        groups = ProfileSaveValidation.class)
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户性别
     */
    @NotNull(message = "用户性别不能为空",
            groups = ProfileSaveValidation.class)
    private String gender;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 用户应用系统主题
     */
    @NotNull(message = "主题描述字符串为空",
            groups = ProfileThemeValidation.class)
    private String theme;

    /**
     * 逻辑删除标志
     */
    private String delFlag;

    /**
     * 最后登陆ip
     */
    private String loginIp;

    /**
     * 最后登陆时间
     */
    private LocalDateTime loginTime;

    /**
     * 邮箱
     */
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "请输入正确的邮箱地址",
            groups = ProfileSaveValidation.class)
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$",
            message = "请输入正确的手机号码",
            groups = ProfileSaveValidation.class)
    private String phoneNumber;

}
