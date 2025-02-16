package com.lihua.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
