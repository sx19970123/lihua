package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
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
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户性别
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


}
