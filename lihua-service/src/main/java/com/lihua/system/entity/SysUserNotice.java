package com.lihua.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SysUserNotice {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 通知id
     */
    private String noticeId;

    /**
     * 已读标记
     */
    private String readFlag;

    /**
     * star标记
     */
    private String starFlag;

    /**
     * 已读时间
     */
    private LocalDateTime readTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createId;
}
