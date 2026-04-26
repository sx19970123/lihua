<template>
	<view class="content">
		<sar-space direction="vertical" size="large">
			<view>
				此组件调用了 plus.nativeObj.view 进行绘制，可全局调用无视路由切换。
				仅适用于原生app（android、ios）。小程序暂无替代方案，多端开发使用时需进行条件编译。
			</view>
			<sar-space direction="vertical">
				<view class="title">基础使用</view>
				<sar-button @click="show('测试通知', '测试内容')"> 显示通知</sar-button>
			</sar-space>
			
			<sar-space direction="vertical">
				<view class="title">自定义消失时间(10s)</view>
				<sar-button @click="show('测试通知', '测试内容', undefind, 10000)"> 显示通知</sar-button>
			</sar-space>
			
			<sar-space direction="vertical">
				<view class="title">自定义图片</view>
				<sar-button @click="show('测试通知', '测试内容', '_www/static/logo.png')"> 显示通知</sar-button>
			</sar-space>
			
			<sar-space direction="vertical">
				<view class="title">超长文本</view>
				<sar-button @click="show('这是一段很长的标题这是一段很长的标题这是一段很长的标题这是一段很长的标题', '这是一段很长的内容这是一段很长的内容这是一段很长的内容这是一段很长的内容这是一段很长的内容这是一段很长的内容')"> 显示通知</sar-button>
			</sar-space>
			
			<sar-space direction="vertical">
				<view class="title">回调函数</view>
				<sar-button @click="callbackShow"> 显示通知</sar-button>
			</sar-space>
			<sar-button @click="MessageNotify.hide()"> 隐藏通知</sar-button>
		</sar-space>
		
	</view>
</template>

<script setup lang="ts">
import MessageNotify from '@/utils/MessageNotify'
import {toast} from '@/utils/Toast'

const show = (title: string, content: string, image?: string, duration?: number) => {
	MessageNotify.show({title: title, content: content, duration: duration, image: image})
}

const callbackShow = () => {
	MessageNotify.show({content: "测试内容"}, () => {
		toast("点击了通知")
	}, (direction) => {
		toast("滑动方向" + direction)
	})
}
</script>


<style scoped lang="scss">
.title {
	font-size: var(--sar-text-lg);
	font-weight: var(--sar-font-bold);
}
.model-val {
	max-width: 100vw;
	overflow-wrap: break-word;
	font-size: var(--sar-text-sm);
}
.description {
	font-size: var(--sar-text-sm);
	color: var(--sar-secondary-color);
}
</style>