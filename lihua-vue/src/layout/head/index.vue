<template>
  <a-flex justify="space-between">
    <a-select style="width: 100px;position: fixed;top: 16px;right: 100px" @change="themeStore.changeLayoutType">
      <a-select-option value="head-only">顶部导航栏</a-select-option>
      <a-select-option value="head-sider">顶部-侧边</a-select-option>
      <a-select-option value="sider-head">侧边-顶部</a-select-option>
    </a-select>
    <head-theme-switch/>
  </a-flex>
</template>
<script setup lang="ts">
import HeadThemeSwitch from "@/layout/head/components/HeadThemeSwitch.vue";
import {useTheme} from "@/stores/modules/theme";
const themeStore = useTheme()
</script>

<style scoped>

</style>
