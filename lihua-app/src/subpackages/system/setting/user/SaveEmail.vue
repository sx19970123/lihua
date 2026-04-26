<template>
	<view class="content">
		<sar-space direction="vertical" size="large">
			<sar-input placeholder="请输入邮箱" v-model="email" root-class="rounded-input" clearable :focus="autoFocus" show-clear-only-focus></sar-input>
			<sar-button round :loading="saveLoading" @click="handleSaveData">保 存</sar-button>
		</sar-space>
	</view>
</template>

<script setup lang="ts">
import { ref, nextTick, onMounted} from 'vue';
import { useUserStore } from '@/stores/user';
import router from '@/router/Router';
import {saveBasics} from '@/api/system/profile/Profile';
import {toast} from '@/utils/Toast';
import { ResponseError } from '@/api/global/Type';

const userStore = useUserStore()
const email = ref<string | undefined>(userStore.userInfo.email)
const saveLoading = ref<boolean>(false)
// 自动聚焦
const autoFocus = ref<boolean>(false)
const handleSaveData = async () => {
	// 输入校验
	if (email.value && !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email.value)) {
		toast("请输入正确的邮箱")
		return
	}
	// 值未修改
	if (email.value === userStore.userInfo.email) {
		router.navigateBack({})
		return
	}
	// 修改逻辑
	try {
		saveLoading.value = true
		const resp = await saveBasics({email: email.value})
		if (resp.code === 200) {
			// 刷新store
			await userStore.initUserInfo()
			router.navigateBack({})
		} else {
			toast(resp.msg)
		}
	} finally {
		saveLoading.value = false
	}
}
onMounted(() => {
	nextTick(() => autoFocus.value = true)
})
</script>
<style lang="scss">
@import "@/static/style/input.scss";
</style>