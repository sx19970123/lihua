interface SysUser {

    /**
     * 主键id
     */
    id?: string;

    /**
     * 用户名
     */
    username: string;

    /**
     * 密码
     */
    password?: string;

    /**
     * 昵称
     */
    nickname: string;

    /**
     * 头像
     */
    avatar?: string;

    /**
     * 性别
     */
    gender: string;

    /**
     * 状态
     */
    status: string;

    /**
     * 主题
     */
    theme?: string;

    /**
     * 登陆ip
     */
    loginIp?: string;

    /**
     * 邮箱
     */
    email?: string;

    /**
     * 手机号码
     */
    phoneNumber?: string;
}