<template>
  <a-layout>
    <a-layout-header class="hct-layout-header"
                     :style="themeStore.siderTheme === 'light' ? { background: themeStore.layoutBackgroundColor } : ''">

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
    <ViewTabs class="hct-view-tabs" v-if="themeStore.showViewTags" :style="{background: themeStore.layoutBackgroundColor}" style="padding-top: 6px"/>
    <a-layout-content>
      <!--内容-->
      <Content/>
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
import Logo from "@/layout/component/logo/index.vue";
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
  height: auto;
  padding-left: 0;
  padding-right: 0;
  backdrop-filter: blur(6px);
  box-shadow: 2px 0 8px rgba(29,35,41,0.1);
  line-height: 48px;
}
</style>

