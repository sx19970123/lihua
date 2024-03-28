<template>
  <a-config-provider :theme="themeStore.themeConfig" :locale="local">
    <router-view/>
  </a-config-provider>
</template>

<script setup lang="ts">
import { useThemeStore } from "@/stores/modules/theme"
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import enUS from 'ant-design-vue/es/locale/en_US';
import {ref, watch} from "vue";
import 'dayjs/locale/zh-cn';
import dayjs from 'dayjs';

const themeStore = useThemeStore()


// 国际化配置
const local = ref(zhCN)
const lang = localStorage.getItem("language") || 'cn'
if (lang === 'cn') {
  dayjs.locale(zhCN.locale)
  local.value = zhCN
}
if (lang === 'en') {
  dayjs.locale(enUS.locale)
  local.value = enUS
}

</script>
