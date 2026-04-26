<template>
	<view style="width: fit-content">
		<sar-tag :style="style" :theme="differenceAdapt(targetDict.tagStyle)" :color="targetDict.tagStyle"
			:plain="props.plain">
			{{ targetDict.label }}
		</sar-tag>
	</view>
</template>

<script setup lang="ts">
import { ref, watch } from "vue"
import type { SysDictDataType } from "@/api/system/dict/type/sys-dict-data-type"
import { traverseWithPath } from "@/utils/tree"
import { cloneDeep } from 'lodash-es'
const props = withDefaults(
	defineProps<{
		// 字典选项
		dictDataOption : SysDictDataType[],
		// 字典值
		dictDataValue : string,
		// 是否镂空
		plain ?: boolean,
		// 自定义样式
		style ?: Record<string, any>,
		// 是否展示树形节点全路径
		fullTreeNode ?: boolean,
		// 树形节点分隔符
		fullTreeSeparator ?: string,
		// 树形节点前缀
		rootTreeNodePrefix ?: string
	}>(),
	{
		plain: false,
		style: () => ({ "margin-right": 0 }),
		fullTreeNode: false,
		fullTreeSeparator: "/",
		rootTreeNodePrefix: ""
	}
)

// 目标字典
const targetDict = ref<SysDictDataType>({})

const initTargetDict = () => {
	const option = cloneDeep(props.dictDataOption)
	const value = cloneDeep(props.dictDataValue)
	// option 和 value 全部存在时才进行处理
	if (option && option.length > 0 && value) {
		let label = undefined
		let dict : SysDictDataType | undefined = undefined
		// 遍历树形所有节点
		traverseWithPath(option, (dictDataList : SysDictDataType[]) => {
			if (props.fullTreeSeparator) {
				const targetIndex = dictDataList.findIndex(item => item.value === value)
				if (targetIndex !== -1) {
					dict = dictDataList[targetIndex]
					if (props.fullTreeNode) {
						label = dictDataList.slice(0, targetIndex + 1).map(item => item.label).join(props.fullTreeSeparator)
					} else {
						label = dict.label
					}
				}
			}
		})
		if (dict) {
			targetDict.value = dict
			if (props.rootTreeNodePrefix !== "") {
				targetDict.value.label = props.rootTreeNodePrefix + props.fullTreeSeparator + label
			} else {
				targetDict.value.label = label
			}
		}
	}
}

// 特殊主题ant design 和 sard 命名不同，进行转化
const differenceAdapt = (theme ?: string) => {
	switch (theme) {
		case 'processing': {
			return 'primary'
		}
		case 'default': {
			return 'secondary'
		}
		case 'success': {
			return 'success'
		}
		case 'warning': {
			return 'warning'
		}
		case 'error': {
			return 'danger'
		}
	}
}

// 监听字典option变化
watch(() => props.dictDataOption, () => {
	initTargetDict()
}, { immediate: true })
// 监听字典值变化
watch(() => props.dictDataValue, () => {
	initTargetDict()
}, { immediate: true })

// 校验必填 prop
if (!props.dictDataValue) {
	console.error("dict-tag/DictTag 组件中 dictDataValue 值不存在")
}
if (!props.dictDataOption) {
	console.error("dict-tag/DictTag 组件中 dictDataOption 值不存在")
}
</script>