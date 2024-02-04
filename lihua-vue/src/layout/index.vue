<template>
<!--  <side-layout v-if="themeStore.layoutType === 'sider-head'"/>-->
  <mixed-layout v-if="themeStore.layoutType === 'head-sider' || true"/>
<!--  <head-layout v-if="themeStore.layoutType === 'head-only'"/>-->
</template>

<script setup lang="ts">
import mixedLayout from "@/layout/type/mixed.vue";
import { useTheme } from "@/stores/modules/theme";
const themeStore = useTheme()
</script>
