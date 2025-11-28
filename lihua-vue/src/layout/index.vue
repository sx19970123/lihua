<template>
  <!--  侧边导航-->
  <side-navigation v-if="themeStore.layoutType === 'side-navigation'" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
  <!--  混合导航-->
  <mix-navigation v-if="themeStore.layoutType === 'mix-navigation'" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
  <!--  顶部导航-->
  <top-navigation v-if="themeStore.layoutType === 'top-navigation'" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
</template>

<script setup lang="ts">
import MixNavigation from "@/layout/layout-type/MixNavigation.vue";
import SideNavigation from "@/layout/layout-type/SideNavigation.vue";
import TopNavigation from "@/layout/layout-type/TopNavigation.vue"
import {useThemeStore} from "@/stores/theme";
import {useViewTabsStore} from "@/stores/viewTabs.ts";
import {onMounted, onUnmounted, ref} from "vue";
import {debounce} from "lodash-es"
import settings from "@/settings.ts";

const themeStore = useThemeStore()
const viewTabsStore = useViewTabsStore()
const isMiniWindow = ref<boolean>(window.location.href.includes("miniWindow=true"))

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
