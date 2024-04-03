import type { RouterType } from "@/api/system/profile/type/router"
import type { RoleType } from "@/api/system/profile/type/role"
import type { StarViewType } from "@/api/system/view-tab/type/SysViewTab.ts"
import type { AuthorType } from "@/api/system/profile/type/author"

/**
 * loginUser
 */
export interface UserInfoType {
    accountNonExpired: boolean,
    accountNonLocked: boolean,
    credentialsNonExpired: boolean,
    enabled: boolean,
    password: null,
    authorities: Array<AuthorType>,
    permissionList: Array<AuthorType>,
    routerList: Array<RouterType>,
    viewTabVOList: Array<StarViewType>,
    sysRoleList: Array<RoleType>,
    sysUserVO: SysUserVOType,
    username: string
}

/**
 * store 用户信息
 */
export interface SysUserVOType {
    avatar: string | null,
    createId: string | null,
    createTime: string | null,
    gender: string | null,
    id: string | null,
    loginIp: string | null,
    loginTime: string | null,
    nickname: string | null,
    password: string | null,
    status: string | null,
    updateId: string | null,
    updateTime: string | null,
    username: string | null,
    theme: string | null,
    email: string | null,
    phoneNumber: string | null
}

/**
 * 个人设置用户信息
 */
export interface UserInfo {
    avatar: AvatarType,
    nickname: string | null,
    gender: string | null,
    email: string | null,
    phoneNumber: string | null
}

/**
 * 用户头像
 */
export interface AvatarType {
    // 类型： image/icon/text
    type: string,
    // 头像背景颜色
    backgroundColor: string,
    // 头像值： image地址链接/icon组件名称/text文本
    value: string,
    // 图片地址
    url: string
}
