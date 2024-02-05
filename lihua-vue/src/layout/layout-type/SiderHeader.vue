<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider class="shr-layout-sider sider-height scrollbar"
                    :style="themeStore.groundGlass && themeStore.siderTheme === 'light' ? { background: themeStore.layoutBackgroundColor } : ''"
                    :theme="themeStore.siderTheme"
                    :trigger="null"
                    :width="themeStore.siderWith"
                    v-model:collapsed="permission.collapsed"
                    collapsible
                    breakpoint="lg"
    >
      <Logo class="logo"/>
      <!-- 侧边栏-->
      <Side/>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="shr-layout-header"
                       :style="{'background': themeStore.layoutBackgroundColor}">
        <a-flex align="center" justify="space-between">
          <HeadCollapsed/>
          <!--页头-->
          <Head></Head>
        </a-flex>
        <ViewTabs  v-if="themeStore.showViewTags"/>
      </a-layout-header>
      <a-layout-content class="layout-content">
        <!--内容-->
        <Content/>
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
import HeadCollapsed from "@/layout/component/collapsed/index.vue";
const themeStore = useThemeStore()
const permission = usePermissionStore()

</script>

<style scoped>
.shr-layout-header {
  height: auto;
  padding-left: 0;
  padding-right: 0;
  backdrop-filter: blur(6px);
  box-shadow: 2px 0 8px rgba(29,35,41,0.1);
  line-height: 48px;
}
.shr-layout-sider {
  position: sticky;
  top: 0px;
  z-index: 2;
  box-shadow: 2px 0 8px rgba(29,35,41,0.05);
  backdrop-filter: blur(6px);
}
.logo {
  margin: 16px;
}
</style>

