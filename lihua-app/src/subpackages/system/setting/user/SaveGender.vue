<template>
  <view class="content">
	  <sar-radio-group v-model="gender" @change="handleSaveData">
	    <template #custom="{toggle, value}">
	      <sar-list card>
			<!-- 选项列表 -->
	        <sar-list-item v-for="item in user_gender" :key="item.value" :title="item.label" hover @click="toggle(item.value)">
	          <template #value>
				<!-- 选中标识 -->
	            <sar-icon v-if="item.value === value" color="var(--sar-primary)" family="outlined" size="32rpx" name="CheckOutlined"/>
	          </template>
	        </sar-list-item>
	      </sar-list>
	    </template>
	  </sar-radio-group>
  </view>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import { useUserStore } from '@/stores/user';
import router from '@/router/router';
import {saveBasics} from '@/api/system/profile/profile';
import {toast} from '@/utils/toast';
import {initDict} from '@/helpers/dict'
const { user_gender } = initDict('user_gender')
const userStore = useUserStore()
const gender = ref<string | undefined>(userStore.userInfo.gender)

const handleSaveData = async () => {
	// 修改逻辑
	try {
		uni.showLoading({title: '加载中', mask: true})
		const resp = await saveBasics({gender: gender.value})
		if (resp.code === 200) {
			// 刷新store
			await userStore.initUserInfo()
			router.navigateBack({})
		} else {
			toast(resp.msg)
		}
	} finally {
		uni.hideLoading()
	}
}

</script>