import type { RouterType } from "@/api/system/user/type/router"
import type { RoleType } from "@/api/system/user/type/role"
import type { StarViewType } from "@/api/system/star-view/type/starView"
import type { AuthorType } from "@/api/system/user/type/author"
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
    theme: string | null
}
