<template>
  <a-layout>
    <a-layout-header class="layout-header" :class="themeStore.affixHead ? 'layout-position' : ''" :style="{'background': themeStore.headBackgroundColor}">
      <a-flex align="center" justify="space-between">
        <!--logo-->
        <Logo class="logo"/>
        <!--导航-->
        <Side class="sider"/>
        <!--页头-->
        <Head></Head>
      </a-flex>
      <!--多标签-->
      <ViewTabs v-if="themeStore.showViewTags" style="padding-top: 6px" :style="{'background': themeStore.headBackgroundColor}"/>
    </a-layout-header>

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
import Logo from "@/layout/logo/index.vue";
import {useTheme} from "@/stores/modules/theme";
const themeStore = useTheme()

</script>

<style scoped>
.logo {
  padding-left: 16px;
}

.sider {
  width: 100%;
  padding-left: 64px;
}

.layout-header {
  padding-left: 0px;
  padding-right: 0px;
  backdrop-filter: blur(6px);
}
.layout-position {
  position: sticky;
  top: 0px;
  z-index: 1;
  height: auto;
}
</style>

