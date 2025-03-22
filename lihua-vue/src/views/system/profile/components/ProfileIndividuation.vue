<template>
  <a-card>
    <a-form layout="vertical">
      <!-- 主题设置 -->
      <a-typography-title :level="5">主题设置</a-typography-title>
      <a-form-item v-if="!themeStore.followSystemTheme">
        <head-theme-switch/>
      </a-form-item>
      <a-form-item label="跟随系统">
        <a-switch v-model:checked="themeStore.followSystemTheme"/>
      </a-form-item>
      <a-form-item label="主题颜色">
        <color-select :dataSource="colorList" v-model:color="themeStore.colorPrimary" @click="themeStore.changeColorPrimary()"/>
      </a-form-item>
      <a-form-item label="导航颜色" v-if="!themeStore.isDarkTheme">
        <nav-color-select v-model="themeStore.siderTheme" @click="themeStore.changeSiderTheme"/>
      </a-form-item>
      <a-divider/>

      <!-- 布局设置 -->
      <a-typography-title :level="5">布局设置</a-typography-title>
      <a-form-item label="导航类型">
        <nav-select v-model="themeStore.layoutType" @click="themeStore.changeLayoutType" @change="handleChangeLayoutType"/>
      </a-form-item>
      <a-form-item label="导航宽度" v-if="themeStore.layoutType !== 'header-content'">
        <a-slider v-model:value="themeStore.siderWith" @change="themeStore.changeSiderWidth" dots :max="400" :min="80" :step="20" style="width: 230px"></a-slider>
      </a-form-item>
      <a-form-item label="布局尺寸">
        <a-radio-group v-model:value="themeStore.componentSize">
          <a-radio value="small">更小</a-radio>
          <a-radio value="default">适中（推荐）</a-radio>
          <a-radio value="large">更大</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="分组导航" v-if="themeStore.layoutType !== 'header-content'">
        <a-switch v-model:checked="themeStore.siderGroup" @change="handleChangeSiderGroup"/>
      </a-form-item>
      <a-form-item label="固定头部">
        <a-switch v-model:checked="themeStore.affixHead" @change="themeStore.changeAffixHead"></a-switch>
      </a-form-item>
      <a-form-item label="多任务栏" v-if="viewTabsStore.$state.showLayout && !isMiniWindow">
        <a-switch v-model:checked="themeStore.showViewTabs" @change="themeStore.changeShowViewTabs"/>
      </a-form-item>
      <a-divider/>

      <!-- 其他设置 -->
      <a-typography-title :level="5">其他设置</a-typography-title>
      <a-form-item label="高级材质">
        <a-switch v-model:checked="themeStore.groundGlass" @change="themeStore.changeGroundGlass"></a-switch>
      </a-form-item>
      <a-form-item label="切换动画">
        <a-select style="width: 200px" v-model:value="themeStore.routeTransition">
          <a-select-option value="zoom">变焦</a-select-option>
          <a-select-option value="fade">淡入淡出</a-select-option>
          <a-select-option value="breathe">呼吸</a-select-option>
          <a-select-option value="top">上升</a-select-option>
          <a-select-option value="down">切换</a-select-option>
          <a-select-option value="switch">交换</a-select-option>
          <a-select-option value="trick">整活</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-card>

</template>
<script setup lang="ts">
import HeadThemeSwitch from "@/components/light-dark-switch/index.vue";
import ColorSelect from "@/components/color-select/index.vue"
import NavSelect from "@/components/nav-type-select/index.vue"
import NavColorSelect from "@/components/nav-color-select/index.vue"
import settings from "@/settings";
import { useUserStore } from "@/stores/user";
import { useThemeStore } from "@/stores/theme";
import { usePermissionStore } from "@/stores/permission.ts";
import { useViewTabsStore } from "@/stores/viewTabs.ts";
import {onUnmounted, ref} from "vue";
import {ResponseError} from "@/api/global/Type.ts";
import {message} from "ant-design-vue";
const themeStore = useThemeStore()
const userStore = useUserStore()
const permissionStore = usePermissionStore()
const viewTabsStore = useViewTabsStore()
// 主题颜色
const colorList = ref<Array<{name: string,color: string}>>(settings.colorOptions)
const submitLoading = ref<boolean>(false)
const isMiniWindow = ref<boolean>(window.location.href.includes("miniWindow=true"))
// 卸载组件时触发，保存用户修改的内容
onUnmounted(()=> {
  handleSaveTheme()
})

// 处理保存主题
const handleSaveTheme = async () => {
  submitLoading.value = true
  try {
    await userStore.saveTheme(JSON.stringify(themeStore.$state))
  } catch (e) {
     if (e instanceof ResponseError) {
       message.error(e.msg)
     } else {
       console.log(e)
     }
  } finally {
    submitLoading.value = false
  }
}

// 处理修改菜单分组模式
const handleChangeSiderGroup = () => {
  permissionStore.reloadMenu()
}
const handleChangeLayoutType = (key: string) => {
  if (key === "header-content") {
    themeStore.$state.siderGroup = false
    handleChangeSiderGroup()
  }
}

// 刷新页面时触发保存主题
window.addEventListener("beforeunload", handleSaveTheme);
</script>