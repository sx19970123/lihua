<template>
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

</template>

<script setup lang="ts">
import MixNavigation from "@/layout/layout-type/MixNavigation.vue";
import SideNavigation from "@/layout/layout-type/SideNavigation.vue";
import TopNavigation from "@/layout/layout-type/TopNavigation.vue"
import DrawerNavigation from "@/layout/layout-type/DrawerNavigation.vue"
import {useThemeStore} from "@/stores/theme";
import {useViewTabsStore} from "@/stores/viewTabs.ts";
import {usePermissionStore} from "@/stores/permission.ts";
import {onMounted, onUnmounted, ref, watch} from "vue";
import {debounce} from "lodash-es"
import settings from "@/settings.ts";

const themeStore = useThemeStore()
const viewTabsStore = useViewTabsStore()
const permissionStore = usePermissionStore()
const isMiniWindow = ref<boolean>(window.location.href.includes("miniWindow=true"))

// 组件切换时重新加载菜单，刷新分组导航
watch(() =>[themeStore.isSmallWindow, themeStore.layoutType], () => {
  if (themeStore.siderGroup) {
    permissionStore.reloadMenu()
  }
})

// 处理窗口拖动
const handleResize = () => {
  themeStore.$state.isSmallWindow = document.body.offsetWidth < settings.menuToggleWidth
}

// 函数防抖
const debounceResize = debounce(handleResize, 0)

onMounted(() => {
  handleResize()
  window.addEventListener("resize", debounceResize)
})

onUnmounted(() => {
  window.removeEventListener("resize", debounceResize)
})
</script>
