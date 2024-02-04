<template>
  <a-layout>
    <a-layout-header class="hsr-layout-header" :style="{'background': themeStore.layoutBackgroundColor}">
      <a-flex align="center" justify="space-between">
        <Logo class="logo"/>
        <!--页头-->
        <Head></Head>
      </a-flex>
    </a-layout-header>

    <a-layout>
      <a-layout-sider class="hsr-layout-sider sider-height scrollbar"
                      :style="themeStore.groundGlass && themeStore.siderTheme === 'light' ? { background: themeStore.layoutBackgroundColor } : ''"
                      :theme="themeStore.siderTheme"
                      :width="themeStore.siderWith"
                      v-model:collapsed="permission.collapsed"
                      collapsible
                      breakpoint="lg"
      >
        <!-- 侧边栏-->
        <Side/>
      </a-layout-sider>
      <!--    菜单开合开关-->
      <a-layout-content>
        <ViewTabs class="hsr-view-tabs" v-if="themeStore.showViewTags" :style="{'background': themeStore.layoutBackgroundColor}"/>
        <!--内容-->
        <Content class="hsr-content"/>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
import { usePermissionStore } from "@/stores/modules/permission";
import Logo from "@/layout/component/logo/index.vue";
import {useThemeStore} from "@/stores/modules/theme";
const themeStore = useThemeStore()
const permission = usePermissionStore()

</script>

<style scoped>

.logo {
  padding: 0 0 0 16px;
}

.hsr-layout-header {
  height: auto;
  padding-left: 0;
  padding-right: 0;
  backdrop-filter: blur(6px);
  box-shadow: 2px 0 8px rgba(29,35,41,0.1);
  line-height: 48px;
}

</style>

