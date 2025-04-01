<template>
  <a-config-provider :theme="themeStore.themeConfig" :locale="local" :component-size="themeStore.componentSize">
<!--    浏览器兼容提示-->
    <a-alert type="warning" closable banner v-if="showOldBrowserAlert()">
      <template #message>
        当前浏览器版本过低，部分功能可能无法正常使用，请升级浏览器版本，推荐使用
          <a-typography-link href="https://www.google.com/intl/zh-CN/chrome/" target="_blank">Chrome</a-typography-link>&nbsp;
          <a-typography-link href="https://www.microsoft.com/zh-cn/edge/" target="_blank">Edge</a-typography-link>&nbsp;
          <a-typography-link href="https://www.apple.com.cn/safari/" target="_blank">Safari</a-typography-link>&nbsp;
        等现代浏览器。
      </template>
    </a-alert>
<!--    网站主要内容-->
    <router-view/>
  </a-config-provider>
</template>

<script setup lang="ts">
import {getBrowserType, getBrowserMajorVersion} from "@/utils/Browser.ts"
import { useThemeStore } from "@/stores/theme"
import { usePermissionStore } from "@/stores/permission.ts";
import {useSettingStore} from "@/stores/setting.ts";
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import {onMounted, onUnmounted, ref, watch} from "vue";
import 'dayjs/locale/zh-cn';
import dayjs from 'dayjs';
import type {GrayModel} from "@/api/system/setting/type/GrayModel.ts";
import {theme} from "ant-design-vue";
const { token } = theme.useToken()
const themeStore = useThemeStore()
const permissionStore = usePermissionStore()
// 应用html-root主题颜色
themeStore.changeDocumentElement(token.value.colorPrimary)

// 初始化系统配置
const settingStore = useSettingStore()

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

// 加载灰色模式
const initGrayModel = async () => {
  const grayModel = await settingStore.getSetting<GrayModel>('GrayModelSetting');
  if (grayModel?.closeTime && dayjs() > dayjs(grayModel.closeTime)) {
    themeStore.enableGrayModel(false)
  } else {
    themeStore.enableGrayModel(grayModel?.enable)
  }
}

// 加载主题相关
const initTheme = () => {
  // 匹配系统主题
  const marchSystemTheme = matchMedia('(prefers-color-scheme: dark)')

  // 处理跟随系统主题
  const handleFollowSystemTheme = () => {
    // 开启跟随系统后暗色模式由App.vue控制
    if (themeStore.$state.isServerLoad && themeStore.followSystemTheme) {
      themeStore.isDarkTheme = marchSystemTheme.matches
      themeStore.changeDataDark()
      marchSystemTheme.addEventListener('change', handleFollowSystemTheme)
    } else {
      marchSystemTheme.removeEventListener('change', handleFollowSystemTheme)
    }
  }

  // 将主题同步到其他标签页
  const syncTabTheme = (event: StorageEvent) => {
    // 同步亮色/暗色模式
    if (event.key === 'data-theme') {
      themeStore.isDarkTheme = event.newValue === 'dark'
      themeStore.changeDataDark(true)
    }
    // 同步其他主题
    if (event.key === 'theme' && event.newValue) {
      themeStore.init(event.newValue)
      permissionStore.reloadMenu()
    }
  }

  return {
    handleFollowSystemTheme,
    syncTabTheme
  }

}
const {handleFollowSystemTheme, syncTabTheme} = initTheme()

// 监听token.value.colorPrimary修改html-root中主题颜色
watch(() => token.value.colorPrimary, () => {
  themeStore.changeDocumentElement(token.value.colorPrimary)
})

// 监听主题跟随系统
watch(() => themeStore.followSystemTheme && themeStore.$state.isServerLoad, () => {
  handleFollowSystemTheme()
})

onMounted(() => {
  // 主题跟随系统
  handleFollowSystemTheme()
  // 灰色模式
  initGrayModel()
  // 启用监听storage以同步标签页间主题
  window.addEventListener('storage', syncTabTheme)
})

onUnmounted(() => {
  // 删除storage监听
  window.removeEventListener('storage', syncTabTheme)
})

</script>

