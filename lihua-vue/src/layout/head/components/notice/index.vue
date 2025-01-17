<template>
  <div>
    <a-dropdown trigger="click"
                placement="bottom"
                v-model:open="open"
                @openChange="handleChangeNoticeList"
                :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode"
    >
      <template #overlay>
        <div class="notice-card">
          <a-tabs centered @change="handleChangeTabs">
            <a-tab-pane key="ALL">
              <template #tab>
                <span>
                  <MessageOutlined />
                </span>
                全部通知
              </template>
            </a-tab-pane>
            <a-tab-pane key="STAR">
              <template #tab>
                <span>
                  <StarOutlined />
                </span>
                标星通知
              </template>
            </a-tab-pane>
          </a-tabs>
<!--          通知列表-->
          <a-list item-layout="horizontal"
                  :data-source="userNoticeList"
                  :loading="loading"
                  :split="false"
                  class="notice-list scrollbar"
          >
            <template #renderItem="{ item }">
              <a-list-item class="notice-list-item" @click="readNoticeDetail(item.readFlag, item.noticeId)">
                <a-list-item-meta>
                  <!--                      发布时间-->
                  <template #description>
                    <a-typography-text type="secondary" ellipsis v-model:content="item.releaseUser"/>
                    <a-divider type="vertical" />
                    <a-typography-text type="secondary">
                      {{handleTime(dayjs(item.releaseTime).format('YYYY-MM-DD HH:mm')) }}
                    </a-typography-text>
                  </template>
                  <template #title>
                    <a-flex justify="space-between" align="flex-start">
                      <!--                      标题-->
                      <div>
                        <a-tooltip :title="item.title" placement="bottom" :get-popup-container="(triggerNode: HTMLElement) => triggerNode.parentNode">
                          <a-typography-text class="notice-title" ellipsis v-model:content="item.title"/>
                        </a-tooltip>
                      </div>
                      <!--                      标星-->
                      <a-rate :count="1"
                              class="notice-star"
                              v-model:value="item.starFlagNumber"
                              @click="(event:MouseEvent) => event.stopPropagation()"
                              @change="(value: number) => handleStar(item.noticeId, value)" />
                      <!--                      优先级-->
                      <dict-tag :dict-data-option="sys_notice_priority" :dict-data-value="item.priority"/>
                    </a-flex>
                  </template>
                  <!--                      头像-->
                  <template #avatar>
                    <a-badge :dot="item.readFlag === '0'">
                      <a-avatar :style="{'background-color': themeStore.getColorPrimary()}">
                        <MessageOutlined v-if="item.type === '0'"/>
                        <NotificationOutlined v-else/>
                      </a-avatar>
                    </a-badge>
                  </template>
                </a-list-item-meta>
              </a-list-item>
            </template>
            <!--                      加载更多-->
            <template #loadMore v-if="userNoticeList.length > 0">
              <a-flex align="center" justify="center">
                <a-button type="text" class="more-btn" @click="queryMore" :disabled="total === userNoticeList.length">
                  {{total === userNoticeList.length ? '没有更多' : '加载更多'}}
                </a-button>
              </a-flex>
            </template>
          </a-list>
        </div>
      </template>
<!--                      通知公告主体-->
      <div class="header-right-item header-right" @click="() => open = true">
        <a-tooltip title="通知公告" placement="bottom" :get-popup-container="(triggerNode: HTMLElement) => triggerNode.parentNode">
          <a-badge :count="unReadCount" :offset="[-4,4]" style="color: #FFFFFF">
            <a-avatar :size="32" style="background-color: rgba(0,0,0,0)">
              <BellOutlined class="icon-default-color"/>
            </a-avatar>
          </a-badge>
        </a-tooltip>
      </div>
    </a-dropdown>
<!--                      详情dialog-->
    <a-modal v-model:open="previewModelOpen"
             :footer="false"
             :width="960"
             destroy-on-close
    >
      <notice-preview :notice-id="noticeId"/>
    </a-modal>
  </div>

</template>

<script setup lang="ts">
import {handleSseMessage, type SSEResponseType} from "@/utils/ServerSentEvents.ts";
import NoticePreview from "@/components/notice-preview/index.vue"
import DictTag from "@/components/dict-tag/index.vue"
import type {SysNotice, SysNoticeDTO} from "@/api/system/noice/type/SysNotice.ts";
import {Button, message, notification} from "ant-design-vue";
import {h, ref} from "vue";
import {MessageOutlined, NotificationOutlined} from "@ant-design/icons-vue";
import {useThemeStore} from "@/stores/theme.ts";
import {useUserStore} from "@/stores/user.ts";
import {getDictLabel, initDict} from "@/utils/Dict.ts";
import {queryListByUserId, read, star, queryUnReadCount} from "@/api/system/noice/Notice.ts";
import type {SysUserNoticeVO} from "@/api/system/noice/type/SysUserNotice.ts";
import {handleTime} from "@/utils/HandleDate.ts";
import dayjs from "dayjs";
import {ResponseError} from "@/api/global/Type.ts";

const themeStore = useThemeStore();
const userStore = useUserStore()

const previewModelOpen = ref<boolean>(false)
const {sys_notice_type, sys_notice_priority} = initDict("sys_notice_type", "sys_notice_priority")

