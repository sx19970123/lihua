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
import {usePermissionStore} from "@/stores/permission";
import {useThemeStore} from "@/stores/theme";
import {useViewTabsStore} from "@/stores/viewTabs.ts";
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
const setOpenKeys = () => {
  // 展开收起状态
  const collapsed = permission.collapsed
  // 导航类型
  const siderMode = themeStore.siderMode

  // 确保菜单可以及时响应，增加setTimeout
  setTimeout(() => {
    if (!collapsed && siderMode !== 'horizontal') {
      state.openKeys = route.matched.map(r => r.path)
    } else {
      state.openKeys = []
    }
  },0)

}

// 赋值展开节点
onMounted(() => {
  setOpenKeys()
})

// 导航回显
watch(()=> route.matched,(value)=> {
  state.selectedKeys = route.matched.map(r => r.path)
  setOpenKeys()
})
// 收起/展开
watch(() => permission.collapsed,(value) => {
  value? themeStore.foldSiderWidth(): themeStore.unfoldSiderWidth()
  setOpenKeys()
})
</script>
<style scoped>
.menu {
  border: none;
  background-color: rgba(255,255,255,0)
}
</style>
