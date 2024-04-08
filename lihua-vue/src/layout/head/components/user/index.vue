<template>
  <a-dropdown>
    <div class="header-right-item header-right">
      <user-avatar :size="32" :value="userStore.avatar.value" :background-color="userStore.avatar.backgroundColor" :type="userStore.avatar.type" :url="userStore.avatar.url"/>
    </div>
    <template #overlay>
      <a-menu style="min-width: 220px" @click="handleClickMenu">
        <a-menu-item disabled style="cursor: default">
          <a-flex align="center" :gap="12">
            <user-avatar :size="48" :value="userStore.avatar.value" :background-color="userStore.avatar.backgroundColor" :type="userStore.avatar.type" :url="userStore.avatar.url"/>
            <a-flex vertical>
              <a-typography-text :copyable="{ tooltip: false }" strong>
                <a-tooltip placement="bottom" title="用户昵称">
                  {{userStore.$state.name}}
                </a-tooltip>
              </a-typography-text>
              <a-typography-text :copyable="{ tooltip: false }">
                <a-tooltip placement="bottom" title="用户uid">
                  {{userStore.$state.userInfo.id}}
                </a-tooltip>
              </a-typography-text>
            </a-flex>
          </a-flex>
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item key="user-center">
          <a-flex :gap="8">
            <UserOutlined />
            <span>个人中心</span>
          </a-flex>
        </a-menu-item>
        <a-menu-item key="user-data-update">
          <a-flex :gap="8">
            <CloudSyncOutlined />
            <span>数据更新</span>
          </a-flex>
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item danger key="logout">
          <a-flex :gap="8">
            <LogoutOutlined />
            <span>退出登陆</span>
          </a-flex>
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import UserAvatar from "@/components/user-avatar/index.vue"
import { useUserStore } from "@/stores/modules/user";
import { useRouter,useRoute } from "vue-router";
import {message} from "ant-design-vue";
import {reloadData} from "@/api/system/login/login.ts";
import {reloadLoginUser} from "@/utils/user.ts";
import {useViewTabsStore} from "@/stores/modules/viewTabs.ts";

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
// 处理点击个人中心按钮
const handleClickMenu = async ({key}: {key: string}) => {
  switch (key) {
    case 'user-center': {
      userInfo()
      break
    }
    case 'user-data-update': {
      const resp = await reloadData()
      if (resp.code === 200) {
        reloadLoginUser()
            .then(() => {
              // 重新加载ViewTag，逻辑与ViewTag组件内相同
              initViewTag()
              message.success(resp.msg)
              router.push('/system/menu')
            })
            .catch((err) => message.error('拉取用户信息失败：' + err))
      } else {
        message.error(resp.msg)
      }
      break
    }
    case 'logout': {
      logout()
      break
    }
  }
}
// 重新加载ViewTag
const initViewTag = () => {
  const viewTabsStore = useViewTabsStore()
  if (route?.meta?.viewTab) {
    viewTabsStore.selectedViewTab(route.path,route?.meta?.viewTab as boolean)
  }
  // 跳过标签管理
  else {
    // 当前组件为跳过，默认选中其父组件
    const unSkipList =  route.matched.filter(item => item?.meta?.viewTab && item.path !== '/')
    if (unSkipList && unSkipList.length > 0) {
      // 选中接收view-tabs托管的父组件
      viewTabsStore.selectedViewTab(unSkipList[unSkipList.length - 1].path, route?.meta?.viewTab as boolean)
    }
  }
}
// 跳转至个人中心
const userInfo = () => {
  router.push('/profile')
}
// 退出登陆
const logout = () => {
  userStore.logout()
}
</script>
