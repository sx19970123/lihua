<template>
  <a-spin :spinning="spinning">
    <div style="margin: 32px">
      <a-flex vertical :gap="32" align="center">
        <a-flex vertical align="center">
          <!--    标题-->
          <a-typography-title>{{notice.title}}</a-typography-title>
          <!--    发布时间-->
          <a-space>
            <a-typography-text type="secondary">admin</a-typography-text>
            <a-typography-text type="secondary" v-if="notice.status === '1'">{{dayjs(notice.releaseTime).format('YYYY-MM-DD HH:mm')}}</a-typography-text>
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
        <div id="preview"/>
      </a-flex>
    </div>
  </a-spin>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from "vue";
import {findById, findReadInfo} from "@/api/system/noice/Notice.ts";
import Vditor from "vditor";
import UserShow from "@/components/user-show/index.vue"
import {useThemeStore} from "@/stores/modules/theme.ts";
import type {SysNoticeVO} from "@/api/system/noice/type/SysNotice.ts";
import dayjs from "dayjs";
import {message} from "ant-design-vue";
import {getUserOptionByUserIds} from "@/api/system/user/User.ts";
import type {SysUser} from "@/api/system/user/type/SysUser.ts";
const themeStore = useThemeStore();
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
const preview = async () => {
  spinning.value = true
  const noticeId = props.noticeId
  // 根据id查询
  const resp = await findById(noticeId)
  if (resp.code === 200) {
    notice.value = resp.data
    // 通知内容回显
    const dom = document.getElementById('preview')
    if (dom instanceof HTMLDivElement && resp.data.content) {
      await Vditor.preview(dom, resp.data.content, {
        mode: themeStore.isDarkTheme ? "dark" : "light",
        cdn: themeStore.isDarkTheme ?'/vditor/preview' : '/vditor/normal',
      })
    }
    spinning.value = false

    // 创建人回显
    if (notice.value.createId) {
      await handleCreateUser(notice.value.createId)
    }
    // 已读/未读回显
    if (props.showReadUser) {
      await handleReadInfo(noticeId)
    }
  } else {
    message.error(resp.msg)
  }
}

const handleCreateUser = async (createId: string) => {
  const createUser = await getUserOptionByUserIds([createId])
  notice.value.createUser = createUser.data[0].nickname
}

// 处理已读未读用户显示
const handleReadInfo = async (noticeId: string) => {
  const resp = await findReadInfo(noticeId)
  if (resp.code === 200) {
    const readInfoData = resp.data
    const unRead = readInfoData["0"] as SysUser[]
    const read = readInfoData["1"] as SysUser[]
    console.log("unRead", unRead)
    console.log("read", read)
    // 未读
    if (unRead) {
      unreadUserList.value = unRead
    }
    // 已读
    if (read) {
      readUserList.value = read
    }
  }
}

onMounted(() => {
  preview()
})

watch(() => props.noticeId, () => {
  preview()
})
</script>

<style scoped>
.read-content {
  max-width: 250px;
  max-height: 400px;
}
</style>