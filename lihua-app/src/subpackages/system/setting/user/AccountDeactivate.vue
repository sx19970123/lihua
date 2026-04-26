<template>
	<view class="content">
		<!-- 密码验证 -->
		<sar-space direction="vertical" size="large" v-if="!isCheck">
			<sar-input root-class="rounded-input" type="password" :focus="autoFocus" placeholder="请输入当前账号密码" v-model="password"></sar-input>
			<sar-button round :loading="checkLoading" @click="handleCheck">下一步</sar-button>
		</sar-space>
		<!-- 注销账号 -->
		<sar-space direction="vertical" size="large" v-else>
			<sar-result status="warning" title="确认注销" description="注销后不可恢复" />
			<sar-button round :loading="deactivateLoading" :disabled="countDownLoading" @click="handleDeactivate" >
				<sar-count-down v-if="countDownLoading" :time="1000 * 5" format="注销账号(s)" @finish="countDownLoading = false"/>
				<view v-else>注销账号</view>
			</sar-button>
		</sar-space>
	</view>
</template>

<script setup lang="ts">
import { ref, nextTick, onMounted} from 'vue';
import {checkPassword, accountDeactivate} from "@/api/system/profile/profile";
import {toast} from '@/utils/toast';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore()
// 自动聚焦
const autoFocus = ref<boolean>(false)
/**
 * 初始化密码校验
 */
const initPasswordCheck = () => {
	// 是否完成了密码校验
	const isCheck = ref<boolean>(false)
	// 校验loading
	const checkLoading = ref<boolean>(false)
	// 密码
	const password = ref<string>()
	// 执行密码校验
	const handleCheck = async () => {
		if (!password.value) {
			toast("请输入密码")
			return
		}
		try {
			checkLoading.value = true
			const resp = await checkPassword(password.value)
			if (resp.code === 200) {
				isCheck.value = resp.data
				if (resp.data) {
					countDownLoading.value = true
				} else {
					toast("校验未通过")
				}
			} else {
				toast(resp.msg)
			}
		} finally {
			checkLoading.value = false
		}
	}
	
	return {
		isCheck,
		checkLoading,
		password,
		handleCheck
	}
}

const {isCheck, checkLoading, password, handleCheck} = initPasswordCheck()

/**
 * 初始化账号注销
 */
const initDeactivate = () => {
	// 账号注销loading
	const deactivateLoading = ref<boolean>(false)
	// 倒计时loading
	const countDownLoading = ref<boolean>(false)
	// 处理注销
	const handleDeactivate = async () => {
		try {
			deactivateLoading.value = true
			const resp = await accountDeactivate()
			if (resp.code === 200) {
				userStore.handleLogout()
			} else {
				toast(resp.msg)
			}
		} finally {
			deactivateLoading.value = false
		}
	}
	
	return {
		deactivateLoading,
		countDownLoading,
		handleDeactivate
	}
}

const {deactivateLoading, countDownLoading, handleDeactivate} = initDeactivate()

onMounted(() => {
	nextTick(() => autoFocus.value = true)
})
</script>

<style lang="scss">
@import "@/static/style/input.scss";
</style>