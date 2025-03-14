import { defineStore } from "pinia";
import { login, logout} from "@/api/system/login/Login.ts";
import { saveTheme, updatePassword} from "@/api/system/profile/Profile.ts";
import token from "@/utils/Token.ts";
import { message } from "ant-design-vue";
import {queryAuthInfo} from "@/api/system/auth/Auth.ts";
import {ResponseError, type ResponseType} from "@/api/global/Type.ts";
import type {AvatarType} from "@/api/system/profile/type/SysProfile.ts";
import type { AuthInfoType, UserInfoType} from "@/api/system/auth/type/AuthInfoType.ts";
import type {SysRole} from "@/api/system/role/type/SysRole.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import type {SysPost} from "@/api/system/post/type/SysPost.ts";
import type {StarViewType} from "@/api/system/view-tab/type/SysViewTab.ts";
import {close} from "@/utils/ServerSentEvents.ts";
import {rasEncryptPassword} from "@/utils/Crypto.ts";
import {publicAttachmentDownload} from "@/api/system/attachment/AttachmentStorage.ts";
import router from "@/router";
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
        async login(username: string, password: string, captchaVerification: string): Promise<ResponseType<string>> {
            // 对密码进行加密处理，获取密文和requestKey
            const { ciphertext, requestKey} = await rasEncryptPassword(password)
            // 执行登录
            const resp = await login(username, ciphertext, captchaVerification, requestKey)
            if (resp.code === 200) {
                // 登录成功后保存 token
                setToken(resp.data);
                return resp;
            } else {
                // 登录失败，则抛出异常并处理
                throw new ResponseError(resp.code, resp.msg);
            }
        },
        // 修改密码
        async updatePassword(oldPassword: string, newPassword: string, confirmPassword: string): Promise<ResponseType<string>> {
            return new Promise(async (resolve, reject) => {
                try {
                    // 对旧密码进行加密处理
                    const oldPasswordEncrypt = await rasEncryptPassword(oldPassword)
                    // 对新密码进行加密处理
                    const newPasswordEncrypt = await rasEncryptPassword(newPassword)
                    // 对确认密码进行加密处理
                    const confirmPasswordEncrypt = await rasEncryptPassword(confirmPassword)

                    // 更新密码
                    const resp = await updatePassword(
                        oldPasswordEncrypt.ciphertext,
                        oldPasswordEncrypt.requestKey,
                        newPasswordEncrypt.ciphertext,
                        newPasswordEncrypt.requestKey,
                        confirmPasswordEncrypt.ciphertext,
                        confirmPasswordEncrypt.requestKey)
                    if (resp.code === 200) {
                        resolve(resp)
                    } else {
                        reject(new ResponseError(resp.code, resp.msg))
                    }
                } catch (error) {
                    reject(error)
                }
            })
        },
        // 初始化用户信息
        initUserInfo ():Promise<ResponseType<AuthInfoType>> {
            return new Promise((resolve, reject) => {
                queryAuthInfo().then((resp) => {
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
                        reject(new ResponseError(resp.code,resp.msg))
                    }
                }).catch(err => {
                    reject(err)
                })
            })
        },
        // 退出登陆
        async handleLogout() {
            // 关闭 sse 连接
            try {
                await close()
                await logout()
            } catch (e) {
                if (e instanceof ResponseError) {
                    message.error(e.msg)
                } else {
                    console.error(e)
                }
            } finally {
                this.clearUserInfo()
            }
        },
        // 认证失效
        authenticationFailure(msg: string) {
            this.clearUserInfo()
            // 路由跳转到登录页
            router.push({name: 'Login', state: {msg: msg}})
            throw msg
        },
        /**
         * 清空用户信息
         */
        clearUserInfo() {
            const userState = this.$state

            // 用户相关赋值
            userState.userInfo = {}
            userState.userId = ''
            userState.nickname = ''
            userState.username = ''
            userState.avatar = this.getDefaultAvatar()

            // 收藏固定菜单赋值
            userState.viewTabs = []

            // 角色权限相关赋值
            userState.roles = []
            userState.roleCodes = []
            userState.permissions = []

            // 部门相关赋值
            userState.deptTrees = []
            userState.defaultDept = {}
            userState.defaultDeptName = ''
            userState.defaultDeptCode = ''

            // 岗位相关赋值
            userState.posts = []
            userState.defaultDeptPosts = []

            removeToken()
        },
        // 更新默认部门
        updateDefaultDept(defaultDept: SysDept) {
            const state = this.$state
            state.defaultDept = defaultDept
            state.defaultDeptName = defaultDept.name ? defaultDept.name : ''
            state.defaultDeptCode = defaultDept.code ? defaultDept.code : ''
            // 更新默认部门后更新部门下岗位
            state.defaultDeptPosts = state.posts.filter(post => post.deptCode === state.defaultDeptCode)
        },
        // 保存主题修改
        saveTheme(themeJson: string) {
            return new Promise((resolve, reject) => {
                if (themeJson !== this.userInfo.theme) {
                    localStorage.setItem('theme', themeJson)
                    saveTheme(themeJson).then(resp => {
                        if (resp.code === 200) {
                            this.userInfo.theme = themeJson
                            message.success("主题已保存")
                            resolve(resp)
                        } else {
                            reject(resp.msg)
                        }
                    }).catch((error) => {
                        reject(error.msg)
                    })
                } else {
                    message.success("主题已保存")
                    resolve("主题已保存")
                }
            })
        },
        // 处理头像
        handleAvatar() {
            const avatar = this.$state.avatar
            if (avatar.type === 'image') {
                // 当头像类型为 image 但 image不存在时，赋值默认头像
                if (avatar.value) {
                    publicAttachmentDownload(avatar.value).then((resp: Blob) => {
                        avatar.url = URL.createObjectURL(resp)
                    }).catch((e) => {
                        if (e instanceof ResponseError) {
                            message.error(e.msg)
                        } else {
                            console.error(e)
                        }
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
    }
})
