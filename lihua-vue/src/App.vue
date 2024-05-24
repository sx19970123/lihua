<template>
  <a-config-provider :theme="themeStore.themeConfig" :locale="local">
<!--    浏览器兼容提示-->
    <a-alert type="warning" closable banner v-if="isTargetBrowser">
      <template #message>
        当前浏览器版本过低，部分功能可能无法正常使用，请升级浏览器版本，推荐使用
          <a-typography-link href="https://www.google.com/intl/zh-CN/chrome/" target="_blank">Chrome</a-typography-link>&nbsp;
          <a-typography-link href="https://www.microsoft.com/zh-cn/edge/" target="_blank">Edge</a-typography-link>&nbsp;
          <a-typography-link href="https://www.apple.com.cn/safari/" target="_blank">Safari</a-typography-link>&nbsp;
        等主流浏览器。
      </template>
    </a-alert>
<!--    网站主要内容-->
    <router-view v-show="!viewAlert"/>
<!--    浏览器窗口过小时全屏覆盖-->
    <div class="full-alert full-alert-color" v-if="viewAlert">
      <a-typography-text class="alertText">显示窗口过小</a-typography-text>
    </div>
  </a-config-provider>
</template>

<script setup lang="ts">
import {getBrowserType, getBrowserMajorVersion} from "@/utils/browser.ts"
import { useThemeStore } from "@/stores/modules/theme"
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import enUS from 'ant-design-vue/es/locale/en_US';
import {onMounted, ref} from "vue";
import 'dayjs/locale/zh-cn';
import dayjs from 'dayjs';
const themeStore = useThemeStore()

// 国际化配置
const local = ref(zhCN)
const lang = localStorage.getItem("language") || 'cn'
if (lang === 'cn') {
  dayjs.locale(zhCN.locale)
  //local.value = zhCN
}
if (lang === 'en') {
  dayjs.locale(enUS.locale)
  //local.value = enUS
}

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
      return true
    }
    case 'Firefox': {
      return true
    }
    default: {
      return true
    }
  }
}

const isTargetBrowser = showOldBrowserAlert()

// 当浏览器窗口小于指定大小时，使用遮罩进行覆盖
const viewAlert = ref<boolean>(false)
// 监听窗口大小
onMounted(() => {
  window.addEventListener('resize', updateViewportSize)
})
// 当窗口大小发生变化时执行
const updateViewportSize = () => {
  viewAlert.value = window.innerWidth < 600 || window.innerHeight < 200;
}
updateViewportSize()
</script>

<style>
.full-alert {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  z-index: 9999;
}
[data-theme = dark] {
  .full-alert-color {
    background-color: rgba(0,0,0,0.7);
  }
}

[data-theme = light] {
  .full-alert-color {
    background-color: rgba(255,255,255,0.7);
  }
}

.alertText {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
    font-size: 24px;
  }
</style>
