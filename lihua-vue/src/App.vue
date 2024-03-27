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
const lang = localStorage.getItem("language")
console.log(lang)
// 设置组件中文
watch(() => lang, (value) => {
  if (value === 'cn') {
    dayjs.locale(zhCN.locale)
    local.value = zhCN
  }
  if (value === 'en') {
    dayjs.locale(enUS.locale)
    local.value = enUS
  }
},{
  deep: true
})

</script>
