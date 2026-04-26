<template>
	<view class="content">
		<view class="description">
			图标选择组件初衷是用户头像与web端保持一致，IconSelect 中选中的值，
			与sar-icon 组件使用规则：
			线框、实底为 family="iconName后缀首字母小写" name="iconName"
			双色、自定义 为读取的 src/static/icons/svg 下的svg文件
			</view>
		<view class="model-val">双向绑定：{{value}}</view>
		<IconSelect v-model:value="value" width="686rpx" height="50vh"></IconSelect>
	</view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import IconSelect from '@/components/icon-select/index.vue'
const value = ref<string>()
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