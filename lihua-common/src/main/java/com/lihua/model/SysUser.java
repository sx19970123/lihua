package com.lihua.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUser extends BaseEntity {
    /**
     * 主键
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
     * 最后登陆IP地址
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginTime;

}
