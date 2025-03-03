<template>
  <sider-header v-if="themeStore.layoutType === 'sider-header'" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
  <header-sider v-if="themeStore.layoutType === 'header-sider'" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
  <header-content v-if="themeStore.layoutType === 'header-content'" :show-layout="!isMiniWindow && viewTabsStore.$state.showLayout"/>
</template>

<script setup lang="ts">
import HeaderSider from "@/layout/layout-type/HeaderSider.vue";
import SiderHeader from "@/layout/layout-type/SiderHeader.vue";
import HeaderContent from "@/layout/layout-type/HeaderContent.vue"
import { useThemeStore } from "@/stores/theme";
import { useViewTabsStore } from "@/stores/viewTabs.ts";
import {ref} from "vue";
const themeStore = useThemeStore()
const viewTabsStore = useViewTabsStore()
const isMiniWindow = ref<boolean>(window.location.href.includes("miniWindow=true"))
</script>
