export interface SysLog {
    /**
     * 日志主键
     */
    id?: string;

    /**
     * 业务描述
     */
    description?: string;

    /**
     * 业务类型编码
     */
    typeCode?: string;

    /**
     * 业务类型描述
     */
    typeMsg?: string;

    /**
     * 包类名
     */
    className?: string;

    /**
     * 方法名
     */
    methodName?: string;

    /**
     * IP地址
     */
    ipAddress?: string;

    /**
     * 创建人（操作人）
     */
    createId?: string;

    /**
     * 创建人姓名
     */
    createName?: string;

    /**
     * 用户名
     */
    username?: string;

    /**
     * 创建时间（操作时间）
     */
    createTime?: Date;

    /**
     * 执行耗时
     */
    executeTime?: number;

    /**
     * 请求参数
     */
    params?: string;

    /**
     * 请求返回
     */
    result?: string;

    /**
     * 异常描述
     */
    errorMsg?: string;

    /**
     * 异常堆栈信息
     */
    errorStack?: string;

    /**
     * 请求URL
     */
    url?: string;

    /**
     * 用户代理字符串
     */
    userAgent?: string;

    /**
     * 删除标识
     */
    delFlag?: string;

    /**
     * 日志执行状态
     */
    executeStatus?: string;

    /**
     * 缓存key
     */
    cacheKey?: string;
}

export interface SysLogDTO extends SysLog {

    /**
     * 创建时间集合
     */
    createTimeList?: Date[];

    /**
     * 当前页数
     */
    pageNum: number;

    /**
     * 每页记录数
     */
    pageSize: number;

}
