<template>
	<sar-space>
		<view v-for="item in data.dataSource" :key="item.key || item.color" class="color-item"
			:style="{ backgroundColor: item.color }" @click="selectedColor(item)">
			<!-- 选中图标 -->
			<sar-icon family="outlined" name="CheckOutlined" v-if="isSelected(item)" class="check-icon" size="var(--sar-text-xl)" color="#fff"/>
		</view>
	</sar-space>
</template>

<script setup lang="ts">
type ColorSelectItem = {
	name : string
	color : string
	key ?: string
}

const data = defineProps<{
	dataSource : Array<ColorSelectItem>,
	color ?: string,
	value ?: string,
}>()

const emits = defineEmits(['update:color', 'update:value', 'click'])

/** 判断是否选中 */
const isSelected = (item : ColorSelectItem) => {
	if (data.color) {
		return item.color === data.color
	}
	if (data.value) {
		return item.key === data.value
	}
	return false
}

const selectedColor = ({ color, name, key } : ColorSelectItem) => {
	emits('update:color', color)
	emits('update:value', key)
	emits('click', { color, name, key })
}
</script>

<style scoped>
.color-item {
	width: var(--sar-text-2xl);
	height: var(--sar-text-2xl);
	border-radius: var(--sar-rounded-lg);
	display: flex;
	justify-content: center;
	align-items: center;
}
</style>