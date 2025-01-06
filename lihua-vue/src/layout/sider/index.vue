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
import {computed, nextTick, onMounted, reactive, watch} from "vue";
const themeStore = useThemeStore()
const route = useRoute()
const router = useRouter()
// pinia 中获取菜单数据
const permission = usePermissionStore()
const viewTabsStore = useViewTabsStore()
// 抛出函数，当路由发生变化时，抛出函数
const emits = defineEmits(['routeChange'])
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
  let routeInfo = viewTabsStore.getViewTabsByKey(key)
  if (!routeInfo) {
    routeInfo = viewTabsStore.getTotalTabByKey(key)
  }
  if (routeInfo) {
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

  // 确保菜单可以及时响应
  nextTick(() => {
    if (!collapsed && siderMode !== 'horizontal') {
      state.openKeys = route.matched.map(r => r.path)
    } else {
      state.openKeys = []
    }
  })

}

// 赋值展开节点
onMounted(() => {
  setOpenKeys()
})

// 监听路由变化
watch(()=> route.matched,()=> {
  emits('routeChange')
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
