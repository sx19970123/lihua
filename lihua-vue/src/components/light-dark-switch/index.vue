<template>
  <div style="padding-right: 16px;">
    <a-switch v-model:checked="themeStore.dataDark" @change="themeStore.changeDataDark" :style="{'background': themeStore.dataDark ? '#00008B' : '#2196F3'}">
      <template #checkedChildren>
        <Moon/>
      </template>
      <template #unCheckedChildren>
        <Sun/>
      </template>
    </a-switch>
  </div>
</template>
<script setup lang="ts">
import { useThemeStore } from "@/stores/modules/theme";
import Sun from "@/components/icon/sun/Sun.vue";
import Moon from "@/components/icon/moon/Moon.vue";
const themeStore = useThemeStore()
</script>