package com.lihua.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class SysUserPost {
    private String userId;
    private String postId;
    private LocalDateTime createTime;
    private String createId;
}
