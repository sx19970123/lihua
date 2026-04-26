<template>
	<view class="content">
		<sar-space direction="vertical" size="large">
			<sar-input placeholder="请输入旧密码" v-model="password.oldPassword" :focus="autoFocus" root-class="rounded-input" type="password" show-eye clearable show-clear-only-focus :maxlength="30"></sar-input>
			<password-input placeholder="请输入新密码" v-model:value="password.newPassword"></password-input>
			<sar-input placeholder="请再次输入密码" v-model="password.confirmPassword" root-class="rounded-input" type="password" show-eye clearable show-clear-only-focus :maxlength="30"></sar-input>
			<sar-button round :loading="saveLoading" @click="handleSaveData">保 存</sar-button>
		</sar-space>
	</view>
</template>

<script setup lang="ts">
import {reactive, ref, nextTick, onMounted} from 'vue';
import router from '@/router/router';
import {toast} from '@/utils/toast';
import PasswordInput from '@/components/password-input/index.vue'
import type {passwordType} from "@/api/system/profile/type/password-type";
import {updatePassword} from "@/api/system/profile/profile";

// 自动聚焦
const autoFocus = ref<boolean>(false)

const password = reactive<passwordType>({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const saveLoading = ref<boolean>(false)

const handleSaveData = async () => {
	// 密码校验
	if (!checkPasswordData()) {
		return
	}
	// 保存密码
	try {
		saveLoading.value = true
		const resp = await updatePassword(password)
		if (resp.code === 200) {
			router.navigateBack({})
		}
	} finally {
		saveLoading.value = false
	}
}

// 检查密码完整性
const checkPasswordData =  () => {
	const {oldPassword, newPassword, confirmPassword} = password
	// 密码非空校验
	if (!oldPassword) {
		toast("请输入旧密码")
		return false
	}
	
	// 密码非空校验
	if (!newPassword) {
		toast("请输入新密码")
		return false
	}
	
	// 密码长度校验
	if (!(newPassword.length >= 6 && newPassword.length <= 30)) {
		toast("密码长度6-30位")
		return false
	}
	
	// 二次密码校验
	if (newPassword !== confirmPassword) {
		toast("两次密码不一致")
		return false
	}
	return true
}
onMounted(() => {
	nextTick(() => autoFocus.value = true)
})
</script>
<style lang="scss">
@import "@/static/style/input.scss";
</style>