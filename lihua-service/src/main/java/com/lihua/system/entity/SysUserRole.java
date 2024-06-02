package com.lihua.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class SysUserRole {
    private String userId;
    private String roleId;
    private LocalDateTime createTime;
    private String createId;
}
