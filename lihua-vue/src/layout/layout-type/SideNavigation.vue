<template>
  <a-layout class="layout">
    <!--   左侧导航   -->
    <transition :name="themeStore.routeTransition" mode="out-in">
      <a-layout-sider :class="themeStore.siderTheme === 'light' ? 'background-glass' : ''"
                      class="side-navigation-sider"
                      v-show="props.showLayout"
                      :theme="themeStore.siderTheme"
                      :trigger="null"
                      :width="themeStore.siderWith"
                      v-model:collapsed="permissionStore.collapsed"
                      collapsible
                      breakpoint="xl"
      >
        <Logo class="logo" :show-title="!permissionStore.collapsed"/>
        <!-- 侧边栏-->
        <div class="sider sider-scrollbar">
          <Side sider-mode="inline"/>
        </div>
      </a-layout-sider>
    </transition>
    <!--   右侧head和content   -->
    <a-layout>
      <a-layout-header class="side-navigation-header background-glass">
        <transition :name="themeStore.routeTransition" mode="out-in">
          <!--    菜单收缩-->
          <a-flex class="side-navigation-head" justify="space-between" v-show="props.showLayout">
            <a-flex align="center" :gap="16">
              <!--菜单开关-->
              <HeadCollapsed/>
              <!--面包屑 宽度不足时隐藏-->
              <Breadcrumb/>
            </a-flex>
            <!-- 右侧头部-->
            <div id="lihua-layout-head"/>
          </a-flex>
        </transition>
        <view-tabs v-if="themeStore.showViewTabs"/>
      </a-layout-header>
      <a-layout-content>
        <!--内容-->
        <div id="lihua-layout-content" class="layout-content" />
      </a-layout-content>
      <!--页脚-->
      <a-layout-footer class="layout-footer" v-if="themeStore.$state.showFooter">
        <page-footer/>
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue";
import Logo from "@/layout/logo/index.vue";
import {usePermissionStore} from "@/stores/permission";
import {useThemeStore} from "@/stores/theme";
import HeadCollapsed from "@/layout/head/components/collapsed/index.vue";
import Breadcrumb from "@/layout/head/components/breadcrumb/index.vue";
import PageFooter from "@/layout/footer/index.vue";

const themeStore = useThemeStore()
const permissionStore = usePermissionStore()
const props = defineProps<{showLayout: boolean}>()
</script>

<style scoped>
.side-navigation-header {
  z-index: 3;
  height: auto;
  padding: 0;
  backdrop-filter: var(--lihua-backdrop-filter-lg);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-lg);
  line-height: var(--lihua-layout-height);
}
.side-navigation-head {
  box-shadow: var(--lihua-layout-box-shadow);
  padding-right: var(--lihua-layout-head-space);
}
.sider {
  height: calc(100vh - var(--lihua-layout-height));
}
.logo {
  padding: var(--lihua-space-sm) var(--lihua-space-base)
}
.side-navigation-sider {
  position: sticky;
  height: 100vh;
  top: 0;
  z-index: 4;
  box-shadow: var(--lihua-layout-box-shadow);
}
</style>

<style lang="scss">
[head-affix = enable] {
  .side-navigation-header {
    position: sticky;
    top: 0;
  }
}
</style>

