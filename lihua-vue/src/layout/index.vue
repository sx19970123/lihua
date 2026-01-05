<template>
  <div>
    <!--  小窗口导航-->
    <drawer-navigation v-if="themeStore.isSmallWindow" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
    <!--  正常导航-->
    <template v-else>
      <!--  侧边导航-->
      <side-navigation v-if="themeStore.layoutType === 'side-navigation'" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
      <!--  混合导航-->
      <mix-navigation v-if="themeStore.layoutType === 'mix-navigation'" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
      <!--  顶部导航-->
      <top-navigation v-if="themeStore.layoutType === 'top-navigation'" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
    </template>

    <!--  使用传送组件重新加载头部内容，避免刷新组件造成的重复请求  -->
    <Teleport :to="headContainer" v-if="headContainer !== null">
      <Head/>
    </Teleport>

    <!--  使用传送组件重新加载内容，避免刷新组件造成的重复请求  -->
    <Teleport :to="contentContainer" v-if="headContainer !== null">
      <Content/>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import MixNavigation from "@/layout/layout-type/MixNavigation.vue"
import SideNavigation from "@/layout/layout-type/SideNavigation.vue"
import TopNavigation from "@/layout/layout-type/TopNavigation.vue"
import DrawerNavigation from "@/layout/layout-type/DrawerNavigation.vue"
import Content from "@/layout/content/index.vue"
import Head from "@/layout/head/index.vue"
import {useThemeStore} from "@/stores/theme"
import {useViewTabsStore} from "@/stores/viewTabs.ts"
import {usePermissionStore} from "@/stores/permission.ts"
import {nextTick, onMounted, onUnmounted, ref, watch} from "vue"
import {debounce} from "lodash-es"
import settings from "@/settings.ts"

const themeStore = useThemeStore()
const viewTabsStore = useViewTabsStore()
const permissionStore = usePermissionStore()
const isMiniWindow = ref<boolean>(window.location.href.includes("miniWindow=true"))


/**
 * 初始化传送组件相关
 */
const initTeleport = () => {
  const headContainer = ref<HTMLElement | null>(null)
  const contentContainer = ref<HTMLElement | null>(null)

  // 加载传送组件容器
  const loadTeleportContainer = () => {
    nextTick(() => {
      // 头部组件容器
      headContainer.value = document.getElementById('lihua-layout-head')
      // 内容组件容器
      contentContainer.value = document.getElementById('lihua-layout-content')
    })
  }

  return {
    headContainer,
    contentContainer,
    loadTeleportContainer
  }
}

const {headContainer, contentContainer, loadTeleportContainer} = initTeleport()

// 组件切换时重新加载菜单，刷新分组导航
watch(() =>[themeStore.isSmallWindow, themeStore.layoutType], () => {
  if (themeStore.siderGroup) {
    permissionStore.reloadMenu()
  }
  // 重新加载传送组件
  loadTeleportContainer()
})

// 处理窗口拖动
const handleResize = () => {
  const isSmallWindow = document.body.offsetWidth < settings.menuToggleWidth
  themeStore.$state.isSmallWindow = isSmallWindow
  document.documentElement.setAttribute("is-small-window", String(isSmallWindow))
}

// 函数防抖
const debounceResize = debounce(handleResize, 50)

onMounted(() => {
  loadTeleportContainer()
  handleResize()
  window.addEventListener("resize", debounceResize)
})

onUnmounted(() => {
  window.removeEventListener("resize", debounceResize)
})
</script>
