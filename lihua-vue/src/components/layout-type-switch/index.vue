<template>
  <a-form layout="horizontal">
    <a-typography-title :level="5">暗色模式</a-typography-title>
    <a-form-item>
      <head-theme-switch/>
    </a-form-item>
    <a-typography-title :level="5">导航模式</a-typography-title>
    <a-form-item>
      <a-radio-group v-model:value="themeStore.layoutType" @change="themeStore.changeLayoutType()">
        <a-radio value="sider-header">侧边导航</a-radio>
        <a-radio value="header-sider">混合导航</a-radio>
        <a-radio value="header-content">顶部导航</a-radio>
      </a-radio-group>
    </a-form-item>
    <a-typography-title :level="5">样式主题</a-typography-title>
    <a-form-item label="主题颜色">
      <a-radio-group v-model:value="themeStore.colorPrimary" @change="themeStore.changeColorPrimary()">
        <a-radio value="rgb(22, 119, 255)">拂晓蓝</a-radio>
        <a-radio value="rgb(245, 34, 45)">薄暮</a-radio>
        <a-radio value="rgb(250, 84, 28)">火山</a-radio>
        <a-radio value="rgb(250, 173, 20)">日暮</a-radio>
        <a-radio value="rgb(19, 194, 194)">明青</a-radio>
        <a-radio value="rgb(82, 196, 26)">极光绿</a-radio>
        <a-radio value="rgb(47, 84, 235)">极客蓝</a-radio>
        <a-radio value="rgb(114, 46, 209)">酱紫</a-radio>
      </a-radio-group>
    </a-form-item>
    <a-form-item label="导航栏颜色" v-if="!themeStore.dataDark">
      <a-radio-group v-model:value="themeStore.siderTheme" @change="themeStore.changeSiderTheme()">
        <a-radio value="light">亮色</a-radio>
        <a-radio value="dark">暗色</a-radio>
      </a-radio-group>
    </a-form-item>
    <a-form-item label="导航栏宽度">
      <a-slider v-model:value="themeStore.siderWith" :max="400" :min="80" :step="10"></a-slider>
    </a-form-item>
    <a-form-item label="毛玻璃效果">
      <a-switch v-model:checked="themeStore.groundGlass" @change="themeStore.changeGroundGlass"></a-switch>
    </a-form-item>
    <a-form-item label="固定头部">
      <a-switch v-model:checked="themeStore.affixHead" @change="themeStore.changeAffixHead()"></a-switch>
    </a-form-item>
     <a-form-item label="快捷标签">
       <a-switch v-model:checked="themeStore.showViewTags"/>
     </a-form-item>
    <a-form-item label="路由动画" v-model:value="themeStore.routeTransition">
      <a-select>
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

</template>
<script setup lang="ts">
import HeadThemeSwitch from "@/components/layout-type-switch/component/HeadThemeSwitch.vue";
import { useThemeStore } from "@/stores/modules/theme";

const themeStore = useThemeStore()
</script>
<style scoped>

</style>
