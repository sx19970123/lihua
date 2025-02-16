package com.lihua.model.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class SysUserNoticeVO implements Serializable {

    /**
     * 公告id
     */
    private String noticeId;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型
     */
    private String type;

    /**
     * 公告优先级
     */
    private String priority;

    /**
     * 发布用户
     */
    private String releaseUser;

    /**
     * 发布时间
     */
    private LocalDateTime releaseTime;

    /**
     * 已读标记
     */
    private String readFlag;

    /**
     * 标星标记
     */
    private String starFlag;
}
