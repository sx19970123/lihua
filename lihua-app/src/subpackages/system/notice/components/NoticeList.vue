<template>
	<sar-list card v-if="props.noticeData && props.noticeData.length > 0">
		<sar-list-item v-for="(notice, index) in props.noticeData" :key="notice.noticeId" :root-style="{padding: index === 0 ? '0 0 0 0' :'1px 0 0 0'}" @click="handleClickItem(notice, index)">
			<sar-swipe-action v-model:visible="autoClose[index].visible">
				<sar-list-item arrow hover>
					<template #title>
						<sar-space align="center">
							<!-- 图标｜未读红点 -->
							<sar-badge :dot="notice.readFlag === '0'">
								<sar-avatar size="96rpx" background="var(--sar-primary)">
									<sar-icon :name="notice.icon" family="outlined" size="64rpx" color="#fff"/>
								</sar-avatar>
							</sar-badge>
							<sar-space direction="vertical" size="small">
								<!-- 标题 -->
								<view class="title">{{notice.title}}</view>
								<!-- 发布人｜时间 -->
								<sar-space>
									<view class="release-info">{{notice.releaseUser}}</view>
									<view class="release-info">{{handleTime(notice.releaseTime)}}</view>
								</sar-space>
							</sar-space>
						</sar-space>
					</template>
					<!-- 优先级 -->
					<template #value>
						<dict-tag :dict-data-value="notice.priority" :dict-data-option="sys_notice_priority"></dict-tag>
					</template>
				</sar-list-item>
				<!-- 侧滑标星 -->
				<template #right="{ hide }">
					<sar-button root-style="height: 100%" theme="primary" square inline @click="handleClickStar(notice, index, hide)">
						<sar-space>
							<sar-rate :model-value="notice.starFlag == '0' ? 0 : 1" :count="1" void-color="var(--sar-warning)"/>
							{{notice.starFlag == '0' ? '标星' : '取消标星'}}
						</sar-space>
					</sar-button>
				</template>
			</sar-swipe-action>
		</sar-list-item>
	</sar-list>
	<sar-empty root-class="empty" v-if="(!props.noticeData || props.noticeData.length === 0) && props.loadStatus !== 'loading'"/>
</template>

<script setup lang="ts">
import {ref, watch, nextTick} from 'vue'
import DictTag from "@/components/dict-tag/index.vue"
import type {SysUserNoticeVO} from "@/api/system/notice/type/SysUserNotice"
import {initDict} from '@/utils/Dict'
import {handleTime} from "@/utils/HandleDate"
// 加载字典
const {sys_notice_priority} = initDict('sys_notice_priority')
// 滑动状态
type SwipeStatusType = 'left' | 'right' | false

// 传入数据
const props = defineProps<{
	noticeData?: Array<SysUserNoticeVO>,
	loadStatus: string
}>()

const emits = defineEmits(["clickItem", "clickStar"])

/**
 * 点击元素
 */
const handleClickItem = (item: SysUserNoticeVO, index: number) => {
	emits("clickItem", item, index)
}

/**
 * 点击标星/取消标星
 */
const handleClickStar = (item: SysUserNoticeVO, index: number, hide: () => {}) => {
	emits("clickStar", item, index, hide)
}

/**
 * 初始化swipe互斥相关
 */
const initSwipe = () => {
	// swipe 自动关闭
	const autoClose = ref<Array<{index: number, visible: SwipeStatusType }>>([])
	const openIndex = ref<number>()
	
	// 保持swipe为互斥状态
	const handleVisible = (data: SwipeStatusType, index: number) => {
		if (data === 'right') {
			const openIdx = openIndex.value
			if (openIdx !== undefined) {
				nextTick(() => autoClose.value[openIdx].visible = false)
			}
			openIndex.value = index
			return
		}
		
		if (data === false && openIndex.value === index) {
			openIndex.value = undefined
		}
	}
	
	// 由外部关闭了
	const closeSwipe = (index: number) => {
		handleVisible(false, index)
	}
	
	return {
		autoClose,
		openIndex,
		closeSwipe,
		handleVisible
	}
}

const {autoClose, openIndex, closeSwipe, handleVisible} = initSwipe()

watch(() => props.noticeData, () => {
	autoClose.value = []
	openIndex.value = undefined
	if (props.noticeData) {
		props.noticeData.forEach((_item, index) => {
			autoClose.value.push({index, visible: false})
		})
	}
	
}, {immediate: true, deep: true})

// 根据滑动状态进行开关互斥
watch(() => autoClose.value.map(item => item.visible), (newVals, oldVals) => {
    newVals.forEach((v, i) => {
      if (oldVals && v !== oldVals[i]) {
		  handleVisible(v, i)
	  }
    })
  },{ immediate: true })

	
// 抛出外部关闭函数
defineExpose({
	closeSwipe
})
</script>

<style scoped lang="scss">
.title {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	max-width: 50vw;
}
.release-info {
	font-size: var(--sar-text-sm); 
	color: var(--sar-secondary);
}
.empty {
	margin-top: 25vh;
}
</style>