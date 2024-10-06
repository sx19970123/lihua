package com.lihua.model.security;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户
 */
@Data
public class CurrentUser implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    @NotNull(message = "请输入用户名")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "请输入密码")
    private String password;

    /**
     * 用户名称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 用户应用系统主题
     */
    private String theme;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

    /**
     * 密码更新时间
     */
    private LocalDateTime passwordUpdateTime;

    /**
     * 注册类型
     * 0：管理员注册
     * 1：自助注册
     */
    private String registerType;

    /**
     * 验证码
     */
    private String captchaVerification;

    /**
     * 请求 key
     */
    @NotNull(message = "请求key不存在")
    private String requestKey;
}
