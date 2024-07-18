<template>
  <a-layout>
    <transition :name="themeStore.routeTransition" mode="out-in">
      <a-layout-header class="hs-header" v-show="props.showLayout" :style="{'background': themeStore.layoutBackgroundColor}">
        <a-flex align="center" justify="space-between">
          <Logo class="logo"/>
          <!--页头-->
          <Head class="head"></Head>
        </a-flex>
      </a-layout-header>
    </transition>

    <a-layout>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-sider class="hs-sider"
                        v-show="props.showLayout"
                        :style="themeStore.groundGlass && themeStore.siderTheme === 'light' ? { background: themeStore.layoutBackgroundColor } : ''"
                        :theme="themeStore.siderTheme"
                        :width="themeStore.siderWith"
                        v-model:collapsed="permission.collapsed"
                        collapsible
                        breakpoint="lg"
        >
          <!-- 侧边栏-->
          <Side class="sider-content scrollbar"/>
        </a-layout-sider>
      </transition>
      <!--    菜单开合开关-->
      <a-layout-content>
        <view-tabs class="view-tabs" v-if="themeStore.showViewTabs" :style="{'background': themeStore.layoutBackgroundColor, 'top': !props.showLayout ? '0' : '' }"/>
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
.hs-header {
  z-index: 4;
  padding: 0;
  height: 48px;
  line-height: 48px;
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%);
  box-shadow: var(--lihua-layout-light-box-shadow);
}
.hs-sider {
  position: sticky;
  height: calc(100vh - 48px);
  z-index: 3;
  top: 0;
  box-shadow: var(--lihua-layout-light-box-shadow);
}
.sider-content {
  height: calc(100vh - 48px - 48px);
}
.head {
  margin-right: 32px;
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
  .hs-header {
    position: sticky;
    top: 0;
  }
  .hs-sider {
    position: sticky;
    top:48px;
  }
  .view-tabs {
    position: sticky;
    z-index: 2;
    top: 48px;
  }
}
[data-theme = dark] {
  .hs-header {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
  .hs-sider {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
}
</style>

