export interface SysNotice {
    /**
     * 主键 id
     */
    id?: string;

    /**
     * 标题
     */
    title?: string;

    /**
     * 类型
     */
    type?: string;

    /**
     * 状态： 未发布 0 / 已发布 1 / 已撤销 2
     */
    status?: string;

    /**
     * 优先级
     */
    priority?: string;

    /**
     * 是否发送给全部用户
     */
    allUser?: string;

    /**
     * 内容
     */
    content?: string;

    /**
     * 发布时间
     */
    releaseTime?: Date;

    /**
     * 逻辑删除标识
     */
    delFlag?: string;

    /**
     * 备注
     */
    remark?: string;
}

export interface SysNoticeVO extends SysNotice {
    /**
     * 指定用户
     */
    userIdList: string[]
}

export interface SysNoticeDTO extends SysNotice {
    /**
     * 指定用户
     */
    userIdList: string[];

    /**
     * 当前页数
     */
    pageNum: number;

    /**
     * 每页记录数
     */
    pageSize: number;
}