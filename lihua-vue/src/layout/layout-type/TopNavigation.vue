<template>
  <a-layout class="layout">
    <div class="top-navigation-header">
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-header class="top-navigation-layout-header"
                         :class="themeStore.siderTheme === 'light' ? 'background-glass' : ''"
                         v-show="props.showLayout">
          <a-flex align="center"
                  style="margin: 0 var(--lihua-layout-head-space) 0 var(--lihua-layout-head-space)">
            <!--logo-->
            <Logo class="logo"/>
            <!--导航-->
            <Side class="sider" sider-mode="horizontal" v-rollDisable="true"/>
            <!--页头-->
            <div id="lihua-layout-head"/>
          </a-flex>
        </a-layout-header>
      </transition>
      <!--多标签-->
      <view-tabs v-if="themeStore.showViewTabs" class="background-glass"/>
    </div>
    <a-layout-content>
      <!--内容-->
      <div id="lihua-layout-content" class="layout-content" />
    </a-layout-content>
    <!--页脚-->
    <a-layout-footer class="layout-footer" v-if="themeStore.$state.showFooter">
      <page-footer/>
    </a-layout-footer>
  </a-layout>
</template>

<script setup lang="ts">
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue";
import PageFooter from "@/layout/footer/index.vue";
import Logo from "@/layout/logo/index.vue";
import {useThemeStore} from "@/stores/theme";

const themeStore = useThemeStore()
const props = defineProps<{showLayout: boolean }>()
</script>

<style scoped>
.top-navigation-header {
  backdrop-filter: var(--lihua-backdrop-filter-lg);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-lg);
  position: relative;
  z-index: 10;
}
.top-navigation-layout-header {
  position: relative;
  z-index: 10;
  padding: 0;
  height: var(--lihua-layout-height);
  line-height: var(--lihua-layout-height);
  box-shadow: var(--lihua-layout-box-shadow);
}

.logo {
  padding-left: var(--lihua-space-sm);
}

.sider {
  flex: 1 1 0;
  min-width: 0;
  margin-left: var(--lihua-layout-head-space);
}
</style>

<style>
[head-affix = enable] {
  .top-navigation-header {
    position: sticky;
    top: 0;
  }
}
</style>
