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
      <a-layout-sider
          class="hsr-layout-sider"
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
        <ViewTabs  v-if="themeStore.showViewTabs" :style="{'background': themeStore.layoutBackgroundColor}"/>
        <!--内容-->
        <Content class="hsr-content layout-content"/>
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

</script>

<style scoped>

.logo {
  padding-left: 16px;
  min-width: 200px;
}
</style>

