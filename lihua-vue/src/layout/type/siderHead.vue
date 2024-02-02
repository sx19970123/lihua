<template>
  <a-layout class="layout">
    <a-layout-sider class="layout-sider sider-height scrollbar"
                    :style="{'background': themeStore.headBackgroundColor}"
                    :theme="themeStore.siderTheme"
                    :trigger="null"
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
                       :class="themeStore.affixHead ? 'layout-position' : ''"
                       :style="{'background': themeStore.headBackgroundColor}">
        <a-flex align="center" justify="space-between">
          <HeadCollapsed/>
          <!--页头-->
          <Head></Head>
        </a-flex>
        <ViewTabs  v-if="themeStore.showViewTags"/>
      </a-layout-header>
      <a-layout-content>
        <!--内容-->
        <Content/>
      </a-layout-content>
      <!--页脚-->
      <a-layout-footer></a-layout-footer>
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
import HeadCollapsed from "@/layout/collapsed/index.vue";
const themeStore = useTheme()
const permission = usePermissionStore()


</script>

<style scoped>
.layout {
  min-height: 100vh;
}
.layout-header {
  height: auto;
  padding-left: 0px;
  padding-right: 0px;
  backdrop-filter: blur(6px);
}
.layout-position {
  position: sticky;
  top: 0px;
  z-index: 1;
}
.layout-sider {
  position: sticky;
  top: 0px;
  z-index: 2;
  box-shadow: 2px 0 8px rgba(29,35,41,0.05);
  backdrop-filter: blur(6px);


}
.sider-height {
  height: 100vh;
}
.logo {
  margin: 16px;
}
</style>

