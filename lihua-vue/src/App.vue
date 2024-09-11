<template>
  <a-config-provider :theme="themeStore.themeConfig" :locale="local" :component-size="themeStore.componentSize">
<!--    浏览器兼容提示-->
    <a-alert type="warning" closable banner v-if="isOldBrowser">
      <template #message>
        当前浏览器版本过低，部分功能可能无法正常使用，请升级浏览器版本，推荐使用
          <a-typography-link href="https://www.google.com/intl/zh-CN/chrome/" target="_blank">Chrome</a-typography-link>&nbsp;
          <a-typography-link href="https://www.microsoft.com/zh-cn/edge/" target="_blank">Edge</a-typography-link>&nbsp;
          <a-typography-link href="https://www.apple.com.cn/safari/" target="_blank">Safari</a-typography-link>&nbsp;
        等主流浏览器。
      </template>
    </a-alert>
<!--    网站主要内容-->
    <router-view/>
  </a-config-provider>
</template>

<script setup lang="ts">
import {getBrowserType, getBrowserMajorVersion} from "@/utils/Browser.ts"
import { useThemeStore } from "@/stores/modules/theme"
import {useSettingStore} from "@/stores/modules/setting.ts";
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import {ref} from "vue";
import 'dayjs/locale/zh-cn';
import dayjs from 'dayjs';
const themeStore = useThemeStore()
// 初始化系统配置
const settingStore = useSettingStore()
settingStore.init()
// 配置中文
const local = ref(zhCN)
dayjs.locale(zhCN.locale)

// 当浏览器版本过低时，显示浏览器兼容性提示
const showOldBrowserAlert = () => {
  const browserType = getBrowserType()
  const version = getBrowserMajorVersion()

  switch (browserType) {
    case 'Chrome': {
      return version < 111
    }
    case 'Opera': {
      return version < 97
    }
    case 'Safari': {
      return false
    }
    case 'Firefox': {
      return true
    }
    default: {
      return true
    }
  }
}

const isOldBrowser = showOldBrowserAlert()
</script>

