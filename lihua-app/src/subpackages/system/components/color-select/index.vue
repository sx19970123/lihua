<template>
	<view class="content">
		<sar-space direction="vertical">
			<view class="model-val">双向绑定颜色：{{color}}</view>
			<view class="model-val">双向绑定Key：{{value}}</view>
			<!-- 头像背景颜色 -->
			<color-select :dataSource="colorSource" v-model:color="color" v-model:value="value"></color-select>
		</sar-space>
	</view>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import ColorSelect from '@/components/color-select/index.vue'
// 头像背景颜色
const colorSource = [
	{
		name: '拂晓蓝',
		color: 'rgb(22, 119, 255)',
		key: '1'
	},
	{
		name: '薄暮',
		color: 'rgb(245, 34, 45)',
		key: '2'
	},
	{
		name: '火山',
		color: 'rgb(250, 84, 28)',
		key: '3'
	},
	{
		name: '日暮',
		color: 'rgb(250, 173, 20)',
		key: '4'
	},
	{
		name: '明青',
		color: 'rgb(19, 194, 194)',
		key: '5'
	},
	{
		name: '极光绿',
		color: 'rgb(82, 196, 26)',
		key: '6'
	},
	{
		name: '极客蓝',
		color: 'rgb(47, 84, 235)',
		key: '7'
	},
	{
		name: '酱紫',
		color: 'rgb(114, 46, 209)',
		key: '8'
	}
]

const color = ref<string>('rgb(114, 46, 209)')
const value = ref<string>('8')
</script>

<style scoped lang="scss">
.model-val {
	max-width: 100vw;
	overflow-wrap: break-word;
	font-size: var(--sar-text-sm);
}
</style>