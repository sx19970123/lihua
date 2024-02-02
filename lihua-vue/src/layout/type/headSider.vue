<template>
  <a-layout>
    <a-layout-header class="layout-header" :style="{'background': themeStore.headBackgroundColor}">
      <a-flex align="center" justify="space-between">
        <Logo class="logo"/>
        <!--页头-->
        <Head></Head>
      </a-flex>
    </a-layout-header>

    <a-layout style="position: sticky;">
      <a-layout-sider class="layout-sider sider-height scrollbar"
                      :style="{'background': themeStore.headBackgroundColor}"
                      :theme="themeStore.siderTheme"
                      :trigger="null"
                      v-model:collapsed="permission.collapsed"
                      collapsible
                      breakpoint="lg"
      >
        <!-- 侧边栏-->
        <Side/>
      </a-layout-sider>
      <!--    菜单开合开关-->
      <a-layout-content  style="padding-top: 64px">
        <ViewTabs class="viewTabs" v-if="themeStore.showViewTags" :style="{'background': themeStore.headBackgroundColor}"/>
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
import Logo from "@/layout/logo/index.vue";
import {useTheme} from "@/stores/modules/theme";
const themeStore = useTheme()
const permission = usePermissionStore()


</script>

<style scoped>

.logo {
  padding: 0px;
  padding-left: 16px
}

.viewTabs {
  width: 100%;
  position: sticky;
  top: 64px;
  padding-top: 6px;
  z-index: 1;
  backdrop-filter: blur(6px);
}

.layout-header {
  position: fixed;
  width: 100%;
  top: 0px;
  z-index: 2;
  height: auto;
  padding-left: 0px;
  padding-right: 0px;
  backdrop-filter: blur(6px);
  box-shadow: 2px 0 8px rgba(29,35,41,0.1);

}

.layout-sider {
  position: sticky;
  top: 0px;
  z-index: 2;
  padding-top: 70px;
  backdrop-filter: blur(6px);
  box-shadow: 2px 0 8px rgba(29,35,41,0.1);

}

.sider-height {
  height: 100vh;
}
</style>

