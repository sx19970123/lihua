<template>
  <Logo/>
  <a-menu
      :items="sidebarRouters"
      mode="inline"
      theme="dark"
      @select="handleClickMenu"
  />
</template>

<script setup lang="ts">
import Logo from "@/layout/sider/components/Logo.vue";
import {usePermissionStore} from "@/stores/modules/permission";
import { useRouter } from 'vue-router'
import { computed } from "vue";

const router = useRouter()
const permissionStore = usePermissionStore()
const sidebarRouters = computed(()=> permissionStore.$state.sidebarRouters)

// 处理点击菜单
const handleClickMenu = ({item}:{item:any}) => {
  if (item.routerPath) {
    router.push(item.routerPath)
  }
}
</script>

<style scoped>

</style>
