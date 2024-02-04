<template>
  <side-layout v-if="themeStore.layoutType === 'sider-head' "/>
  <mixed-layout v-if="themeStore.layoutType === 'head-sider'"/>
  <head-layout v-if="themeStore.layoutType === 'head-only'"/>
</template>

<script setup lang="ts">
import mixedLayout from "@/layout/type/mixed.vue";
import sideLayout from "@/layout/type/side.vue";
import headLayout from "@/layout/type/head.vue"
import { useTheme } from "@/stores/modules/theme";
const themeStore = useTheme()
</script>
