export interface SysUser {

    /**
     * 主键id
     */
    id?: string;

    /**
     * 用户名
     */
    username?: string;

    /**
     * 密码
     */
    password?: string;

    /**
     * 昵称
     */
    nickname?: string;

    /**
     * 头像
     */
    avatar?: string;

    /**
     * 性别
     */
    gender?: string;

    /**
     * 状态
     */
    status?: string;

    /**
     * 主题
     */
    theme?: string;

    /**
     * 邮箱
     */
    email?: string;

    /**
     * 手机号码
     */
    phoneNumber?: string;

    /**
     * 备注
     */
    remark?: string;

    /**
     * 密码修改时间
     */
    passwordUpdateTime?: Date;

    /**
     * 注册类型
     */
    registerType?: string;
}

export interface SysUserVO extends SysUser{
    deptList?: string[]

    statusIsNormal?: boolean

    updateStatusLoading?: boolean
}

export interface SysUserDTO extends SysUser {

    /**
     * 部门id集合
     */
    deptIdList?: string[]

    /**
     * 默认的部门id
     */
    defaultDeptId?: string

    /**
     * 角色id集合
     */
    roleIdList?: string[]

    /**
     * 岗位id集合
     */
    postIdList?: string[]

    /**
     * 当前页数
     */
    pageNum?: number

    /**
     * 每页记录数
     */
    pageSize?: number

    /**
     * 创建时间集合
     */
    createTimeList?: Date[]

    /**
     * 密码加密请求key
     */
    passwordRequestKey?: string
}
