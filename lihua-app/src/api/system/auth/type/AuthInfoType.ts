import type {SysRole} from "@/api/system/role/type/SysRole";
import type {SysDept} from "@/api/system/dept/type/SysDept";
import type {SysPost} from "@/api/system/post/type/SysPost";

/**
 * 登陆成功后的认证数据信息，包含用户、角色、部门、岗位等所有信息
 */
export interface AuthInfoType {
    // 权限信息（菜单权限编码，角色编码集合）
    permissions: string[],
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

