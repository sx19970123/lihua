<template>
	<view>
		<!-- 上传附件类型为 image 或 video 并设置为按钮模式 -->
		<sar-upload v-model="fileList"
			:after-read="handleUpload" 
			:accept="props.uploadType" 
			:max-count="props.maxCount" 
			:max-size="props.maxSize" 
			:disabled="props.disabled"
			:readonly="props.readonly"
			:removable="props.removable"
			:multiple="props.maxCount > 1"
			:over-size="handleOverSize"
			:before-remove="handleDelete"
			@remove="handleModelValue"
			v-if="props.uploadType !== 'file' && props.uploadType !== 'all' && props.mode === 'button'">
			<template #default="{list, onSelect, onRemove, onImageClick}">
				<sar-space direction="vertical">
					<sar-button 
						@click="onSelect" 
						v-if="!props.readonly && fileList.length < props.maxCount"
						:disabled="props.disabled"
						:type="props.buttonType"
						:theme="props.buttonTheme"
						:square="props.buttonIsSquare"
						:round="props.buttonIsRound"
						:size="props.buttonSize"
						:icon="props.buttonIcon"
						:icon-family="props.buttonIconFamily" 
					>{{props.buttonText}}</sar-button>
					<attachment-card-list 
						:fileType="props.uploadType" 
						:list="list" 
						:disabled="props.disabled"
						:readonly="props.readonly"
						:removable="props.removable"
						@delete="onRemove" 
						@clickImage="onImageClick"/>
				</sar-space>
			</template>
		</sar-upload>
		
		<!-- 上传附件类型为 image 或 video 并设置为图片模式 -->
		<sar-upload v-model="fileList" 
			:after-read="handleUpload" 
			:accept="props.uploadType" 
			:max-count="props.maxCount" 
			:max-size="props.maxSize" 
			:disabled="props.disabled"
			:readonly="props.readonly"
			:removable="props.removable"
			:multiple="props.maxCount > 1"
			:over-size="handleOverSize"
			:before-remove="handleDelete"
			@remove="handleModelValue"
			v-if="props.uploadType !== 'file' && props.uploadType !== 'all' && props.mode === 'picture'"/>
		
		<!-- 上传附件类型不为image和video，自行实现，只能以按钮形式上传（仅微信小程序支持） -->
		<sar-space direction="vertical" v-if="props.uploadType === 'file' || props.uploadType === 'all'">
			<sar-button 
				v-if="!props.readonly && fileList.length < props.maxCount" 
				:disabled="props.disabled"
				:type="props.buttonType"
				:theme="props.buttonTheme"
				:square="props.buttonIsSquare"
				:round="props.buttonIsRound"
				:size="props.buttonSize"
				:icon="props.buttonIcon"
				:icon-family="props.buttonIconFamily" 
				@click="handleMessageChoose"
				>{{props.buttonText}}</sar-button>
			<attachment-card-list
				fileType="file" 
				:list="fileList"
				:disabled="props.disabled"
				:readonly="props.readonly"
				:removable="props.removable"
				@clickImage="handleWechatImgPreview"
				@delete="handleDelete" 
			/>
		</sar-space>
	</view>
</template>

<script setup lang="ts">
import { ref, withDefaults, watch, nextTick } from 'vue'
import type { UploadFileItem } from 'sard-uniapp'
import { dialog } from 'sard-uniapp'
import AttachmentCardList from '@/components/attachment-upload/AttachmentCardList.vue'
import {toast} from '@/utils/toast'
import {getFileInfo} from '@/utils/attachment/attachment-utils'
import {queryAttachmentInfoByIds, upload, fastUpload, existsAttachmentByMd5, deleteFromBusiness} from '@/api/system/attachment/attachment-storage'
import { ResponseError } from '@/api/global/type'
// 记录双向绑定回显是否已经完成
let modelValueInitComplete = false
// 删除的id
const removeIds: string[] = []

