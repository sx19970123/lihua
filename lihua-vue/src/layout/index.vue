<template>
  <sider-head v-if="themeStore.layoutType === 'sider-head'"/>
  <head-sider v-if="themeStore.layoutType === 'head-sider'"/>
  <head-only v-if="themeStore.layoutType === 'head-only'"/>
</template>

<script setup lang="ts">
import SiderHead from "@/layout/type/siderHead.vue";
import HeadSider from "@/layout/type/headSider.vue";
import HeadOnly from "@/layout/type/headOnly.vue";
import { useTheme } from "@/stores/modules/theme";
const themeStore = useTheme()
</script>


