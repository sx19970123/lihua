<template>
	<view :style="{maxWidth: props.width}">
		<!-- 分段 -->
		<sar-segmented v-model="currentSegmented" :options="options"/>
		<!-- 图标 -->
		<scroll-view scroll-y :style="{maxHeight: props.height}" style="margin-top: 16rpx;">
			<!-- 线框 -->
			<sar-space wrap v-if="currentSegmented === 'Outlined'">
				<view v-for="iconName in outlinedIcons" :key="iconName" class="icon-item-content" :class="{'icon-item-content-active': props.value === iconName}" @click="handleChickIcon(iconName)">
					<sar-icon family="outlined" :name="iconName" :size="iconSize" :color="currentTheme === 'dark' ? '#fff' : '#000'"></sar-icon>
				</view>
			</sar-space>
			<!-- 实底 -->
			<sar-space wrap v-if="currentSegmented === 'Filled'">
				<view v-for="iconName in filledIcons" :key="iconName" class="icon-item-content" :class="{'icon-item-content-active': props.value === iconName}" @click="handleChickIcon(iconName)">
					<sar-icon family="filled" :name="iconName" :size="iconSize" :color="currentTheme === 'dark' ? '#fff' : '#000'"></sar-icon>
				</view>
			</sar-space>
			<!-- 双色 -->
			<sar-space wrap v-if="currentSegmented === 'TwoTone'">
				<view v-for="iconName in twoToneIcons" :key="iconName" class="icon-item-content" :class="{'icon-item-content-active': props.value === iconName}" @click="handleChickIcon(iconName)">
					<sar-icon :name="svgIconPath + iconName + svgIconSuffix" :size="iconSize"></sar-icon>
				</view>
			</sar-space>
			<!-- 自定义 -->
			<sar-space wrap v-if="currentSegmented === 'Custom'">
				<view v-for="iconName in customIcons" :key="iconName" class="icon-item-content" :class="{'icon-item-content-active': props.value === iconName}" @click="handleChickIcon(iconName)">
					<sar-icon :name="svgIconPath + iconName + svgIconSuffix" :size="iconSize"></sar-icon>
				</view>
			</sar-space>
		</scroll-view>
	</view>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import icons from '@/subpackages/system/static/icons.json'

const svgIconPath = "/static/icons/svg/"
const svgIconSuffix = ".svg"

const iconSize = '70rpx'

const props = withDefaults(defineProps<{
	// 双向绑定图标name
	value: string,
	// 图表区域高度
	height?: string,
	// 组件宽度
	width?: string
}>(), {
	width: '718rpx',
	height: '25vh'
})

// 双向绑定
const emits = defineEmits(['update:value'])

// 获取当前主题
const { theme } = uni.getSystemInfoSync();
const currentTheme = ref<string|undefined>(theme)

/**
 * 分段器相关
 */
const currentSegmented = ref<'Outlined' | 'Filled' | 'TwoTone' | 'Custom'>('Outlined')
const options = [{label: '线框', value: 'Outlined'}, {label: '实底', value: 'Filled'}, {label: '双色', value: 'TwoTone'}, {label: '自定义', value: 'Custom'}]

/**
 * 不同样式图标
 */
const filledIcons:string[] = []
const outlinedIcons:string[] = []
const twoToneIcons:string[] = []
const customIcons:string[] = []

/**
 * 初始化图标列表
 */
icons.forEach(iconName => {
	if (iconName.endsWith('Filled')) {
		filledIcons.push(iconName)
	} else if (iconName.endsWith('Outlined')) {
		outlinedIcons.push(iconName)
	} else if (iconName.endsWith('TwoTone')) {
		twoToneIcons.push(iconName)
	} else {
		customIcons.push(iconName)
	}
})

/**
 * 点击图标
 */
const handleChickIcon = (iconName: string) => {
	emits('update:value', iconName)
}

/**
 * 监听主题变化
 */
const handleChangeTheme = ({theme}: {theme: 'dark' | 'light'}) => {
	currentTheme.value = theme
}

onMounted(() => {
	uni.onThemeChange(handleChangeTheme);
})

onUnmounted(() => {
	uni.offThemeChange(handleChangeTheme)
})
</script>

<style scoped lang="scss">
.icon-item-content {
	height: 100rpx;
	width:  100rpx;
	border-radius: 24rpx;
	display: flex;
	justify-content: center;
	align-items: center;
}

.icon-item-content-active {
	background-color: var(--sar-primary);
}
</style>