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
const state = reactive<{
  selectedKeys: string[],
  openKeys: string[]
}>({
  selectedKeys: route.matched.map(r => r.path),
  openKeys: []
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

// 获取可展开的节点
// 当菜单未收起并且不为顶部导航时，设置展开节点
const getOpenKeys = (): string[] => {
  // 展开收起状态
  const collapsed = permission.collapsed
  // 导航类型
  const siderMode = themeStore.siderMode

  if (!collapsed && siderMode !== 'horizontal') {
    return route.matched.map(r => r.path)
  } else {
    return []
  }
}

// 赋值展开节点
onMounted(() => {
  state.openKeys = getOpenKeys()
})

// 导航回显
watch(()=> route.matched,(value)=> {
  state.selectedKeys = route.matched.map(r => r.path)
  state.openKeys = getOpenKeys()
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
