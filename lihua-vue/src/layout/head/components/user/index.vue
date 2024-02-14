<template>
  <a-dropdown>
    <a-avatar :size="32" class="avatar">USER</a-avatar>
    <template #overlay>
      <a-menu style="min-width: 220px" @click="handleClickMenu">
        <a-menu-item disabled>
          <a-flex align="center">
            <a-avatar :size="40" class="avatar">USER</a-avatar>
            <a-flex vertical>
              <a-typography-text :copyable="{ tooltip: false }" strong>{{userStore.$state.name}}</a-typography-text>
              <a-typography-text :copyable="{ tooltip: false }">
                <a-tooltip placement="bottom" title="用户唯一id">
                  {{userStore.$state.userInfo.id}}
                </a-tooltip>
              </a-typography-text>
            </a-flex>
          </a-flex>
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item key="user-center">
          <UserOutlined />
          <span>个人中心</span>
        </a-menu-item>
        <a-sub-menu key="user-switch">
          <template #title>
            <UserSwitchOutlined />
            <span>切换账号</span>
          </template>
          <a-menu-item key="user-add">
            <PlusOutlined />
            <span>添加账号</span>
          </a-menu-item>
        </a-sub-menu>
        <a-menu-divider/>
        <a-menu-item danger key="logout">
          <LogoutOutlined />
          <span>退出登陆</span>
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import {useUserStore} from "@/stores/modules/user";
import {useRouter} from "vue-router";

const userStore = useUserStore()
const router = useRouter()
// 处理点击个人中心按钮
const handleClickMenu = ({key}: {key: string}) => {
  switch (key) {
    case 'user-center': {
      userInfo()
      break
    }
    case 'user-add': {
      break
    }
    case 'logout': {
      break
    }
  }
}
// 跳转至个人中心
const userInfo = () => {
  router.push('/user')
}
// 新增切换账号
const userAdd = () => {

}
// 退出登陆
const logout = () => {

}
</script>

<style scoped>
span {
  margin-left: 8px;
}
</style>

