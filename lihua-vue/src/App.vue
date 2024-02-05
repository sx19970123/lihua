<template>
  <a-config-provider :theme="themeStore.themeConfig">
    <router-view/>
  </a-config-provider>

</template>

<script setup lang="ts">
import { useThemeStore } from "@/stores/modules/theme"
const themeStore = useThemeStore()
</script>

<style lang="scss">
#nprogress .bar {
  background: var(--theme-color) !important; //自定义颜色
}
#nprogress .peg {
  box-shadow: 0 0 10px red, 0 0 5px red !important;
}
</style>
