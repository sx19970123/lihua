<template>
  <div>
    <a-form layout="vertical">
      <!-- 主题设置 -->
      <a-typography-title :level="5">{{$t('profile.theme')}}</a-typography-title>
      <a-form-item>
        <head-theme-switch/>
      </a-form-item>
      <a-form-item :label="$t('profile.themeColor')">
        <color-select v-model:items="colorList" v-model="themeStore.colorPrimary" @click="themeStore.changeColorPrimary()"/>
      </a-form-item>
      <a-form-item :label="$t('profile.navigationColor')" v-if="!themeStore.dataDark">
        <nav-color-select v-model="themeStore.siderTheme" @click="themeStore.changeSiderTheme()"/>
      </a-form-item>
      <a-divider/>

      <!-- 布局设置 -->
      <a-typography-title :level="5">{{$t('profile.layout')}}</a-typography-title>
      <a-form-item :label="$t('profile.navigationType')">
        <nav-select v-model="themeStore.layoutType" @click="themeStore.changeLayoutType()"/>
      </a-form-item>
      <a-form-item :label="$t('profile.navigationWidth')" v-if="themeStore.layoutType !== 'header-content'">
        <a-slider v-model:value="themeStore.siderWith" @change="themeStore.changeSiderWidth()" dots :max="400" :min="80" :step="20" style="width: 230px"></a-slider>
      </a-form-item>
      <a-form-item :label="$t('profile.fixedHeader')">
        <a-switch v-model:checked="themeStore.affixHead" @change="themeStore.changeAffixHead()"></a-switch>
      </a-form-item>
      <a-form-item :label="$t('profile.viewTags')">
        <a-switch v-model:checked="themeStore.showViewTags"/>
      </a-form-item>
      <a-divider/>

      <!-- 其他设置 -->
      <a-typography-title :level="5">{{$t('profile.other')}}</a-typography-title>
      <a-form-item :label="$t('profile.glass')">
        <a-switch v-model:checked="themeStore.groundGlass" @change="themeStore.changeGroundGlass"></a-switch>
      </a-form-item>
      <a-form-item :label="$t('profile.animation')">
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
      <a-form-item>
        <a-button type="primary" @click="userStore.saveTheme(JSON.stringify(themeStore.$state))">{{$t('global.submit')}}</a-button>
      </a-form-item>
    </a-form>
  </div>

</template>
<script setup lang="ts">
import HeadThemeSwitch from "@/components/light-dark-switch/index.vue";
import ColorSelect from "@/components/color-select/index.vue"
import NavSelect from "@/components/nav-type-select/index.vue"
import NavColorSelect from "@/components/nav-color-select/index.vue"
import settings from "@/settings";
import { useUserStore } from "@/stores/modules/user";
import { useThemeStore } from "@/stores/modules/theme";
import {onUnmounted, ref} from "vue";

const themeStore = useThemeStore()
const userStore = useUserStore()

// 主题颜色
const colorList = ref<Array<{name: string,color: string}>>(settings.colorOptions)

// 卸载组件时触发，保存用户修改的内容
onUnmounted(()=> {
  userStore.saveTheme(JSON.stringify(themeStore.$state));
})

</script>
<style scoped>

</style>
