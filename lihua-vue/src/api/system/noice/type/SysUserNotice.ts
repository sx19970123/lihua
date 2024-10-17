export interface SysUserNoticeVO {
    /**
     * 公告id
     */
    noticeId?: string;
    /**
     * 公告标题
     */
    title?: string;
    /**
     * 公告类型
     */
    type?: string;
    /**
     * 发布用户
     */
    releaseUser?: string;
    /**
     * 公告优先级
     */
    priority?: string;
    /**
     * 发布时间
     */
    releaseTime?: Date;
    /**
     * 已读标记
     */
    readFlag?: string;
    /**
     * 标星标记
     */
    starFlag?: string;

    /**
     * 标星标记（number类型）
     */
    starFlagNumber?: number;
}
