/**
 * 个人设置用户信息
 */
export interface ProfileInfo {
    avatar?: AvatarType,
    nickname?: string,
    gender?: string,
    email?: string,
    phoneNumber?: string
}

/**
 * 用户头像
 */
export interface AvatarType {
    // 类型： image/icon/text
    type?: string,
    // 头像背景颜色
    backgroundColor?: string,
    // 头像值： image头像全路径/icon组件名称/text文本
    value?: string,
    // 访问头像url
    url?: string
}
