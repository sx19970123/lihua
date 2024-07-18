<template>
  <a-layout>
    <div class="hc-header">
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-header class="hc-layout-header" v-show="props.showLayout" :style="themeStore.siderTheme === 'light' ?
        { background: themeStore.layoutBackgroundColor } : ''">
          <a-flex align="center" justify="space-between" style="margin-right: 32px">
            <!--logo-->
            <Logo class="logo"/>
            <!--导航-->
            <Side class="sider"/>
            <!--页头-->
            <Head></Head>
          </a-flex>
        </a-layout-header>
      </transition>
      <!--多标签-->
      <view-tabs v-if="themeStore.showViewTabs" :style="{background: themeStore.layoutBackgroundColor}" style="padding-top: 6px"/>
    </div>
    <a-layout-content>
      <!--内容-->
      <Content class="layout-content"/>
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
import Logo from "@/layout/logo/index.vue";
import {useThemeStore} from "@/stores/modules/theme";
const themeStore = useThemeStore()
const props = defineProps<{  showLayout: boolean }>()


</script>

<style scoped>
.hc-header {
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%);
}
.hc-layout-header {
  padding: 0;
  height: 48px;
  line-height: 48px;
  box-shadow: var(--lihua-layout-light-box-shadow);
}
.logo {
  padding: 0 16px 0 16px;
  margin-left: 16px;
}
.sider {
  width: 100%;
  padding: 0 64px 0 64px;
}
</style>

<style>
[data-head-affix = affix] {
  .hc-header {
    position: sticky;
    top: 0;
    z-index: 2;
  }
}
[data-theme = dark] {
  .hc-layout-header {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
}
</style>
