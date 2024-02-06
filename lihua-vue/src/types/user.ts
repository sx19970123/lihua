import type { RouterType } from "@/types/router"
import type { RoleType } from "@/types/role"
import type { StarViewType } from "@/types/starView"
import type { AuthorType } from "@/types/author"
interface UserInfoType {
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

interface SysUserVOType {
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
    username: string | null
}


export {
    UserInfoType,
    SysUserVOType
}
