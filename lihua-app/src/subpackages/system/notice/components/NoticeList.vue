<template>
	<sar-swipe-action-group>
		<sar-list card v-if="props.noticeData && props.noticeData.length > 0">
			<sar-list-item v-for="(notice, index) in props.noticeData" :key="notice.noticeId" :root-style="{padding: index === 0 ? '0 0 0 0' :'1px 0 0 0'}" @click="handleClickItem(notice, index)">
				<sar-swipe-action>
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
	</sar-swipe-action-group>
	<sar-empty root-class="empty" v-if="(!props.noticeData || props.noticeData.length === 0) && props.loadStatus !== 'loading'"/>
</template>

<script setup lang="ts">
import {ref, watch, nextTick} from 'vue'
import DictTag from "@/components/dict-tag/index.vue"
import type {SysUserNoticeVO} from "@/api/system/notice/type/sys-user-notice"
import {initDict} from '@/helpers/dict'
import {handleTime} from "@/utils/handle-date"
// 加载字典
const {sys_notice_priority} = initDict('sys_notice_priority')

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