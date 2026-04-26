<template>
	<view>
		<!--头像-->
		<view class="title">
			<sar-space justify="between" align="center">
				<sar-space align="center" size="large">
					<!-- 头像 -->
					<view class="unobstructed" @click="toUserSetting()">
						<user-avatar/>
					</view>
					<sar-space direction="vertical" size="small">
						<!-- 昵称 -->
						<view class="nickname unobstructed" @click="toUserSetting()">{{userStore.$state.nickname}}</view>
						<!-- 部门 -->
						<sar-space align="center" class="dept unobstructed">
							<view @click="toChangeDept()">
								{{userStore.$state.defaultDeptName ? userStore.$state.defaultDeptName : '设置默认部门'}}
							</view>
							<sar-tag plain theme="primary" :root-style="{paddingTop: 0, paddingBottom: 0}" @click="toChangeDept()">
								<sar-icon family="outlined" name="SwapOutlined"/>
							</sar-tag>
						</sar-space>
					</sar-space>
				</sar-space>
				<!-- 通知 -->
				<view style="margin-right: 16rpx;" @click="toNotice">
					<sar-badge :value="noticeStore.unreadCount">
						<sar-icon name="BellOutlined" family="outlined" size="42rpx"></sar-icon>
					</sar-badge>
				</view>
			</sar-space>
		</view>
		<!--操作列表-->
		<view class="setting-content">
			<sar-list card>
				<sar-list-item title="设置" @click="toSetting" icon-family="outlined" icon="SettingOutlined" hover arrow/>
				<sar-list-item title="组件" @click="toComponentList" icon-family="outlined" icon="SkinOutlined" hover arrow/>
				<sar-list-item title="仓库" @click="toGitee" icon-family="custom" icon="GiteeCustom" hover arrow/>
			</sar-list>
		</view>
	</view>
</template>
<script setup lang="ts">
import {useUserStore} from '@/stores/user'
import {useNoticeStore} from "@/stores/notice"
import router from '@/router/router'
import UserAvatar from '@/components/user-avatar/index.vue'
import { watch } from 'vue'

const userStore = useUserStore()
const noticeStore = useNoticeStore()

// 前往gitee
const toGitee = () => {
	router.navigateTo({
		url: "/pages/webview/index?url=" + encodeURIComponent('https://gitee.com/yukino_git/lihua-app')
	})
}

// 前往设置
const toSetting = () => {
	router.navigateTo({
		url: "/subpackages/system/setting/index"
	})
}

// 前往组件列表
const toComponentList = () => {
	router.navigateTo({
		url: '/subpackages/system/components/index'
	})
}

// 前往修改部门
const toChangeDept = () => {
	router.navigateTo({
		url: "/subpackages/system/setting/user/SaveDefaultDept"
	})
}

// 前往用户设置
const toUserSetting = () => {
	router.navigateTo({
		url: "/subpackages/system/setting/user/index"
	})
}

// 前往消息通知
const toNotice = () => {
	router.navigateTo({
		url: "/subpackages/system/notice/index"
	})
}

// 监听tabbar红点
watch(() => noticeStore.unreadCount, (newVal: number) => {
	if (newVal > 0) { 
		uni.showTabBarRedDot({ index: 1 }) 
	} else { 
		uni.hideTabBarRedDot({ index: 1 })
	}
}, {immediate: true})
</script>

<style scoped lang="scss">
.title {
	position: relative;
	overflow: hidden;
	padding: 10vh 16px 16px 16px;
	/* 主背景渐变 + 线条纹理 */
	background-image:
		linear-gradient(to top, #f7f7f8 0%, #f0faff 5%, #99caf9 100%),
		repeating-linear-gradient(90deg, rgba(247,247,248,0.15) 0 20px, rgba(247,247,248,0) 20px 40px),
		repeating-linear-gradient(45deg, rgba(247,247,248,0.08) 0 10px, rgba(247,247,248,0) 10px 20px),
		repeating-linear-gradient(-45deg, rgba(247,247,248,0.08) 0 10px, rgba(247,247,248,0) 10px 20px);

	background-blend-mode: overlay;

	.nickname {
		font-weight: var(--sar-font-bold);
	}
	
	.dept {
		font-size: var(--sar-text-base);
		color:var(--sar-secondary-text);
	}

	/* 底部遮罩 */
	&::after {
		content: "";
		position: absolute;
		left: 0;
		bottom: 0;
		width: 100%;
		height: 80rpx;
		pointer-events: none;
		z-index: 1;
		background: linear-gradient(to top, #f7f7f8 0%, rgba(247,247,248,0) 100%);
	}
	
	.unobstructed {
		position: relative;
		z-index: 2;
	}
}

.setting-content {
	padding: 0 16rpx 16rpx 16rpx;
}

/* 深色模式 */
@media (prefers-color-scheme: dark) {
	.title {
		background-image:
			linear-gradient(to top, #000000 0%, #121529 5%, #3a557f 100%),
			repeating-linear-gradient(90deg, rgba(0,0,0,0.15) 0 20px, rgba(0,0,0,0) 20px 40px),
			repeating-linear-gradient(45deg, rgba(0,0,0,0.08) 0 10px, rgba(0,0,0,0) 10px 20px),
			repeating-linear-gradient(-45deg, rgba(0,0,0,0.08) 0 10px, rgba(0,0,0,0) 10px 20px);
    /* 深色模式底部遮罩 */
    &::after {
		background: linear-gradient(to top, #000000 0%, rgba(0,0,0,0) 100%);
    }
  }
}
</style>
