<template>
  <a-layout>
    <div class="hct-layout-header">
      <a-layout-header style="padding-left: 0;padding-right: 0" :style="themeStore.siderTheme === 'light' ? { background: themeStore.layoutBackgroundColor } : ''">
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
      <ViewTabs class="hct-view-tabs" v-if="themeStore.showViewTabs" :style="{background: themeStore.layoutBackgroundColor}" style="padding-top: 6px"/>
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
import settings from "@/settings";
import {useThemeStore} from "@/stores/modules/theme";
const themeStore = useThemeStore()
</script>

<style scoped>
.logo {
  padding-left: 16px;
  min-width: 200px;
}
.sider {
  width: 100%;
  padding-left: 64px;
}
.hct-layout-header {
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  box-shadow: 2px 0 8px rgba(29,35,41,0.1);
}
</style>

