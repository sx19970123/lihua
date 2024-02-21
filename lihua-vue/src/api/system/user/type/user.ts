import type { RouterType } from "@/api/system/user/type/router"
import type { RoleType } from "@/api/system/user/type/role"
import type { StarViewType } from "@/api/system/star-view/type/starView"
import type { AuthorType } from "@/api/system/user/type/author"

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
    starViewVOList: Array<StarViewType>,
    sysRoleList: Array<RoleType>,
    sysUserVO: SysUserVOType,
    username: string
}

/**
 * store 用户信息
 */
export interface SysUserVOType {
    avatar: Avatar,
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
    avatar: Avatar,
    nickname: string | null,
    gender: string | null,
    email: string | null,
    phoneNumber: string | null
}

/**
 * 用户头像
 */
export interface Avatar {
    // 类型： image/icon/text
    type: string,
    // 头像背景颜色
    backgroundColor: string,
    // 头像值： image地址链接/icon组件名称/text文本
    value: string
}