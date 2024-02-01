<template>
  <a-config-provider :theme="{
      token: {},
      algorithm: type,
  }">
      <router-view/>
  </a-config-provider>

</template>

<script setup lang="ts">
import { theme } from 'ant-design-vue';
import { useTheme } from "@/stores/modules/theme"
import {ref, watch} from "vue";
const themeStore = useTheme()

const type = ref(theme.defaultAlgorithm)
watch(() => themeStore.theme,(value) => {
  if (value === 'dark') {
    type.value = theme.darkAlgorithm
  } else {
    type.value = theme.defaultAlgorithm
  }
})
</script>

<style>

</style>
