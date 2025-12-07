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
     * 发送的用户范围
     */
    userScope?: string;

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

    /**
     * 创建时间
     */
    createTime?: Date;

    /**
     * 创建人id
     */
    createId?:string
}

export interface SysNoticeVO extends SysNotice {
    /**
     * 指定用户
     */
    userIdList?: string[];

    /**
     * 创建用户
     */
    createUser?: string;

    /**
     * 发布用户
     */
    releaseUser?: string;
}

export interface SysNoticeDTO extends SysNotice {
    /**
     * 指定用户
     */
    userIdList?: string[];

    /**
     * 当前页数
     */
    pageNum: number;

    /**
     * 每页记录数
     */
    pageSize: number;

    /**
     * 是否查询star数据
     */
    star?: '1'
}
