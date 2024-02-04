<template>
  <sider-header v-if="themeStore.layoutType === 'sider-header'"/>
  <header-sider v-if="themeStore.layoutType === 'header-sider'"/>
  <header-content v-if="themeStore.layoutType === 'header-content'"/>
</template>

<script setup lang="ts">
import HeaderSider from "@/layout/layout-type/HeaderSider.vue";
import SiderHeader from "@/layout/layout-type/SiderHeader.vue";
import HeaderContent from "@/layout/layout-type/HeaderContent.vue"
import { useThemeStore } from "@/stores/modules/theme";
const themeStore = useThemeStore()
</script>
