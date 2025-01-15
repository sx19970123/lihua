<template>
  <a-dropdown trigger="click" :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
    <a-tooltip title="个人中心" placement="bottom" :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
      <div class="header-right-item header-right">
        <user-avatar :size="34" :value="userStore.avatar.value" :background-color="userStore.avatar.backgroundColor" :type="userStore.avatar.type" :url="userStore.avatar.url"/>
      </div>
    </a-tooltip>
    <template #overlay>
      <a-menu class="user-card" @click="handleClickMenu">
        <a-menu-item key="user-overview">
          <a-flex align="center" :gap="12">
            <user-avatar :size="48" :value="userStore.avatar.value" :background-color="userStore.avatar.backgroundColor" :type="userStore.avatar.type" :url="userStore.avatar.url"/>
            <a-flex vertical>
              <a-typography-text ellipsis :copyable="{ tooltip: false }" strong v-model:content="userStore.$state.nickname" style="max-width: 120px"/>
              <a-tooltip :title="userStore.$state.userId" placement="bottom" :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
                <a-typography-text ellipsis :copyable="{ tooltip: false }" v-model:content="userStore.$state.userId" style="max-width: 120px"/>
              </a-tooltip>
            </a-flex>
            <RightOutlined class="input-prefix-icon-color" style="position: absolute; right: 8px"/>
          </a-flex>
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item key="user-center">
          <a-flex :gap="8">
            <UserOutlined />
            <span>个人中心</span>
          </a-flex>
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item key="user-data-update">
          <a-flex :gap="8">
            <CloudSyncOutlined />
            <span>数据更新</span>
          </a-flex>
        </a-menu-item>
        <a-menu-divider v-if="isAdmin || isVisitor"/>
        <a-menu-item key="admin-setting" v-if="isAdmin || isVisitor">
          <a-flex :gap="8">
            <SettingOutlined />
            <span>系统设置</span>
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
import { useUserStore } from "@/stores/user";
import { useRouter,useRoute } from "vue-router";
import {message} from "ant-design-vue";
import {reloadData} from "@/api/system/auth/Auth.ts";
import { init } from "@/utils/AppInit.ts";
import {useViewTabsStore} from "@/stores/viewTabs.ts";
import {useThemeStore} from "@/stores/theme.ts";
import {ref} from "vue";
import {ResponseError} from "@/api/global/Type.ts";
const viewTabsStore = useViewTabsStore()
const themeStore = useThemeStore()
const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

const isAdmin = ref<boolean>(userStore.$state.roleCodes.filter(item => item === 'ROLE_admin').length > 0)
const isVisitor = ref<boolean>(userStore.$state.roleCodes.filter(item => item === 'ROLE_visitor').length > 0)
// 处理点击个人中心按钮
const handleClickMenu = async ({key}: {key: string}) => {
  switch (key) {
    case 'user-overview': {
      userInfo()
      break
    }
    case 'user-center': {
      userInfo()
      break
    }
    case 'admin-setting': {
      settingPage()
      break
    }
    case 'user-data-update': {
      try {
        const resp = await reloadData()
        if (resp.code === 200) {
          init().then(() => {
                // 重新加载ViewTag
                viewTabsStore.init(route)
                // 重新生成key
                viewTabsStore.regenerateComponentKey()
                message.success(resp.msg)
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
      }
      break
    }
    case 'logout': {
      await logout()
      break
    }
  }
}

// 跳转至个人中心
const userInfo = () => {
  router.push('/profile')
}

// 跳转至设置页面
const settingPage = () => {
  router.push('/setting')
}

// 退出登陆
const logout = async () => {
  await userStore.handleLogout()
  message.success("退出成功")
  await router.push('/login')
}
</script>

<style scoped>
.user-card {
  width: 220px;
  box-shadow: var(--lihua-light-box-shadow);
}
</style>
