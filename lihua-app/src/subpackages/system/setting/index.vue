<template>
	<view class="content">
		<sar-space direction="vertical" size="large">
			<sar-list card>
				<sar-list-item title="用户设置" icon-family="outlined" icon="UserOutlined" arrow hover @click="toSettingPage('user/index')"></sar-list-item>		
			</sar-list>
			<!-- #ifdef APP -->
			<!-- 仅app支持手动切换主题 -->
			<sar-list card>	
				<sar-list-item title="系统主题" icon-family="outlined" icon="BgColorsOutlined" arrow hover @click="toSettingPage('theme/index')"></sar-list-item>
			</sar-list>
			<!-- #endif -->	
			<sar-list card>
				<sar-list-item title="用户协议" icon-family="outlined" icon="ExceptionOutlined" arrow hover @click="goProtocol('UserAgreement')"></sar-list-item>
				<sar-list-item title="隐私政策" icon-family="outlined" icon="FileProtectOutlined" arrow hover @click="goProtocol('PrivacyPolicy')"></sar-list-item>
			</sar-list>
		</sar-space>
		<view class="logout-btn">
			<sar-button type="pale" theme="danger" round @click="handleLogout">退出登录</sar-button>
		</view>
	</view>
</template>

<script lang="ts" setup>
import router from '@/router/router'
import { dialog } from 'sard-uniapp'
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()

// 跳转到不同的配置页面
const toSettingPage = (pageName: string) => {
	const baseURL = "/subpackages/system/setting/"
	router.navigateTo({
		url: baseURL + pageName
	})
}

// 前往隐私政策
const goProtocol = (pageName: string) => {
	router.navigateTo({
		url: "/subpackages/system/protocol/" + pageName,
	})
}

// 处理退出登录
const handleLogout = () => {
	dialog.confirm({
		message: "是否退出登录？",
		buttonType: 'round',
		onConfirm: () => {
			userStore.handleLogout()
		}
	})
}
</script>
<style scoped lang="scss">
.logout-btn {
	position: absolute;
	left: 16rpx;
	right: 16rpx;
	bottom: 48rpx;
}
</style>