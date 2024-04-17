<template>
  <a-layout>
    <div class="header">
      <a-layout-header class="layout-header" v-show="false" :style="themeStore.siderTheme === 'light' ?
      { background: themeStore.layoutBackgroundColor } : ''">
        <a-flex align="center" justify="space-between">
          <!--logo-->
          <Logo class="logo"/>
          <!--导航-->
          <Side class="sider"/>
          <!--页头-->
          <Head></Head>
        </a-flex>
      </a-layout-header>
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
</script>

<style scoped>
.header {
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%);
}
.layout-header {
  padding: 0;
  height: 48px;
  line-height: 48px;
  box-shadow: 0 1px 4px rgba(0,21,41,.12);
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
.header {
 position: sticky;
 z-index: 2;
}
</style>
