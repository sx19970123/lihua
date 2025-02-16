package com.lihua.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class SysUserDept {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    private String createId;
    /**
     * 默认
     */
    private String defaultDept;
}
