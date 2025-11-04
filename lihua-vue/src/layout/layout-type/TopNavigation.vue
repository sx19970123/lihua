<template>
<!--  小屏状态切换至mix-navigation布局-->
  <mix-navigation v-if="themeStore.isSmallWindow" :show-layout="props.showLayout"/>
<!--  大屏状态使用top-navigation布局-->
  <a-layout v-else>
    <div class="tn-header">
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-header class="tn-layout-header"
                         v-show="props.showLayout"
                         :style="themeStore.siderTheme === 'light' ?
                          { background: themeStore.layoutBackgroundColor } : ''">
          <a-flex align="center" justify="space-between" style="margin-right: 32px; margin-left: 32px">
            <!--logo-->
            <Logo class="logo"/>
            <!--导航-->
            <Side class="sider" v-rollDisable="true"/>
            <!--页头-->
            <Head></Head>
          </a-flex>
        </a-layout-header>
      </transition>
      <!--多标签-->
      <view-tabs v-if="themeStore.showViewTabs" :style="{background: themeStore.layoutBackgroundColor}"/>
    </div>
    <a-layout-content>
      <!--内容-->
      <Content class="layout-content"/>
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
import Logo from "@/layout/logo/index.vue";
import MixNavigation from "@/layout/layout-type/MixNavigation.vue";
import {useThemeStore} from "@/stores/theme";

const themeStore = useThemeStore()
const props = defineProps<{showLayout: boolean }>()

</script>

<style scoped>
.tn-header {
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  position: relative;
  z-index: 10;
}
.tn-layout-header {
  padding: 0;
  height: 48px;
  line-height: 48px;
  box-shadow: var(--lihua-layout-light-box-shadow);
}

.logo {
  padding-left: 8px;
}

.sider {
  flex: 1 1 0;
  min-width: 0;
  margin-left: 32px;
}
</style>

<style>
[data-head-affix = affix] {
  .tn-header {
    position: sticky;
    top: 0;
  }
}
[data-theme = dark] {
  .tn-layout-header {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
}
</style>
