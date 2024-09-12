import type {StarViewType} from "@/api/system/view-tab/type/SysViewTab.ts";
import type {SysRole} from "@/api/system/role/type/SysRole.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import type {SysPost} from "@/api/system/post/type/SysPost.ts";

/**
 * 登陆成功后的认证数据信息，包含用户、角色、部门、岗位等所有信息
 */
export interface AuthInfoType {
    // 权限信息（菜单权限编码，角色编码集合）
    permissions: string[],
    // 所有路由信息
    routers: RouterType[],
    // 所有收藏固定的菜单信息
    viewTabs: StarViewType[],
    // 所有角色信息
    roles: SysRole[],
    // 登陆用户信息
    userInfo: UserInfoType,
    // 部门信息
    depts: SysDept[],
    // 默认部门
    defaultDept: SysDept,
    // 岗位信息
    posts: SysPost[],
}

/**
 * store 用户信息
 */
export interface UserInfoType {
    avatar?: string,
    gender?: string,
    id?: string,
    nickname?: string,
    password?: string,
    status?: string,
    username?: string,
    theme?: string,
    email?: string,
    phoneNumber?: string,
    passwordUpdateTime?: Date
}


/**
 * 动态路由相关
 */
export interface RouterType {
    children: Array<RouterType>,
    component: Function | string,
    id: string,
    key: string,
    meta: MetaType,
    name: string,
    parentId: string,
    path: string,
    perms: string,
    query: string | null,
    type: string,
    danger: boolean
}

/**
 * 路由元数据相关
 */
export interface MetaType {
    icon: string,
    label: string,
    link: string | null,
    cache: boolean | null,
    title: string,
    skip: boolean,
    linkOpenType: string,
    visible: boolean
}