// 传入参数
const props = withDefaults(defineProps<{
	// 双向绑定
	modelValue?: string,
	// 类型
	mode?: 'button' | 'picture',
	// 可上传的附件类型（设置为file会从微信聊天中选取文件，仅微信小程序支持）
	uploadType?: 'image' | 'video' | 'file' | 'all',
	// 包含的文件后缀，仅 uploadType === file 时生效，数组后缀名不带. 如匹配pdf，直接 :extension="['pdf']"
	extension?: string[],
	// 文件上传限制的数量
	maxCount?: number,
	// 文件上传大小限制
	maxSize?: number,
	// 禁用状态
	disabled?: boolean,
	// 只读状态
	readonly?: boolean,
	// 可否删除
	removable?: boolean,
	// 业务编码（自定义，用于后台附件管理区分附件对应的业务）
	businessCode: string,
	// 业务名称（自定义，用于后台附件管理区分附件对应的业务）
	businessName: string,
	// 自动删除（点击删除按钮是否自动进行业务删除）
	autoRemove?: boolean,
	// 删除描述（自动删除开启后，点击删除弹框提示文本）
	removeText?: string,
	// 按钮文本
	buttonText?: string,
	// 按钮类型
	buttonType?: 'default' | 'pale' | 'mild' | 'outline' | 'text' | 'pale-text',
	// 按钮主题
	buttonTheme?: 'primary' | 'secondary' | 'success' | 'info' | 'warning' | 'danger',
	// 是否为圆形按钮
	buttonIsRound?: boolean,
	// 是否为方形按钮
	buttonIsSquare?: boolean,
	// 按钮尺寸
	buttonSize?: 'mini' | 'small' | 'medium' | 'large',
	// 按钮图标
	buttonIcon?: string,
	// 按钮图标字体
	buttonIconFamily?: string,
}>(), {
	mode: 'picture',
	uploadType: 'image',
	maxCount: 6,
	maxSize: 1 * 1024 * 1024,
	disabled: false,
	readonly: false,
	removable: true,
	autoRemove: true,
	removeText: '删除后无法恢复，是否删除？',
	buttonText: '点击上传',
	buttonType: 'default',
	buttonTheme: 'primary',
	buttonIsRound: false,
	buttonIsSquare: false,
	buttonSize: 'medium'
})

// 抛出方法
const emits = defineEmits(['update:model-value', 'uploadSuccess', 'uploadError', 'exceedMaxCount'])

// 初始化双向绑定
const initModelValue = async () => {
	const modelValue = props.modelValue
	if (modelValue && !modelValueInitComplete) {
		try {
			const resp = await queryAttachmentInfoByIds(modelValue.split(","))
			if (resp.code === 200) {
				// 组合数据
				fileList.value = resp.data.map(item => {
					return {
						// 返回路径以http开头，则直接回显，否则拼接路径访问后台获取附件
						url: item.path?.startsWith("http") ? item.path : import.meta.env.VITE_APP_BASE_API + '/app' + item.path,
						name: item.originalName,
						status: item.status === 'error' ? "failed" : "done",
						message: item.errorMsg,
						id: item.id,
						type: item.type?.includes("image") ? 'image' : item.type?.includes("video") ? 'video' : 'file',
						isImage: item.type?.includes("image"),
						isVideo: item.type?.includes("video")
					}
				})
			} else {
				toast(resp.msg)
			}
		} catch(err) {
			if (err instanceof ResponseError) {
				toast((err as unknown as ResponseError).msg)
			} else {
				toast("附件加载失败")
			}
		} finally {
			modelValueInitComplete = true
		}
	}
}

// 处理双向绑定
const handleModelValue = () => {
	// 取出附件列表中的id属性，进行双向绑定
	const ids = fileList.value.filter(item => item.id).map(item => item.id).join(",")
	emits('update:model-value', ids)
}

// 附件列表
const fileList = ref<UploadFileItem[]>([])

// 处理上传
const handleUpload = async (fileItem : UploadFileItem) => {
	// 上传前状态赋值
	fileItem.status = 'uploading'
	fileItem.message = '正在上传'
	fileItem.url = fileItem?.file?.path || fileItem.url
	
	// 获取附件md5
	try {
		const { md5, fileName, filePath, size } = await getFileInfo(fileItem.url)
		if (!md5 || !fileName || !filePath || !size) {
			toast("附件信息获取异常")
			return
		}
		const resp = await existsAttachmentByMd5(md5)
		if (resp.code === 200) {
			let uploadResp
			// 附件存在，进行文件秒传
			if (resp.data) {
				uploadResp = await handleFastUpload(fileName, size, md5)
			} else {
				// 附件上传
				uploadResp = await handleFileUpload(filePath, md5)
			}
			// 上传完成
			if (uploadResp.code === 200) {
				// 将服务器id记录到 fileItem 对象
				fileItem.id = uploadResp.data
				// 修改状态
				fileItem.status = 'done'
				emits('uploadSuccess', fileItem, fileList.value)
			} else {
				fileItem.status = 'failed'
				fileItem.message = uploadResp.msg
				emits('uploadError', fileItem, uploadResp.msg)
			}
		} else {
			fileItem.status = 'failed'
			fileItem.message = resp.msg
		}
	} catch(err) {
		fileItem.status = 'failed'
		if (err instanceof ResponseError) {
			fileItem.message = (err as unknown as ResponseError).msg
		} else {
			fileItem.message = "上传失败"
		}
	} finally {
		setTimeout(() => {
			nextTick(() => {
				// 重新赋值
				fileList.value = [...fileList.value]
				// 处理双向绑定
				handleModelValue()
			})
		}, 100)
	}
}

