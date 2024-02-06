import { defineStore } from "pinia";

import { login } from "@/api/system/login";
import { getUserInfo } from "@/api/system/user";

import type { ResponseType } from "@/types/globalType";
import type {UserInfoType} from "@/types/user";

import token from "@/utils/token";

const { setToken } = token
/**
 * 定义 userStore 的用户信息
 */
interface UserStoreType {
    // 用户昵称
    name: string,
    // 用户名
    username:string,
    // 用户头像
    avatar: string,
    // 用户角色编码
    roles: Array<string>,
    // 用户权限字符串
    permissions: Array<string>,
    // 用户全部信息
    userInfo: object
}


export const useUserStore = defineStore('user', {
    state: ():UserStoreType => {
        const name: string = ''
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
                login(username,password,code).then((resp: ResponseType<string>) => {
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
        getUserInfo () {
            return new Promise((resolve, reject) => {
                getUserInfo().then((resp: ResponseType<UserInfoType>) => {
                    this.$state.userInfo = resp.data
                    this.$state.name = resp.data.name
                    this.$state.avatar = resp.data.avatar
                    this.$state.username = resp.data.username
                    this.$state.roles = resp.data.roles
                    this.$state.permissions = resp.data.permissions
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
