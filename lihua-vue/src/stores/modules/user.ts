import { defineStore } from "pinia";

import { login } from "@/api/system/login/login";
import { getUserInfo } from "@/api/system/user/user";

import type { ResponseType } from "@/api/type";
import type { UserInfoType } from "@/api/system/user/type/user";

import token from "@/utils/token";

const { setToken } = token
/**
 * 定义 userStore 的用户信息
 */
interface UserStoreType {
    // 用户昵称
    name: string | null,
    // 用户名
    username:string | null,
    // 用户头像
    avatar: string | null,
    // 用户角色编码
    roles: Array<string | null>,
    // 用户权限字符串
    permissions: Array<string | null>,
    // 用户全部信息
    userInfo: object
}


export const useUserStore = defineStore('user', {
    state: ():UserStoreType => {
        const name: string | null = ''
        const username: string = ''
        const avatar: string = ''
        const roles: Array<string> = []
        const permissions: Array<string> = []
        const userInfo:object = {}
        return {
            name,
            avatar,
            username,
            roles,
            permissions,
            userInfo
        }
    },
    actions: {
        // 用户登录
        login(username: string, password: string, code?: string)  {
            return new Promise((resolve, reject) => {
                login(username,password,code).then((resp:ResponseType<string>) => {
                    if (resp.code === 200) {
                        setToken(resp.data)
                    }
                    resolve(resp)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        // 获取用户信息
        getUserInfo ():Promise<ResponseType<UserInfoType>> {
            return new Promise((resolve, reject) => {
                getUserInfo().then((resp: ResponseType<UserInfoType>) => {
                    const suerInfo = resp.data.sysUserVO
                    this.$state.userInfo = suerInfo
                    this.$state.name = suerInfo.nickname
                    this.$state.avatar = suerInfo.avatar
                    this.$state.username = suerInfo.username
                    this.$state.roles = resp.data.sysRoleList.map(role => role.code)
                    this.$state.permissions = resp.data.permissionList.map(permission => permission.authority)
                    resolve(resp)
                }).catch(err => {
                    reject(err)
                })
            })
        }
    },
    getters: {

    }
})
