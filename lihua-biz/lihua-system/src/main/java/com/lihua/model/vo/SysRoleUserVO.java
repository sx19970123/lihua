package com.lihua.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色-分配用户列表返回模型（全量用户 + 该角色授权标记）
 */
@Data
public class SysRoleUserVO {

    // 主键id
    private String id;

    // 用户名
    private String username;

    // 昵称
    private String nickname;

    // 用户状态
    private String status;

    // 创建时间
    private LocalDateTime createTime;

    // 是否已授权该角色
    private Boolean authorized;
}
