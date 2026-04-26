<script setup lang="ts">
import {watch} from 'vue'
import {onLaunch} from "@dcloudio/uni-app"
import {useThemeStore} from "@/stores/theme"
import {useNoticeStore} from "@/stores/notice"
import {useRootRefStore} from "@/stores/root"
import {webSocket} from '@/utils/web-socket'
import type {NoticeMessage} from '@/api/system/notice/type/notice-message'
import router from "@/router/router"
import { initDict, getDictLabel } from '@/helpers/dict'

// #ifdef APP-PLUS
// 仅app支持原生消息通知
import MessageNotify from '@/utils/message-notify'
// #endif

const themeStore = useThemeStore()
const noticeStore = useNoticeStore()
const rootRefStore = useRootRefStore()

onLaunch(() => {
	// 设置当前主题
	themeStore.setMode()
	// 处理通知初始化
	addNoticeEventListener()
})

// 处理websocket消息通知监听
const addNoticeEventListener = () => {
	// 订阅notice通知消息
	webSocket.addEventListener("WS_NOTICE", (data: NoticeMessage) => {
		// #ifdef APP-PLUS
		// 全局通知推送（仅原生app）
		showNotify(data)
		// #endif
		
		// 重新获取未读消息数量，尝试更新红点
		noticeStore.getUnreadCount().finally(() => noticeStore.setTabbarRedDot())
	})
}

// 全局通知推送（仅原生app）
const showNotify = (data: NoticeMessage) => {
	// 消息/公告标识
	const pngName = data.type === '0' ? 'MessageOutlined.png' : 'NotificationOutlined.png'
	// 获取字典
	const {sys_notice_type} = initDict("sys_notice_type")
	// 全局消息提醒
	MessageNotify.show({title: '收到一条新' + getDictLabel(sys_notice_type.value, data.type), content: data.title, image: '_www/static/notice/' + pngName}, () => {
		// 跳转到详情页
		router.navigateTo({
			url: "/subpackages/system/notice/Detail",
			query: {
				id: data.id,
				title: data.title
			},
			success: () => noticeStore.markAsRead(data.id)
		})
	}, (direction) => {
		// 向下拖动打开抽屉预览，以根节点为媒介，拿到保存到rootStore中的根节点实例，调用通知方法
		if (direction === 'bottom') {
			const ref = rootRefStore.getRootRef()
			if (ref && ref.showNoticeLite) {
				ref.showNoticeLite(data.id)
			}
			// 向下拖动需要手动关闭通知
			MessageNotify.hide()
		}
	})
}

// 监听未读消息变化
watch(() => noticeStore.unreadCount, () => noticeStore.setTabbarRedDot())
</script>

<style lang="scss">
/* 全局样式 */
@import "@/uni.scss";
/* sard-uniapp组件库样式 */
@import 'sard-uniapp/index.scss';
/* sard-uniapp组件库暗色模式 */
@import 'sard-uniapp/dark.scss';
/* 自定义图标 */
@import '@/static/icons/filled.css';
@import '@/static/icons/custom.css';
@import '@/static/icons/outlined.css';

page {
	background-color: var(--sar-body-bg);
}
text {
	color: var(--sar-secondary-color);
}

.content {
	padding: 16rpx;
}
</style>
