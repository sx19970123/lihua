import { defineStore } from "pinia";
import type { AuthInfoType, UserInfoType } from "@/api/system/profile/type/auth-info-type";
import type { SysRole } from "@/api/system/role/type/sys-role";
import type { SysDept } from "@/api/system/dept/type/sys-dept";
import type { SysPost } from "@/api/system/post/type/sys-post";
import type { AvatarType } from "@/api/system/profile/type/avatar-type";
import { logout } from "@/api/system/authentication/authentication";
import { removeToken } from "@/helpers/token";
import { queryAuthInfo } from "@/api/system/profile/profile";
import {ResponseError, type ResponseType} from "@/api/global/type";
import {attachmentUrl, getFileTempPath} from "@/utils/attachment/attachment-utils";
import { setDefaultDept } from "@/api/system/profile/profile";
import { webSocket } from '@/utils/web-socket'

export const useUserStore = defineStore('user', {
	state: () => {
		// 用户相关数据
		const userInfo: UserInfoType = {}
		const userId: string = ''
		const nickname: string = ''
		const username: string = ''
		const avatar: AvatarType = {}

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
		/**
		 * 退出登录
		 */
		async handleLogout() {
			try {
				await logout()
			} finally {
				this.authenticationFailure()
			}
		},
		/**
		 * 认证失效
		 */
		authenticationFailure() {
			removeToken()
			this.clearUserInfo()
			webSocket.closeConnect()
			uni.reLaunch({
				url: "/pages/login/Login"
			})
		},
		/**
		 * 初始化用户信息
		 */
		initUserInfo(): Promise<ResponseType<AuthInfoType>> {
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
		/**
		 * 更新默认部门
		 */
		async updateDefaultDept(defaultDept: SysDept) {
			return new Promise(async (resolve, reject) => {
				if (defaultDept.id) {
					try {
						const resp = await setDefaultDept(defaultDept.id)
						if (resp.code === 200) {
							const state = this.$state
							state.defaultDept = defaultDept
							state.defaultDeptName = defaultDept.name ? defaultDept.name : ''
							state.defaultDeptCode = defaultDept.code ? defaultDept.code : ''
							// 更新默认部门后更新部门下岗位
							state.defaultDeptPosts = state.posts.filter(post => post.deptCode === state.defaultDeptCode)
							resolve(resp)
						} else {
							reject(new ResponseError(resp.code,resp.msg))
						}
					} catch(err) {
						console.error(err)
						reject(err)
					}
				} else {
					reject()
				}
			})
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
		},
		/**
		 * 处理头像
		 */
		async handleAvatar() {
			const avatar = this.$state.avatar
			if (avatar.type === 'image') {
				// 当头像类型为 image 但 image不存在时，赋值默认头像
				if (avatar.value) {
					avatar.url = await getFileTempPath(attachmentUrl(avatar.value))
				} else {
					this.$state.avatar = this.getDefaultAvatar()
				}
			}
		},
		/**
		 * 默认头像
		 */
		getDefaultAvatar() {
			return {type: 'text', backgroundColor: 'rgb(191, 191, 191)', value: 'lihua', url: ''}
		}
	}
})