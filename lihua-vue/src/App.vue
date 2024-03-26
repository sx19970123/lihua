<template>
  <a-config-provider :theme="themeStore.themeConfig" :locale="local">
    <router-view/>
  </a-config-provider>
</template>

<script setup lang="ts">
import { useThemeStore } from "@/stores/modules/theme"
const themeStore = useThemeStore()
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import enUS from 'ant-design-vue/es/locale/en_US';
import {ref} from "vue";
import 'dayjs/locale/zh-cn';
import dayjs from 'dayjs';
// 设置组件中文
dayjs.locale(zhCN.locale)
const local = ref(zhCN)

</script>
