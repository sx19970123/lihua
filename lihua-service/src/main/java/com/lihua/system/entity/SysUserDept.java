package com.lihua.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class SysUserDept {
    private String userId;
    private String deptId;
    private LocalDateTime createTime;
    private String createId;
    private String defaultDept;
}
