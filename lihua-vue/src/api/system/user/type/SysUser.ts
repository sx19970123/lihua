interface SysUser {

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
}

interface SysUserVO extends SysUser{
    deptList?: string[]
}

interface SysUserDTO extends SysUser {

    /**
     * 部门id集合
     */
    deptIdList?: string[]

    /**
     * 默认的部门id
     */
    defaultId?: string

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
}