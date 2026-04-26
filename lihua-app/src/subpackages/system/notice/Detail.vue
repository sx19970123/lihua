<template>
	<view class="content">
		<!-- #ifdef APP-PLUS -->
		<!-- app 开启动态导航栏，需要占位元素 -->
		<sar-tabbar-pit />
		<!-- #endif -->
		
		<view class="notice-title">
			{{noticeData.title}}
		</view>
		<view class="notice-meta">
			<text>
				{{noticeData.releaseUser}} {{noticeData.releaseTime}}
			</text>
		</view>
		<!-- 富文本解析组件 -->
		<mp-html :content="noticeData.content" :tag-style="tagStyle"/>
	</view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { toast } from '@/utils/Toast';
import MpHtml from "@/components/mp-html/mp-html.vue"
import {useNoticeStore} from "@/stores/notice"
import type {PreviewNotice} from '@/api/system/notice/type/PreviewNotice';
const noticeStore = useNoticeStore()
// 通知公告数据
const noticeData = ref<PreviewNotice>({})

// 预览
const preview = (id?: string) => {
	if (!id) {
		toast("通知公告id不存在")
		return
	}
	// 预览
	noticeStore.previewNotice(id).then((resp) => {
		noticeData.value = resp
	})
}

onLoad((option) => {
	// 设置标题
	uni.setNavigationBarTitle({
		title: decodeURIComponent(option?.title)
	})
	// 预览
	preview(option?.id)
})

const tagStyle = {
  "*": "max-width:100%;box-sizing:border-box;",
  "img": "max-width:100%;height:auto;display:block;",
  "table": "width:100%;display:block;overflow-x:auto;"
}
</script>

<style lang="scss">
@import "@/static/style/notice-detail.scss";
</style>