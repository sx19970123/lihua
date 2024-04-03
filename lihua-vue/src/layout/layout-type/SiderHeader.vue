<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider class="shr-layout-sider scrollbar"
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
        <Head/>
        <ViewTabs  v-if="themeStore.showViewTabs"/>
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
import Logo from "@/layout/logo/index.vue";
import { usePermissionStore } from "@/stores/modules/permission";
import {useThemeStore} from "@/stores/modules/theme";
const themeStore = useThemeStore()
const permission = usePermissionStore()

</script>

<style scoped>
.shr-layout-header {
  height: auto;
  padding-left: 0;
  padding-right: 0;
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  box-shadow: 2px 0 8px rgba(29,35,41,0.1);
  line-height: 48px;
}
.shr-layout-sider {
  position: sticky;
  height: 100vh;
  top: 0px;
  z-index: 2;
  box-shadow: 2px 0 8px rgba(29,35,41,0.05);
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
}
.logo {
  position: sticky;
  margin: 16px;
}
</style>