// 未读计数
const unReadCount = ref<number>(0)
// 查询未读数量
const handleUnReadCount = async () => {
  try {
    const resp = await queryUnReadCount()
    if (resp.code === 200) {
      unReadCount.value = resp.data
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  }
}
handleUnReadCount()

// 处理消息通知
handleSseMessage((response: SSEResponseType<SysNotice>) => {
  // 通知类型为 SSE_NOTICE 进行后续处理
  if (response.type !== 'SSE_NOTICE') {
    return
  }
  const {id, title, type} = response.data
  // 新未读消息计数 + 1
  handleUnReadCount()
  // 弹出消息通知
  notification.open({
    message: '您有一条新' + getDictLabel(sys_notice_type.value, type),
    description: title,
    btn: () => h( Button, {
      type: "text",
      size: "small",
      onClick: () => {
        if (id) {
          // 显示详情
          showNoticeDetail(id)
          // 关闭消息提醒
          notification.close(id)
          // 处理已读
          handleRead(id)
        }
      },
    }, {
      default: () => '查看详情'
    }),
    icon: () => h("0" === type ? MessageOutlined : NotificationOutlined, { style: 'color: ' + themeStore.getColorPrimary()}),
    key: id
  })
})

// 初始化列表查询
const initList = () => {
  const open = ref<boolean>(false)
  const loading = ref<boolean>(false)
  // notice 列表数据
  const userNoticeList = ref<SysUserNoticeVO[]>([])
  // 全部数量
  const total = ref<number>(0)

  // 分页查询
  const query = ref<SysNoticeDTO>({
    pageNum: 1,
    pageSize: 5,
  })

  // 处理展开关闭Notice
  const handleChangeNoticeList = (open: boolean) => {
    if (open) {
      query.value.pageNum = 1
      userNoticeList.value = []
      // 查询列表
      initNoticeList()
      // 查询未读数量
      handleUnReadCount()
    }
  }

  const queryMore = () => {
    query.value.pageNum++
    initNoticeList()
  }

  // 查询star
  const queryStar = () => {
    query.value.pageNum = 1
    query.value.star = '1'
    userNoticeList.value = []
    initNoticeList()
  }

  // 查询全部
  const queryAll = () => {
    query.value.pageNum = 1
    query.value.star = undefined
    userNoticeList.value = []
    initNoticeList()
  }

  // 切换tab时查询不同数据
  const handleChangeTabs = (key: string) => {
    switch (key) {
      case 'ALL': {
        queryAll()
        break
      }
      case 'STAR': {
        queryStar()
        break
      }
    }
  }

  // 查询列表
  const initNoticeList = async () => {
    loading.value = true
    try {
      const resp = await queryListByUserId(userStore.userId,query.value)
      if (resp.code === 200) {
        total.value = resp.data.total
        resp.data.records.forEach(item => {
          // 处理标星回显
          if (item.starFlag) {
            item.starFlagNumber = Number.parseInt(item.starFlag)
          }
          // 向列表中push
          userNoticeList.value.push(item)
        })
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    } finally {
      loading.value = false
    }
  }

  return {
    open,
    userNoticeList,
    total,
    loading,
    handleChangeTabs,
    handleChangeNoticeList,
    queryMore
  }
}
const {open, userNoticeList, total, loading, handleChangeTabs, handleChangeNoticeList, queryMore} = initList()

// 初始化notice详情所需数据
const initNoticeDetail = () => {
  const noticeId = ref<string>('')

  const readNoticeDetail = (readFlag: string, id: string) => {
    // 显示详情
    showNoticeDetail(id)
    // 处理已读
    if (readFlag === '0') {
      handleRead(id)
    }
  }

  // 显示消息详情
  const showNoticeDetail = (id: string) => {
    noticeId.value = id
    previewModelOpen.value = true
    open.value = false
  }

  return {
    noticeId,
    readNoticeDetail,
    showNoticeDetail
  }
}
const {noticeId, readNoticeDetail, showNoticeDetail} = initNoticeDetail()

// 处理标星
const handleStar = async (noticeId: string, value: number) => {
  try {
    const resp = await star(noticeId, value.toString())
    if (resp.code === 200) {
      message.success(resp.msg)
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  }
}
// 处理已读
const handleRead = (id: string) => {
  read(id).then(resp => {
    if (resp.code === 200) {
      handleUnReadCount()
    } else {
      message.error(resp.msg)
    }
  }).catch(e => {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  })
}

</script>
<style scoped>
.notice-card {
  width: 340px;
  max-height: 500px;
  box-shadow: var(--lihua-light-box-shadow);
  padding: 8px;
  background-color: #ffffff;
  border-radius: 8px;
}
.notice-list-item:hover {
  background-color: rgba(0, 0, 0, 0.06);
  cursor: pointer;
  border-radius: 8px;
}
.notice-list {
  max-height: 400px
}
.notice-title {
  width: 130px
}
.notice-star {
  margin-top: -5px;
  margin-bottom: -5px;
}
.more-btn {
  width: 100%;
}
</style>
<style>
[data-theme = 'dark'] {
  .notice-list-item:hover {
    background-color: rgba(255, 255, 255, 0.12)
  }
  .notice-card {
    background-color: #1f1f1f;
  }
}
</style>
