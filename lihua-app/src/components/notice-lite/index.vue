<template>
	<sar-popout :title="noticeData.title" v-model:visible="visible" @update:visible="handleUpdate" :show-confirm="false">
	    <scroll-view scroll-y style="max-height: 60vh;">
			<view v-if="loading" class="notice-lite-loading">
				<sar-loading
					vertical
				    color="var(--sar-primary)"
				    text-color="var(--sar-primary)"
				    text="努力加载中"
				  />
			</view>
			<view class="content" v-else>
				<view class="notice-meta">
					<text>
						{{noticeData.releaseUser}} {{noticeData.releaseTime}}
					</text>
				</view>
				<!-- 富文本解析组件 -->
				<mp-html v-if="noticeData.content" :content="noticeData.content" :tag-style="tagStyle"/>
				<sar-empty v-else description="正文为空"/>
			</view>
		</scroll-view>
	</sar-popout>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import MpHtml from "@/components/mp-html/mp-html.vue"
import {useNoticeStore} from "@/stores/notice"
import type {PreviewNotice} from '@/api/system/notice/type/PreviewNotice';

const noticeStore = useNoticeStore()
// 显隐抽屉&通知id
const props = defineProps<{
	modelValue: boolean
	noticeId: string
}>()
// 双向绑定
const emits = defineEmits(["update:modelValue"])
// 显隐抽屉
const visible = ref<boolean>(props.modelValue)
// 抽屉变化
const handleUpdate = (val: boolean) => {
	visible.value = val
	// 关闭抽屉清空数据
	if (val === false) {
		noticeData.value = {}
	}
	// 双向绑定
	emits("update:modelValue", val)
}

// 通知公告数据
const noticeData = ref<PreviewNotice>({})

// 加载中
const loading = ref<boolean>(false)

// 预览通知公告
const handlePreview = async () => {
	loading.value = true
	noticeStore.previewNotice(props.noticeId).then((resp) => {
		noticeData.value = resp
		noticeStore.markAsRead(props.noticeId)
		loading.value = false
	})
}

// 双向绑定
watch(() => props.modelValue, (newValue) => {
	visible.value = newValue
	if (newValue && props.noticeId) {
		handlePreview()
	}
})

const tagStyle = {
  "*": "max-width:100%;box-sizing:border-box;",
  "img": "max-width:100%;height:auto;display:block;",
  "table": "width:100%;display:block;overflow-x:auto;"
}
</script>

<style lang="scss">
@import "@/static/style/notice-detail.scss";
.notice-lite-loading {
	text-align: center;
	height: 10vh;
	padding-top: 5vh;
}
</style>