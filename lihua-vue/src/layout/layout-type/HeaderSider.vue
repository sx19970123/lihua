<template>
  <a-layout>
    <a-layout-header class="header" v-show="props.showLayout" :style="{'background': themeStore.layoutBackgroundColor}">
      <a-flex align="center" justify="space-between">
        <Logo class="logo"/>
        <!--页头-->
        <Head></Head>
      </a-flex>
    </a-layout-header>

    <a-layout>
      <a-layout-sider class="sider scrollbar"
                      v-show="props.showLayout"
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
        <view-tabs class="view-tabs" style="top: 0" v-if="themeStore.showViewTabs" :style="{'background': themeStore.layoutBackgroundColor}"/>
        <!--内容-->
        <Content class="layout-content"/>
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
import Logo from "@/layout/logo/index.vue";
import {useThemeStore} from "@/stores/modules/theme";
const themeStore = useThemeStore()
const permission = usePermissionStore()
const props = defineProps<{  showLayout: boolean }>()
</script>

<style scoped>
.header {
  z-index: 4;
  padding: 0;
  height: 48px;
  line-height: 48px;
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%);
  box-shadow: 0 1px 4px rgba(0,21,41,.12);
}
.sider {
  position: sticky;
  height: calc(100vh - 48px);
  z-index: 3;
  top: 0;
  box-shadow: 0 1px 4px rgba(0,21,41,.12);
}
.logo {
  padding: 0 16px 0 16px;
  margin-left: 16px;
}
.view-tabs {
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%);
}
</style>

<style>
[data-head-affix = affix] {
  .header {
    position: sticky;
    top: 0;
  }
  .sider {
    position: sticky;
    top:48px;
  }
  .view-tabs {
    position: sticky;
    z-index: 2;
    top: 48px;
  }
}
</style>

