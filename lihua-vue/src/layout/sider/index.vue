<template>
  <a-menu
      class="menu"
      :theme="themeStore.siderTheme"
      :mode="themeStore.siderMode"
      v-model:selected-keys="state.selectedKeys"
      v-model:open-keys="state.openKeys"
      :style="themeStore.layoutType === 'header' ? { width: themeStore.siderWith + 'px'} : ''"
      style="border-inline-end: none"
      :items="menu"
      @select="handleClickMenuItem"
  />
</template>

<script setup lang="ts">
import {usePermissionStore} from "@/stores/modules/permission";
import {useThemeStore} from "@/stores/modules/theme";
import {useViewTabsStore} from "@/stores/modules/viewTabs.ts";
import {useRoute,useRouter} from "vue-router";
import {computed, onMounted, reactive, watch} from "vue";
const themeStore = useThemeStore()
const route = useRoute()
const router = useRouter()
// pinia 中获取菜单数据
const permission = usePermissionStore()
const viewTabsStore = useViewTabsStore()
const menu = computed(() => permission.menuRouters)
const state = reactive({
  selectedKeys: route.matched.map(r => r.path),
  openKeys: route.matched.map(r => r.path)
})

// 点击菜单跳转路由
const handleClickMenuItem = ({ key }: {key: string}) => {
  const targetRoute = viewTabsStore.totalViewTabs.filter(tab => tab.routerPathKey === key)
  if (targetRoute && targetRoute.length > 0) {
    const routeInfo = targetRoute[0]
    // 路由跳转
    router.push({
      path: routeInfo.routerPathKey,
      query: routeInfo.query ? JSON.parse(routeInfo.query) : undefined,
    })
  }
}

// 导航回显
watch(()=> route.matched,(value)=> {
  state.selectedKeys = route.matched.map(r => r.path)
  state.openKeys = value.map(r => r.path)
})
// 收起/展开
watch(() => permission.collapsed,(value) => {
  value? themeStore.foldSiderWidth(): themeStore.unfoldSiderWidth()
})
</script>
<style scoped>
.menu {
  border: none;
  background-color: rgba(255,255,255,0)
}
</style>
