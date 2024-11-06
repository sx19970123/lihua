<template>
  <sider-header v-if="themeStore.layoutType === 'sider-header'" :show-layout="viewTabsStore.$state.showLayout"/>
  <header-sider v-if="themeStore.layoutType === 'header-sider'" :show-layout="viewTabsStore.$state.showLayout"/>
  <header-content v-if="themeStore.layoutType === 'header-content'" :show-layout="viewTabsStore.$state.showLayout"/>
</template>

<script setup lang="ts">
import HeaderSider from "@/layout/layout-type/HeaderSider.vue";
import SiderHeader from "@/layout/layout-type/SiderHeader.vue";
import HeaderContent from "@/layout/layout-type/HeaderContent.vue"
import { useThemeStore } from "@/stores/theme";
import { useViewTabsStore } from "@/stores/viewTabs.ts";
const themeStore = useThemeStore()
const viewTabsStore = useViewTabsStore()


</script>
