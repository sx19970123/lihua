package com.lihua.model.security;

import com.lihua.model.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统用户
 */
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
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名称
     */
    private String nickname;

    /**
     * 用户头像
     */
    @NotBlank(message = "请输入用户头像")
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
     * 最后登陆IP地址
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

}
