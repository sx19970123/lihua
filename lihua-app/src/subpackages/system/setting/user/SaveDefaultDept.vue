<template>
  <view class="content">
	 <sar-card>
		 <sar-input v-model="keyword" root-class="rounded-input keyword-input" placeholder="请输入部门名称" @input="handleSearch"/>
		 <sar-tree
			v-model:current="defaultDeptId"
			default-expand-all
			single-selectable
		 	ref="treeRef"
		 	:data="treeData"
		 	:node-keys="{ title: 'name', key: 'id' }"
			@select="handleChangeDefaultDept"
		 />
	 </sar-card>
  </view>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import type { TreeExpose } from 'sard-uniapp'
import { useUserStore } from '@/stores/user'
import type { SysDept } from '@/api/system/dept/type/sys-dept'
import {traverse} from '@/utils/tree'
import router from '@/router/router';

const userStore = useUserStore()
const keyword = ref<string>('')
const treeRef = ref<TreeExpose>()

/**
 * 默认部门id
 */
const defaultDeptId = ref<string | undefined>(userStore.defaultDept.id)

/**
 * 树形结构数据
 */
const treeData = ref<SysDept[]>(userStore.deptTrees)

/**
 * 处理筛选
 */
const handleSearch = () => {
	treeRef.value?.filter(keyword.value)
}

/**
 * 处理选择默认部门
 */
const handleChangeDefaultDept = (key: string) => {
	// 递归便利树形结构
	traverse(treeData.value, (item: SysDept) => {
		if (item.id === key) {
			uni.showLoading({title: '加载中', mask: true})
			// 更新默认部门
			userStore.updateDefaultDept(item).then(() => {
				router.navigateBack({})
			}).finally(() => {
				uni.hideLoading()
			})
			return true
		}
	})
}
</script>
<style lang="scss">
@import "@/static/style/input.scss";
.keyword-input {
	margin-bottom: 16rpx;
}
</style>