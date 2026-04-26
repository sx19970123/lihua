<template>
	<view v-if="avatarData">
		<!-- 图片类型 -->
		<sar-avatar v-if="avatarData.type === 'image'" :src="avatarData.url" :size="size + 'rpx'" :shape="shape" :root-class="shape === 'square' ? 'avatar-shape' : ''"/>
		<!-- 文本|图标类型 -->
		<sar-avatar v-else :background="avatarData.backgroundColor" :size="size + 'rpx'" class="avatar-text" :shape="shape" :root-class="shape === 'square' ? 'avatar-shape' : ''">
			<view style="margin-top: 3rpx;" v-if="avatarData.type === 'icon'">
				<sar-icon :family="iconInfo?.family" :name="iconInfo?.name" color="#fff" :size="(size / 1.4) + 'rpx'"/>
			</view>
			<text v-else :style="{fontSize: fontSize + 'rpx', lineHeight: size + 'rpx'}" style="color: #fff">{{avatarData.value}}</text>
		</sar-avatar>
	</view>
	<!-- 默认头像 -->
	<sar-avatar v-else />
</template>

<script setup lang="ts">
import {onMounted, ref, withDefaults, watch} from "vue"
import type {AvatarType} from "@/api/system/profile/type/AvatarType"
import {useUserStore} from "@/stores/user"
import { onPageShow } from "@dcloudio/uni-app"
import {attachmentUrl} from "@/utils/attachment/AttachmentUtils";

// 接收参数
const {size, shape, customAvatar} = withDefaults(defineProps<{
	// 头像大小（rpx）
	size?: number,
	// 图标形状
	shape?: "square" | "circle",
	// 头像
	customAvatar?: AvatarType
}>(), {
	size: 128,
	shape: "circle"
})

const avatarData = ref<AvatarType>()
const fontSize = ref<number>(0)
const iconInfo = ref<{family: string, name: string}>()
const svgIconPath = "/static/icons/svg/"
const svgIconSuffix = ".svg"

/**
 * 加载头像
 */
const initAvatar = () => {
	// 传入type表示自定义显示
	if (customAvatar && customAvatar.type) {
		avatarData.value = customAvatar
	} else {
		// type不存在，加载当前用户头像
		const userStore = useUserStore()
		avatarData.value = userStore.avatar
		if (avatarData.value.backgroundColor?.includes('conic-gradient')) {
			avatarData.value.backgroundColor = 'rgb(22, 119, 255)'
		}
	}
	// 自适应文本尺寸
	autoFontSize()
	// 处理图标
	handleIcon()
}

/**
 * 自适应字号
 */
const autoFontSize = () => {
  if (avatarData.value?.type !== 'text') return

  const text = avatarData.value?.value
  if (!text) return

  // 计算加权长度：中文 1，英文 0.5，emoji 1.5
  let weightLen = 0
  for (const ch of text) {
    if (/[\u4e00-\u9fa5]/.test(ch)) {
		// 中文字符
		weightLen += 1  
    } else if (/[\uD800-\uDBFF][\uDC00-\uDFFF]/.test(ch)) {
		// emoji
		weightLen += 1.5
    } else {
		if (text.length === 1) {
			weightLen += 1
		} else {
			// 英文、数字、符号
			weightLen += 0.5
		}
    }
  }

  // 字号计算
  fontSize.value = (size * 0.8) / weightLen
}

// 处理图标
const handleIcon = () => {
	if (avatarData.value?.type !== 'icon') return
	const icon = avatarData.value?.value
	if (!icon) return
	// ant design 图标
	if (icon.endsWith("Outlined")) {
		iconInfo.value = {family: 'outlined', name: icon}
	} else if (icon.endsWith("Filled")) {
		iconInfo.value = {family: 'filled', name: icon}
	} else {
		// 自定义svg及双色图标
		iconInfo.value = {family: '', name: svgIconPath + icon + svgIconSuffix}
	}
}

onMounted(() => {
	initAvatar()
})

watch(() => customAvatar, () => {
	initAvatar()
}, {deep: true})

onPageShow(() => {
	initAvatar()
})

defineExpose({
	initAvatar
})
</script>
<style scoped lang="scss">
.avatar-shape {
	border-radius: var(--sar-rounded-lg)
}
</style>