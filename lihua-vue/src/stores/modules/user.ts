import { defineStore } from "pinia";

import { login } from "@/api/system/login";
import { getUserInfo } from "@/api/system/user";

import token from "@/utils/token";
const {getToken,setToken,removeToken} = token
/**
 * 定义 userStore 的用户信息
 */
interface UserStoreType {
    // 用户token
    token: string,
    // 用户昵称
    name: string,
    // 用户头像
    avatar: string,
    // 用户角色
    roles: Array<string>,
    // 用户权限
    permissions: Array<string>,
    // 用户全部信息
    userInfo: object
}


export const useUserStore = defineStore('user', {
    state: ():UserStoreType => {
        return {
            token: '',
            name: '',
            avatar: '',
            roles: [],
            permissions: [],
            userInfo: {}
        }
    },
    actions: {
        // 用户登录
        login(username: string, password: string, code?: string)  {
            return new Promise((resolve, reject) => {
                login(username,password,code).then(resp => {
                    const token = resp.data
                    setToken(token)
                    this.$state.token = token
                    resolve(resp)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        // 获取用户信息
        getUserInfo () {
            getUserInfo().then(resp => {
                this.$state.userInfo = resp.data
            })
        }
    },
    getters: {

    }
})