// 处理文件秒传
const handleFastUpload = async (fileName: string, size: number, md5: string) => {
	return await fastUpload(fileName, props.businessCode, props.businessName, size, md5)
}

// 处理附件上传
const handleFileUpload = async (filePath: string, md5: string) => {
	return await upload(filePath, props.businessCode, props.businessName, md5)
}

// 处理超出指定大小
const handleOverSize = (fileItemList: UploadFileItem[]) => {
	emits('exceedMaxCount', fileItemList)
	// 根据附件大小转换 mb 和 kb 提示
	const maxSize = props.maxSize / 1024 / 1024
	toast(fileItemList.length + "个附件上传失败，单个附件不能超过" + (maxSize < 1 ? maxSize * 1024 + 'KB' : maxSize + 'MB'))
}

// 处理附件删除
const handleDelete = async (_index: number, fileItem: UploadFileItem) => {
	return new Promise((resolve, reject) => {
		const id = fileItem.id
		if (id) {
			// 开启自动删除后，点击附件删除直接调用业务删除方法
			if (props.autoRemove) {
				dialog.confirm({
					message: props.removeText,
					buttonType: 'round',
					onConfirm: async () => {
						// 业务删除数据，真实删除需要从后台附件管理进行删除
						try {
							const resp = await deleteFromBusiness([id])
							if (resp.code === 200) {
								// 只有上传类型为 file 或 all 时进行手动删除fileList数据
								if (props.uploadType === 'file' || props.uploadType === 'all') {
									fileList.value = fileList.value.filter(item => item.id !== id)
									handleModelValue()
								}
								resolve({})
							} else {
								toast(resp.msg)
								reject()
							}
						} catch(err) {
							if (err instanceof ResponseError) {
								toast((err as unknown as ResponseError).msg)
							} else {
								toast("删除失败")
								console.error(err)
							}
							reject()
						}
					},
					onCancel: () => {
						// 取消删除
						reject()
					}
				})
			} else {
				// 上传类型为file 或 all 则手动控制列表元素
				if (props.uploadType === 'file' || props.uploadType === 'all') {
					fileList.value = fileList.value.filter(item => item.id !== id)
					handleModelValue()
				}
				// 向删除id列表中添加数据
				removeIds.push(id)
				resolve({})
			}
		} else {
			toast("附件id不存在")
			reject()
		}
	})
}

// 处理业务删除
const businessRemove = async () => {
	return new Promise((resolve, reject) => {
		if (removeIds.length === 0) {
			reject({msg: '附件id不存在'})
			return
		}
		deleteFromBusiness(removeIds)
		.then(resp => {
			if (resp.code === 200) {
				removeIds.length = 0
			}
			resolve(resp)
		})
		.catch(err => reject(err))
	})
}

// 微信消息文件选择
const handleMessageChoose = () => {
	// #ifdef MP-WEIXIN
		uni.chooseMessageFile({
			count: props.maxCount - fileList.value.length,
			type: props.uploadType,
			extension: props.extension,
			success: ({ tempFiles }: { tempFiles: { path: string; name: string; size: number, type: string }[] }) => {
				// 超出大小的附件
				const overSizeFiles: UploadFileItem[] = []
				
				tempFiles.forEach(file => {
					const {size, path, name, type} = file
					const fileItem = {url: path, name: name, type: type}
					if (size <= props.maxSize) {
						// 大小范围内的附件进行上传
						handleUpload(fileItem)
						fileList.value.push(fileItem)
					} else {
						// 超出大小的附件进行收集
						overSizeFiles.push(fileItem)
					}
				})
				
				// 同一处理超出大小的附件
				if (overSizeFiles.length > 0) {
					handleOverSize(overSizeFiles)
				}
			}
		})
	// #endif
	// #ifdef APP-PLUS
		toast("仅微信小程序支持此配置")
		throw new Error("APP 不支持传入uploadType为 file 和 all")
	// #endif
}

// 处理微信图片预览
const handleWechatImgPreview = (_index: number, item: UploadFileItem) => {
	// 过滤image
	const urls = fileList.value.filter(item => item.type === 'image').map(item => item.url)
	const idx = urls.findIndex(url => url === item.url)
	// 预览
	uni.previewImage({current: idx, urls: urls as string[]})

}

// 处理回显
watch(() => props.modelValue, (value) => {
    if (value) initModelValue()
  },{ immediate: true })

// 抛出函数
defineExpose({
	businessRemove
})


</script>