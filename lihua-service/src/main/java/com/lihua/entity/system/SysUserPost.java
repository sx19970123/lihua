package com.lihua.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class SysUserPost {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 岗位id
     */
    private String postId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    private String createId;
}
