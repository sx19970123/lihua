import { defineStore } from "pinia";
import {login, logout} from "@/api/system/login/Login.ts";
import {getAvatar, saveTheme} from "@/api/system/profile/Profile.ts";
import token from "@/utils/Token.ts";
import { message } from "ant-design-vue";
import router from "@/router";
import {getAuthInfo} from "@/api/system/auth/Auth.ts";
import type { ResponseType } from "@/api/global/Type.ts";
import type {AvatarType} from "@/api/system/profile/type/SysProfile.ts";
import type { AuthInfoType, UserInfoType} from "@/api/system/auth/type/AuthInfoType.ts";
import type {SysRole} from "@/api/system/role/type/SysRole.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import type {SysPost} from "@/api/system/post/type/SysPost.ts";
import type {StarViewType} from "@/api/system/view-tab/type/SysViewTab.ts";
import {close} from "@/utils/ServerSentEvents.ts";


const { setToken,removeToken } = token

export const useUserStore = defineStore('user', {

    state: () => {
        // 用户相关数据
        const userInfo: UserInfoType = {}
        const userId: string = ''
        const nickname: string = ''
        const username: string = ''
        const avatar: AvatarType = {}

        // 用户收藏固定的菜单数据
        const viewTabs: StarViewType[] = []

        // 角色权限相关数据
        const roles: SysRole[] = []
        const roleCodes: string[] = []
        const permissions: string[] = []
        // 部门相关数据
        const deptTrees:SysDept[] = []
        const defaultDept: SysDept = {}
        const defaultDeptName: string = ''
        const defaultDeptCode: string = ''
        // 岗位相关数据
        const posts: SysPost[] = []
        const defaultDeptPosts: SysPost[] = []
        return {
            userInfo,
            userId,
            nickname,
            username,
            avatar,
            viewTabs,
            roles,
            roleCodes,
            permissions,
            deptTrees,
            defaultDept,
            defaultDeptName,
            defaultDeptCode,
            posts,
            defaultDeptPosts
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
        getUserInfo ():Promise<ResponseType<AuthInfoType>> {
            return new Promise((resolve, reject) => {
                getAuthInfo().then((resp) => {
                    if (resp.code === 200) {
                        const data = resp.data
                        const state = this.$state

                        // 用户相关赋值
                        state.userInfo = data.userInfo
                        state.userId = data.userInfo.id ? data.userInfo.id : ''
                        state.nickname = data.userInfo.nickname ? data.userInfo.nickname : ''
                        state.username = data.userInfo.username ? data.userInfo.username : ''
                        state.avatar = data.userInfo.avatar ? JSON.parse(data.userInfo.avatar) : this.getDefaultAvatar()

                        // 收藏固定菜单赋值
                        state.viewTabs = data.viewTabs

                        // 角色权限相关赋值
                        state.roles = data.roles
                        state.roleCodes = data.roles.filter(role => role.code).map(role => role.code) as string[]
                        state.permissions = data.permissions

                        // 部门相关赋值
                        state.deptTrees = data.depts
                        state.defaultDept = data.defaultDept
                        state.defaultDeptName = data.defaultDept.name ? data.defaultDept.name : ''
                        state.defaultDeptCode = data.defaultDept.code ? data.defaultDept.code : ''

                        // 岗位相关赋值
                        state.posts = data.posts
                        state.defaultDeptPosts = data.posts.filter(post => post.deptCode === state.defaultDeptCode)

                        // 处理头像
                        this.handleAvatar()
                        resolve(resp)
                    } else {
                        message.error(resp.msg)
                        reject(resp.msg)
                    }
                }).catch(err => {
                    reject(err)
                })
            })
        },
        // 退出登陆
        async handleLogout() {
            // 关闭 sse 连接
            await close()
            await logout()
            this.clearUserInfo()
        },
        /**
         * 清空用户信息并重定向到登录页面
         * @param msg
         */
        clearUserInfo(msg?: string) {
            const state = this.$state

            // 用户相关赋值
            state.userInfo = {}
            state.userId = ''
            state.nickname = ''
            state.username = ''
            state.avatar = this.getDefaultAvatar()

            // 收藏固定菜单赋值
            state.viewTabs = []

            // 角色权限相关赋值
            state.roles = []
            state.roleCodes = []
            state.permissions = []

            // 部门相关赋值
            state.deptTrees = []
            state.defaultDept = {}
            state.defaultDeptName = ''
            state.defaultDeptCode = ''

            // 岗位相关赋值
            state.posts = []
            state.defaultDeptPosts = []

            removeToken()
            router.push({name: "Login", state: {msg: msg}})
        },
        // 登录成功后检查是否需要进行相关配置
        checkLoginAfter(): string[] {
            const needSettingComponentNames = []

            // 默认部门不存在，需要选择部门
            if (!this.$state.defaultDept.code) {
                needSettingComponentNames.push("LoginSettingDefaultDept")
            }

            return needSettingComponentNames;
        },
        // 更新默认部门
        updateDefaultDept(defaultDept: SysDept) {
            this.$state.defaultDept = defaultDept
            this.$state.defaultDeptName = defaultDept.name ? defaultDept.name : ''
            this.$state.defaultDeptCode = defaultDept.code ? defaultDept.code : ''
        },
        // 保存主题修改
        saveTheme(themeJson: string) {
            if (themeJson !== this.userInfo.theme) {
                saveTheme(themeJson).then(resp => {
                    if (resp.code === 200) {
                        this.userInfo.theme = themeJson
                         message.success("主题已保存")
                    }
                })
            } else {
                message.success("主题已保存")
            }
        },
        // 处理头像
        handleAvatar() {
            const avatar = this.$state.avatar
            if (avatar.type === 'image') {
                // 当头像类型为 image 但 image不存在时，赋值默认头像
                if (avatar.value) {
                    getAvatar(avatar.value).then((resp: Blob) => {
                        avatar.url = URL.createObjectURL(resp)
                    }).catch(() => {
                        this.$state.avatar = this.getDefaultAvatar()
                    })
                } else {
                    this.$state.avatar = this.getDefaultAvatar()
                }
            }
        },
        // 默认头像
        getDefaultAvatar() {
            return {type: 'text', backgroundColor: 'rgb(191, 191, 191)', value: this.$state.nickname, url: ''}
        }
    },
    getters: {

    }
})
