<template>
  <div>
    <a-dropdown trigger="click" placement="bottom">
      <template  #overlay>
        <a-card :body-style="{padding: 0, width: '340px'}">
          <a-tabs centered>
            <a-tab-pane key="1">
              <template #tab>
                <span>
                  <MessageOutlined />
                </span>
                全部通知
              </template>
              <a-list item-layout="horizontal"
                      :data-source="data"
                      :split="false"
                      class="notice-list scrollbar"
              >
                <template #renderItem="{ item }">
                  <a-list-item class="notice-list-item">
                    <a-list-item-meta>
<!--                      发布时间-->
                      <template #description>
                        2020-11-11
                      </template>
<!--                      标题-->
                      <template #title>
                        <a-flex justify="space-between" align="flex-start">
                          <a-tooltip :title="item.title">
                            <a-typography-text class="notice-title" ellipsis>
                              {{ item.title }}
                            </a-typography-text>
                          </a-tooltip>
<!--                      标星-->
                          <a-rate :count="1" class="notice-star"/>
<!--                      优先级-->
                          <a-tag color="#ff4d4f">
                            紧急
                          </a-tag>
                        </a-flex>
                      </template>
<!--                      头像-->
                      <template #avatar>
                        <a-badge dot>
                          <a-avatar :style="{'background-color': themeStore.colorPrimary}">
                            <MessageOutlined />
                          </a-avatar>
                        </a-badge>
                      </template>
                    </a-list-item-meta>
                  </a-list-item>
                </template>
<!--                      加载更多-->
                <template #loadMore>
                  <a-flex align="center" justify="center">
                    <a-button type="text">加载更多</a-button>
                  </a-flex>
                </template>
              </a-list>
            </a-tab-pane>
            <a-tab-pane key="3">
              <template #tab>
                <span>
                  <StarOutlined />
                </span>
                标星通知
              </template>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </template>
<!--                      通知公告主体-->
      <div class="header-right-item header-right">
        <a-tooltip title="通知公告">
          <a-badge count="1" :offset="[-4,4]" style="color: #FFFFFF">
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
import type {SysNotice, SysNoticeDTO} from "@/api/system/noice/type/SysNotice.ts";
import {Button, notification} from "ant-design-vue";
import {h, ref} from "vue";
import {MessageOutlined, NotificationOutlined} from "@ant-design/icons-vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
import {useUserStore} from "@/stores/modules/user.ts";
import {getDictLabel, initDict} from "@/utils/Dict.ts";
import {findListByUserId} from "@/api/system/noice/Notice.ts";
const themeStore = useThemeStore();
const userStore = useUserStore()
const noticeId = ref<string>('')
const previewModelOpen = ref<boolean>(false)
const {sys_notice_type} = initDict("sys_notice_type")
interface DataItem {
  title: string;
}
const data: DataItem[] = [
  {
    title: 'Ant Design Title TitleTitle TitleTitle1',
  },
  {
    title: 'Ant Design Title 2',
  },
  {
    title: 'Ant Design Title 3',
  },
  {
    title: 'Ant Design Title 4',
  },
];
// 接收消息通知
handleSseMessage((response: SSEResponseType<SysNotice>) => {
  const {id, title, type} = response.data
  notification.open({
    message: '您有一条新' + getDictLabel(sys_notice_type.value, type),
    description: title,
    btn: () => h( Button, {
      type: "text",
      size: "small",
      onClick: () => {
        if (id) {
          // 显示详情
          showNoticeInfo(id)
          // 关闭消息提醒
          notification.close(id)
        }
      },
    }, {
      default: () => '查看详情'
    }),
    icon: () => h("0" === type ? MessageOutlined : NotificationOutlined, { style: 'color: ' + themeStore.colorPrimary}),
    key: id
  })
})

const initNoticeList = async () => {
  const query = ref<SysNoticeDTO>({
    pageNum: 0,
    pageSize: 5,
  })
  const resp = await findListByUserId(userStore.userId,query.value)
  console.log("fanhui===",resp)
}
initNoticeList()
// 显示消息详情
const showNoticeInfo = (id: string) => {
  noticeId.value = id
  previewModelOpen.value = true
}

</script>
<style scoped>
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
  margin-top: -6px;
  margin-bottom: -6px;
}
</style>
<style>
[data-theme = 'dark'] {
  .notice-list-item:hover {
    background-color: rgba(255, 255, 255, 0.12)
  }
}
</style>