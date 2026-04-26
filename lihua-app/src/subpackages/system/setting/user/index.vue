<template>
	<view class="content">
		<sar-space direction="vertical" size="large">
			<sar-list card title="基础设置">
				<sar-list-item title="头像" arrow hover @click="goSaveDataPage('SaveAvatar')">
					<template #value>
						<user-avatar ref="userAvatarRef"/>
					</template>
				</sar-list-item>
				<sar-list-item title="昵称" :value="userStore.userInfo.nickname" arrow hover @click="goSaveDataPage('SaveNickname')"></sar-list-item>
				<sar-list-item title="邮箱" :value="userStore.userInfo.email" arrow hover @click="goSaveDataPage('SaveEmail')"></sar-list-item>
				<sar-list-item title="性别" :value="getDictLabel(user_gender, userStore.userInfo.gender)" arrow hover @click="goSaveDataPage('SaveGender')"></sar-list-item>
				<sar-list-item title="手机号码" :value="userStore.userInfo.phoneNumber" arrow hover @click="goSaveDataPage('SavePhoneNumber')"></sar-list-item>
				<sar-list-item title="数据更新" hover @click="reloadUserInfo"></sar-list-item>
			</sar-list>
			<sar-list card title="权限信息">
				<sar-list-item title="默认部门" :value="userStore.defaultDeptName" arrow hover @click="goSaveDataPage('SaveDefaultDept')"></sar-list-item>
				<sar-list-item title="所属岗位" :value="userStore.defaultDeptPosts.map((post: SysPost) => post.name).join('、')" hover></sar-list-item>
				<sar-list-item title="我的角色" :value="userStore.roles.map((role: SysRole) => role.name).join('、')" hover></sar-list-item>
			</sar-list>
			<sar-list card title="安全设置">
				<sar-list-item title="修改密码" arrow hover @click="goSaveDataPage('SavePassword')"></sar-list-item>
				<sar-list-item title="注销账号" arrow hover @click="goSaveDataPage('AccountDeactivate')"></sar-list-item>
			</sar-list>
		</sar-space>
	</view>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import UserAvatar from '@/components/user-avatar/index.vue'
import router from '@/router/Router'
import {toast} from '@/utils/Toast'
import { reloadData } from '@/api/system/auth/Auth'
import {getDictLabel, initDict} from '@/utils/Dict'
import type {SysPost} from "@/api/system/post/type/SysPost";
import type {SysRole} from "@/api/system/role/type/SysRole";

const { user_gender } = initDict('user_gender')
const userStore = useUserStore()

const userAvatarRef = ref<InstanceType<typeof UserAvatar>>()
/**
 * 前往数据保存页面
 */
const goSaveDataPage = (pageName: string) => {
	const baseURL = "/subpackages/system/setting/user/"
	router.navigateTo({
		url: baseURL + pageName
	})
}

/**
 * 刷新用户信息
 */
const reloadUserInfo = async () => {
	try {
		uni.showLoading({title: '加载中', mask: true})
		await reloadData()
		await userStore.initUserInfo()
		// 刷新头像
		if (userAvatarRef.value) {
			userAvatarRef.value.initAvatar()
		}
		toast("更新完成")
	} finally {
		uni.hideLoading()
	}
}
</script>

<style scoped lang="scss">
:deep(.sar-list__title) {
	margin-top: 16rpx
}
:deep(.sar-list-item__title) {
	min-width: 120rpx;
}
</style>