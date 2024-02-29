import { defineStore } from "pinia";

import {login, logout} from "@/api/system/login/login";
import {getUserInfo, saveTheme} from "@/api/system/profile/profile";

import type { ResponseType } from "@/api/type";
import type {AvatarType, SysUserVOType, UserInfoType} from "@/api/system/profile/type/user";

import token from "@/utils/token";
import { message } from "ant-design-vue";
import { h } from "vue";
import { BgColorsOutlined } from "@ant-design/icons-vue";
import router from "@/router";
import {imagePreview} from "@/api/system/file/file";

const { setToken,removeToken } = token
/**
 * 定义 userStore 的用户信息
 */
interface UserStoreType {
    // 用户昵称
    name: string | null,
    // 用户名
    username:string | null,
    // 用户头像
    avatar: AvatarType,
    // 用户角色编码
    roles: Array<string | null>,
    // 用户权限字符串
    permissions: Array<string | null>,
    // 用户全部信息
    userInfo: SysUserVOType
}


export const useUserStore = defineStore('user', {
    state: ():UserStoreType => {
        const name: string | null = ''
        const username: string = ''
        const avatar: AvatarType = {type: '', backgroundColor: '', value: '', url: ''}
        const roles: Array<string> = []
        const permissions: Array<string> = []
        const userInfo:SysUserVOType = {
            avatar: null,
            createId: null,
            createTime: null,
            gender: null,
            id: null,
            loginIp: null,
            loginTime: null,
            nickname: null,
            password: null,
            status: null,
            updateId: null,
            updateTime: null,
            username: null,
            theme: null,
            email: null,
            phoneNumber: null
        }
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
        login(username: string, password: string, captchaVerification: string)  {
            return new Promise((resolve, reject) => {
                login(username,password,captchaVerification).then((resp:ResponseType<string>) => {
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
                    if (resp.code === 200) {
                        const userInfo = resp.data.sysUserVO
                        this.$state.userInfo = userInfo
                        this.$state.name = userInfo.nickname
                        this.$state.avatar = userInfo.avatar ? JSON.parse(userInfo.avatar) : {type: 'text', backgroundColor: 'rgb(191, 191, 191)', value: userInfo.nickname, url: ''}
                        this.$state.username = userInfo.username
                        this.$state.roles = resp.data.sysRoleList.map(role => role.code)
                        this.$state.permissions = resp.data.permissionList.map(permission => permission.authority)
                        this.handleAvatar()
                        resolve(resp)
                    } else {
                        reject(resp.msg)
                    }
                }).catch(err => {
                    reject(err)
                })
            })
        },
        // 退出登陆
        logout() {
            logout().then(resp => {
                this.clearUserInfo()
            })
        },
        // 清空用户信息
        clearUserInfo() {
            this.$state.name = ''
            this.$state.username = ''
            this.$state.avatar = {type: 'text', backgroundColor: 'rgb(191, 191, 191)', value: '', url: ''}
            this.$state.roles = []
            this.$state.permissions = []
            this.$state. userInfo = {
                avatar: null,
                createId: null,
                createTime: null,
                gender: null,
                id: null,
                loginIp: null,
                loginTime: null,
                nickname: null,
                password: null,
                status: null,
                updateId: null,
                updateTime: null,
                username: null,
                theme: null,
                phoneNumber: null,
                email: null
            }
            removeToken()
            router.push("/login")
        },
        // 保存主题修改
        saveTheme(themeJson: string) {
            if (themeJson !== this.userInfo.theme) {
                saveTheme(themeJson).then(resp => {
                    if (resp.code === 200) {
                        this.userInfo.theme = themeJson
                        message.success({
                            content: () => '样式已保存',
                            icon: () => h( BgColorsOutlined ),
                        })
                    }
                })
            } else {
                message.success({
                    content: () => '样式已保存',
                    icon: () => h( BgColorsOutlined ),
                })
            }
        },
        // 处理头像
        handleAvatar() {
            const avatar = this.$state.avatar
            if (avatar.type === 'image') {
                imagePreview(avatar.value).then(resp => {
                    avatar.url = URL.createObjectURL(resp)
                })
            }
        }
    },
    getters: {

    }
})
