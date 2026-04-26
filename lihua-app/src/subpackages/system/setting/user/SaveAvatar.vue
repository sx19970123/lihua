<template>
	<view class="content avatar-content">
		<sar-space direction="vertical" size="large">
			<!-- 预览头像 -->
			<user-avatar :size="750 - 32" shape="square" :type="avatarData.type" :customAvatar="avatarData" />
			<!-- 操作菜单 -->
			<sar-list card>
				<sar-list-item title="选择照片" arrow hover @click="chooseImage"></sar-list-item>
				<sar-list-item title="选择图标" arrow hover @click="handleIconAvatar"></sar-list-item>
				<sar-list-item title="编辑文本" arrow hover @click="handleTextAvatar"></sar-list-item>
			</sar-list>
		</sar-space>
		<!-- 文本抽屉 -->
		<sar-popout v-model:visible="textPopout" :overlay-closable="!keyboardOpen" :show-close="false"
			@leave="autoFocus = false" :before-close="handleSave">
			<view class="popout-content">
				<sar-space direction="vertical" size="large">
					<!-- 头像背景颜色 -->
					<color-select :dataSource="colorSource" v-model:color="avatarData.backgroundColor"></color-select>
					<!-- 头像文本 -->
					<sar-input :focus="autoFocus" :adjust-position="false" root-class="rounded-input"
						placeholder="请输入文本" v-model="avatarData.value"
						@keyboardheightchange="handleKeyboardChange"></sar-input>
				</sar-space>
			</view>
		</sar-popout>
		<!-- 图标抽屉 -->
		<sar-popout v-model:visible="iconPopout" :show-close="false" @leave="autoFocus = false"
			:before-close="handleSave">
			<view class="popout-content">
				<sar-space direction="vertical" size="large">
					<!-- 头像背景颜色 -->
					<color-select :dataSource="colorSource" v-model:color="avatarData.backgroundColor"></color-select>
					<!-- 头像图标 -->
					<IconSelect v-model:value="avatarData.value" width="686rpx"></IconSelect>
				</sar-space>
			</view>
		</sar-popout>
	</view>

</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { cropImage } from 'sard-uniapp'
import UserAvatar from '@/components/user-avatar/index.vue'
import ColorSelect from '@/components/color-select/index.vue'
import type { AvatarType } from '@/api/system/profile/type/AvatarType'
import { saveBasics } from '@/api/system/profile/Profile'
import { publicUpload } from '@/api/system/attachment/AttachmentStorage'
import { useUserStore } from '@/stores/user'
import router from '@/router/Router'
import { toast } from '@/utils/Toast'
import { cloneDeep } from 'lodash-es'
import IconSelect from '@/components/icon-select/index.vue'
import {getFileInfo} from '@/utils/attachment/AttachmentUtils'

const userStore = useUserStore()

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

// 头像数据
const avatarData = ref<AvatarType>(cloneDeep(userStore.avatar))

// 执行保存
const handleSave = async (type ?: 'confirm' | 'cancel' | 'close') => {
	if (type === 'confirm') {
		// url 字段无需保存
		avatarData.value.url = undefined
		const resp = await saveBasics({ avatar: JSON.stringify(avatarData.value) })
		if (resp.code === 200) {
			// 刷新store
			await userStore.initUserInfo()
			router.navigateBack({})
		} else {
			toast(resp.msg)
		}
	}
}
/**
 * 初始化图片头像
 */
    // 选择头像
const chooseImage = async () => {
  // 选择照片｜拍照
  const filePath = await new Promise<string>((resolve, reject) => {
    uni.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success: (resp) => resolve(resp.tempFilePaths[0]),
      fail: reject,
    });
  });

  // 裁剪图片
  const croppedFilePath = await new Promise<string>((resolve, reject) => {
    cropImage({
      // 图片压缩到一半的清晰度
      beforeCrop: () => 0.5,
      src: filePath,
      success: resolve,
      fail: reject,
    });
  });

  // 获取图片信息
  const {size} = await getFileInfo(croppedFilePath)

  // 限制 2MB
  if (!size || (size / 1024 / 1024 > 2)) {
    toast("上传失败，头像不能超过 2MB");
    return;
  }

  // 上传图片
  uni.showLoading({ title: "正在上传", mask: true });
  try {
    const resp = await publicUpload(croppedFilePath, "UserAvatar");
    if (resp.code === 200) {
      avatarData.value.type = "image";
      avatarData.value.value = resp.data;
      handleSave("confirm");
    } else {
      toast(resp.msg);
    }
  } catch (err) {
    console.error(err);
    toast("上传失败");
  } finally {
    uni.hideLoading();
  }
}

/**
 * 初始化图标头像
 */
const initIconAvatar = () => {
	// 图标抽屉开关
	const iconPopout = ref<boolean>(false)
	// 处理图标头像
	const handleIconAvatar = () => {
		if (avatarData.value.type !== 'icon') {
			avatarData.value.type = 'icon'
			avatarData.value.value = ''
		}
		iconPopout.value = true
	}
	return {
		iconPopout,
		handleIconAvatar
	}
}
const { iconPopout, handleIconAvatar } = initIconAvatar()

/**
 * 初始化文本头像
 */
const initTextAvatar = () => {
	// 文本抽屉开关
	const textPopout = ref<boolean>(false)
	// 自动聚焦
	const autoFocus = ref<boolean>(false)
	// 键盘是否弹起
	const keyboardOpen = ref<boolean>(false)
	// 处理键盘事件
	const handleKeyboardChange = ({ detail } : { detail : { height : number } }) => {
		if (detail.height === 0) {
			setTimeout(() => {
				keyboardOpen.value = false
			}, 200)
		} else {
			keyboardOpen.value = true
		}
	}
	// 处理文本头像
	const handleTextAvatar = () => {
		// 设置文本头像前头像不为文本则清除value值
		if (avatarData.value.type !== 'text') {
			avatarData.value.type = 'text'
			avatarData.value.value = ''
		}
		textPopout.value = true
		autoFocus.value = true
	}
	return {
		textPopout,
		autoFocus,
		keyboardOpen,
		handleTextAvatar,
		handleKeyboardChange
	}
}
const { textPopout, autoFocus, keyboardOpen, handleTextAvatar, handleKeyboardChange } = initTextAvatar()
</script>

<style scoped lang="scss">
@import "@/static/style/input.scss";

.avatar-title {
	font-size: var(--sar-text-lg);
	font-weight: var(--sar-font-bold);
}

.popout-content {
	padding-left: 32rpx;
	padding-right: 32rpx
}

:deep(.sar-popout__header) {
	height: 32rpx !important;
}
</style>