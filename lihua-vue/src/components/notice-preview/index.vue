<template>
  <a-spin :spinning="spinning">
    <div style="margin: var(--lihua-space-xl)">
      <a-flex vertical :gap="32" align="center">
        <a-flex vertical align="center">
          <!--    标题-->
          <a-typography-title>{{notice.title}}</a-typography-title>
          <!--    发布时间-->
          <a-space>
            <a-tooltip title="发布用户">
              <a-typography-text type="secondary">{{notice.releaseUser}}</a-typography-text>
            </a-tooltip>
            <a-tooltip title="发布时间">
              <a-typography-text type="secondary" v-if="notice.status === '1'">{{dayjs(notice.releaseTime).format('YYYY-MM-DD HH:mm')}}</a-typography-text>
            </a-tooltip>
<!--            已读未读-->
            <a-popover placement="bottom">
              <template #content>
                  <a-flex v-if="readUserList.length > 0 || unreadUserList.length > 0">
                    <div v-if="readUserList.length > 0">
                      <a-flex :gap="8">
                        <a-typography-title :level="5">已读</a-typography-title>
                        <a-typography-text type="secondary">{{readUserList.length + '/' +(readUserList.length + unreadUserList.length)}}</a-typography-text>
                      </a-flex>
                      <div class="read-content scrollbar">
                        <a-flex wrap="wrap">
                          <user-show v-for="user in readUserList" :avatar-json="user.avatar" :nickname="user.nickname"/>
                        </a-flex>
                      </div>
                    </div>
                    <a-divider type="vertical" style="height: auto" v-if="readUserList.length > 0 && unreadUserList.length > 0"/>
                    <div v-if="unreadUserList.length > 0">
                      <a-flex :gap="8">
                        <a-typography-title :level="5">未读</a-typography-title>
                        <a-typography-text type="secondary">{{unreadUserList.length + '/' +(readUserList.length + unreadUserList.length)}}</a-typography-text>
                      </a-flex>
                      <div class="read-content scrollbar">
                        <a-flex wrap="wrap">
                          <user-show v-for="user in unreadUserList" :avatar-json="user.avatar" :nickname="user.nickname"/>
                        </a-flex>
                      </div>
                    </div>
                  </a-flex>
                  <a-empty v-else/>
              </template>
              <a-typography-text type="secondary" style="cursor: pointer" v-if="showReadUser && notice.status === '1'">
                <EyeOutlined />
              </a-typography-text>
            </a-popover>
          </a-space>
        </a-flex>
        <!--    文章内容-->
        <div v-html="notice.content"/>
      </a-flex>
    </div>
  </a-spin>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from "vue";
import {preview, queryReadInfo} from "@/api/system/notice/Notice.ts";
import UserShow from "@/components/user-show/index.vue"
import type {SysNoticeVO} from "@/api/system/notice/type/SysNotice.ts";
import dayjs from "dayjs";
import {message} from "ant-design-vue";
import type {SysUser} from "@/api/system/user/type/SysUser.ts";

const props = defineProps<{
  noticeId: string,
  showReadUser?: boolean
}>()

// 加载中
const spinning = ref<boolean>(false)
// notice 对象
const notice = ref<SysNoticeVO>({})
// 已读用户
const readUserList = ref<SysUser[]>([])
// 未读用户
const unreadUserList = ref<SysUser[]>([])

// 预览
const handlePreview = async () => {
  spinning.value = true
  const noticeId = props.noticeId
  // 后端查询预览
  const resp = await preview(noticeId)
  if (resp.code === 200) {
    notice.value = resp.data
    spinning.value = false
    // 已读/未读回显
    if (props.showReadUser) {
      await handleReadInfo(noticeId)
    }
  } else {
    message.error(resp.msg)
  }
}

// 处理已读未读用户显示
const handleReadInfo = async (noticeId: string) => {
  const resp = await queryReadInfo(noticeId)
  if (resp.code === 200) {
    const readInfoData = resp.data
    const unRead = readInfoData["0"] as SysUser[]
    const read = readInfoData["1"] as SysUser[]
    // 未读
    if (unRead) {
      unreadUserList.value = unRead
    }
    // 已读
    if (read) {
      readUserList.value = read
    }
  } else {
    message.error(resp.msg)
  }
}

onMounted(() => {
  handlePreview()
})

watch(() => props.noticeId, () => {
  handlePreview()
})
</script>

<style scoped>
.read-content {
  max-width: 250px;
  max-height: 400px;
}
</style>
