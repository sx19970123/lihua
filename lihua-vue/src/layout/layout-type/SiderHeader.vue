<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider class="sider scrollbar"
                    v-show="props.showLayout"
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
      <a-layout-header class="layout-header"
                       :style="{'background': themeStore.layoutBackgroundColor}">
        <Head class="header" v-show="props.showLayout"/>
        <view-tabs  v-if="themeStore.showViewTabs"/>
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
const props = defineProps<{  showLayout: boolean }>()
</script>

<style scoped>
.layout-header {
  height: auto;
  padding: 0;
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  line-height: 48px;
}
.header {
  box-shadow: 0 1px 4px rgba(0,21,41,.12);
}
.sider {
  position: sticky;
  height: 100vh;
  top: 0;
  z-index: 2;
  box-shadow: 0 1px 4px rgba(0,21,41,.12);
}
.logo {
  margin: 16px;
}
</style>

<style>
[data-head-affix = affix] {
  .layout-header {
    position: sticky;
    top: 0;
    z-index: 1;
  }
}
</style>

