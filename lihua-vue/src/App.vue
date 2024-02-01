<template>
  <a-config-provider :theme="themeStore.themeConfig">
    <router-view/>
  </a-config-provider>

</template>

<script setup lang="ts">
import { useTheme } from "@/stores/modules/theme"
const themeStore = useTheme()
</script>

<style>

</style>
