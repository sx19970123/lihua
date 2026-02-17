<template>
  <a-menu
      class="menu"
      style="border-inline-end: none"
      :items="menu ? menu : defaultMenu"
      :theme="siderTheme ? siderTheme : themeStore.siderTheme"
      :mode="siderMode"
      v-model:selected-keys="state.selectedKeys"
      v-model:open-keys="state.openKeys"
      @select="handleClickMenuItem"
  />
</template>

<script setup lang="ts">
import {usePermissionStore} from "@/stores/permission";
import {useThemeStore} from "@/stores/theme";
import {useViewTabsStore} from "@/stores/viewTabs.ts";
import {useRoute, useRouter} from "vue-router";
import {computed, nextTick, onMounted, reactive, watch} from "vue";
import type {ItemType} from "ant-design-vue";

const themeStore = useThemeStore()
const route = useRoute()
const router = useRouter()
// pinia 中获取菜单数据
const permissionStore = usePermissionStore()
const viewTabsStore = useViewTabsStore()
// 抛出函数，当路由发生变化时，抛出函数
const emits = defineEmits(['routeChange', 'menuClick', 'mounted'])
// 外部传入属性
const {siderMode, menu, selectedKeys, openKeys, siderTheme, isMixTop} = defineProps<{
  // 导航类型
  siderMode: 'inline' | 'horizontal',
  // 菜单
  menu?: ItemType[],
  // 选中菜单key
  selectedKeys?: string[],
  // 展开菜单key
  openKeys?: string[],
  // 菜单颜色
  siderTheme?: string,
  // 是否为混合布局的顶栏
  isMixTop?: boolean,
}>()

const defaultMenu = computed(() => permissionStore.menuRouters)

const state = reactive<{
  selectedKeys: string[],
  openKeys: string[],
}>({
  selectedKeys: route.matched.map(r => r.path),
  openKeys: []
})

// 点击菜单跳转路由
const handleClickMenuItem = ({ key }: {key: string}) => {
  // 菜单点击
  emits('menuClick', key)
  // 判断viewTabs中是否存在key
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
  const collapsed = permissionStore.collapsed

  // 确保菜单可以及时响应
  nextTick(() => {
    if (!collapsed && siderMode !== 'horizontal') {
      state.openKeys = route.matched.map(r => r.path)
    } else {
      state.openKeys = []
    }
  })

}

onMounted(() => {
  emits('mounted', state.selectedKeys)
  // 赋值展开节点
  setOpenKeys()
})

defineExpose({
  handleClickMenuItem
})

// 监听路由变化
watch(()=> route.matched,()=> {
  state.selectedKeys = route.matched.map(r => r.path)
  emits('routeChange', state.selectedKeys)
  setOpenKeys()
})

// 收起/展开
watch(() => permissionStore.collapsed,(value) => {
  if (isMixTop) {
    return
  }
  value? themeStore.foldSiderWidth(): themeStore.unfoldSiderWidth()
  setOpenKeys()
})

// 选中菜单变化
watch(() => selectedKeys, () => {
  state.selectedKeys = selectedKeys || []
})

// 打开菜单变化
watch(() => openKeys, () => {
  state.openKeys = openKeys || []
})
</script>

<style scoped>
.menu {
  border: none;
  background-color: var(--lihua-alpha-level-0);
}
</style>
