<template>
	<view class="content">
		<sar-space direction="vertical">
			<sar-list card v-if="!isStarList && noticeDataList.length > 0">
				<sar-list-item arrow hover @click="toStarPage">
					<template #title>
						<sar-space align="center">
							<sar-avatar size="96rpx" background="var(--sar-warning)">
								<sar-icon name="StarOutlined" family="outlined" size="64rpx" color="#fff"/>
							</sar-avatar>
							<view>标星通知</view>
						</sar-space>
					</template>
				</sar-list-item>
			</sar-list>
			<!-- 消息列表 -->
			<notice-list :notice-data="noticeDataList" :load-status="status" @clickItem="handleClickItem" @clickStar="handleStar" ref="noticeListRef"/>
			<!-- 加载更多 -->
			<sar-load-more v-if="(status === 'loading' || noticeDataList.length > 0) && !isReload" :status="status" @load-more="loadMore" @reload="reload"/>
		</sar-space>
	</view>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue';
import NoticeList from '@/subpackages/system/notice/components/NoticeList.vue';
import {userMessageList, star} from '@/api/system/notice/notice';
import type {SysUserNoticeVO} from "@/api/system/notice/type/sys-user-notice"
import type { SysNoticeDTO } from '@/api/system/notice/type/sys-notice';
import { onLoad, onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app';
import { toast } from '@/utils/toast';
import type { LoadMoreStatus } from 'sard-uniapp';
import { ResponseError } from '@/api/global/type';
import router from "@/router/router";
import {useNoticeStore} from "@/stores/notice"

const noticeStore = useNoticeStore()
const noticeListRef = ref<InstanceType<typeof NoticeList>>()
// 是否为star页面
const isStarList = ref<boolean>(false)
// 是否为刷新
const isReload = ref<boolean>(false)

/**
 * 初始化加载列表
 */
const initList = () => {
	const status = ref<LoadMoreStatus>('incomplete')
	
	// 查询条件
	const query = ref<SysNoticeDTO>({pageNum: 1, pageSize: 10})
	
	// 列表数据
	const noticeDataList = ref<SysUserNoticeVO[]>([])
	
	// 加载更多
	const loadMore = () => {
		query.value.pageNum = query.value.pageNum + 1
		queryList()
	}
	
	// 重新加载
	const reload = () => {
		isReload.value = true
		noticeDataList.value = []
		query.value.pageNum = 1
		queryList()
	}
	
	// 数据查询
	const queryList = async () => {
		status.value = "loading"
		try {
			// star 页面增加查询条件
			if (isStarList.value) {
				query.value.star = "1"
			}
			const resp = await userMessageList(query.value)
			if (resp.code === 200) {
				const records = resp.data.records
				
				// 向数组中添加元素
				noticeDataList.value.push(...records)
				
				// 判断是否全部加载完成
				if (noticeDataList.value.length >= resp.data.total) {
					status.value = "complete"
				} else {
					status.value = "incomplete"
				}
			}
		} finally {
			uni.stopPullDownRefresh()
			isReload.value = false
		}
	}
	
	return {
		status,
		noticeDataList,
		reload,
		loadMore
	}
}

const {status, noticeDataList, reload, loadMore} = initList()

/**
 * 处理标星
 */
const handleStar = async (data: SysUserNoticeVO, index: number, hide: () => {}) => {
	if (data.noticeId && data.starFlag) {
    data.starFlag = data.starFlag === '0' ? '1' : '0'
    const resp = await star(data.noticeId, data.starFlag)
    if (resp.code === 200) {
      hide()
      // 处理关闭Swipe
      noticeListRef.value?.closeSwipe(index)
      // star页面记录操作数据
      if (isStarList) {
        uni.$emit("changeNoticeMeta", data)
      }
    } else {
      toast(resp.msg)
    }
	}
}

/**
 * 处理已读
 */
const handleRead = (noticeId: string, readFlag?: string) => {
	// 未读时发送已读标记
	if (readFlag === "0") {
		noticeStore.markAsRead(noticeId)
		.then((resp) => {
			if (resp.code === 200) {
				const item = noticeDataList.value.find(item => item.noticeId === noticeId)
				if (item) {
				  item.readFlag = "1"
				}
				// star页面记录操作数据
				if (isStarList) {
					uni.$emit("changeNoticeMeta", item)
				}
			} else {
				toast(resp.msg)
			}
		})
		.catch(err => {
			if (err instanceof ResponseError) {
				toast(err.msg)
			} else {
				console.error(err)
			}
		})
	}
}

// star页面点击已读｜标星操作
const handelChangeNoticeMeta = (data: SysUserNoticeVO) => {
	const item = noticeDataList.value.find(item => item.noticeId === data.noticeId)
	if (item) {
		item.starFlag = data.starFlag
		item.readFlag = data.readFlag
	}
}

/**
 * 处理点击消息
 */
const handleClickItem = (data: SysUserNoticeVO) => {
	const noticeId = data.noticeId
	if (!noticeId) {
		toast("参数错误")
		return
	}
	// 处理已读
	handleRead(noticeId, data.readFlag)
	// 跳转到详情页
	router.navigateTo({
		url: "/subpackages/system/notice/Detail",
		query: {
			id: noticeId,
			title: data.title
		}
	})
}

/**
 * 前往star页面
 * 其实是跳转本页面，携带参数进行判断
 */
const toStarPage = () => {
	router.navigateTo({
		url: "/subpackages/system/notice/index",
		query: {
			type: "star"
		}
	})
}

/**
 * 针对star页面的初始化
 */
const initStarPage = () => {
	isStarList.value = true
	uni.setNavigationBarTitle({
		title: "标星通知"
	})
}

/**
 * 触底加载
 */
onReachBottom(() => {
	if (status.value === "incomplete") {
		loadMore()
	}
})

/**
 * 获取page中的参数
 */
onLoad((option) => {
	const type = option?.type
	// 从query中根据参数判断是否为star页面
	if (type === 'star') {
		initStarPage()
	} else {
		// 非star页面，监听 changeNoticeMeta 事件
		uni.$on('changeNoticeMeta', handelChangeNoticeMeta)
	}
})

/**
 * 显示页面时加载数据
 */
onMounted(() => {
	reload()
	noticeStore.getUnreadCount()
	
})

/**
 * 退出通知公告后关闭监听
 */
onUnmounted(() => {
	if (!isStarList) {
		uni.$off('changeNoticeMeta', handelChangeNoticeMeta)
	}
})

/**
 * 下拉刷新
 */
onPullDownRefresh(() => {
	reload()
	noticeStore.getUnreadCount()
})

</script>
