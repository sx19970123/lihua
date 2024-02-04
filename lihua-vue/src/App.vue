<template>
  <a-config-provider :theme="themeStore.themeConfig">
    <router-view/>
  </a-config-provider>

</template>

<script setup lang="ts">
import { useThemeStore } from "@/stores/modules/theme"
const themeStore = useThemeStore()
</script>

<style>

</style>
