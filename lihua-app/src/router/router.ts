import { Router } from 'sard-uniapp'
import { getToken } from '@/helpers/token'
import { useUserStore } from '@/stores/user'
import { useNoticeStore } from '@/stores/notice'
import { webSocket } from '@/utils/web-socket'
import { initDict } from '@/helpers/dict'
const router = new Router()

/**
 * 无需登录即可访问的路由列表
 */
const publicRoutesList = [
	// 首屏页
	"/pages/splash/index",
	// 登录
	"/pages/login/Login",
	// 注册
	"/pages/login/Register",
	// 隐私政策
	"/subpackages/system/protocol/PrivacyPolicy",
	// 用户协议
	"/subpackages/system/protocol/UserAgreement"
	]

/**
 * 路由守卫
 * 返回true或不返回数据正常跳转
 * 返回false阻止跳转
 * 返回Route跳转到指定页面
 */
router.beforeEach((to, from) => {	
	const userStore = useUserStore()
	const noticeStore = useNoticeStore()
	
	if (getToken()) {
		// 用户信息不存在，获取用户信息
		if (!userStore.userId) {
			userStore.initUserInfo()
			// 连接到websocket
			webSocket.connect()
			// 获取最新的未读消息
			noticeStore.getUnreadCount()
			// 缓存通知类型字典
			initDict("sys_notice_type")
		} else {
			return true
		}
	} else {
		// 没有token断开websocket连接
		webSocket.closeConnect()
		// 访问的页面是公开页面，可直接访问
		if (publicRoutesList.includes(to.url)) {
			return true
		}
		
		// 退回登录页
		uni.reLaunch({
			url: "/pages/login/Login"
		})
		return false
	}
})

export default router