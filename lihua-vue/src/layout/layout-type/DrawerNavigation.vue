<template>
  <div>
    <a-layout>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-header class="drawer-navigation-header background-glass" v-show="props.showLayout">
          <a-flex align="center" justify="space-between">
            <Logo class="logo" :show-title="false"/>
            <!--页头-->
            <Head class="head"/>
          </a-flex>
        </a-layout-header>
      </transition>

      <a-layout>
        <transition :name="themeStore.routeTransition" mode="out-in">
          <a-layout-sider class="drawer-navigation-sider"
                          v-show="props.showLayout"
                          :theme="themeStore.siderTheme"
                          :width="themeStore.siderWith"
                          v-model:collapsed="permissionStore.collapsed"
                          :collapsedWidth="0"
                          @collapse="handleChangeCollapse"
                          :trigger="showMask ? null : ''"
                          collapsible
          >
            <!-- 窗口缩小到阈值后特殊侧边栏logo-->
            <a-flex align="center" justify="center" class="sider-logo">
              <Logo :max-width="themeStore.siderWith"/>
            </a-flex>
            <!-- 侧边栏-->
            <Side class="sider-scrollbar min-sider-content" @route-change="handleRouteChange"/>
          </a-layout-sider>
        </transition>
        <!--    菜单开合开关-->
        <a-layout-content>
          <view-tabs class="view-tabs background-glass" v-if="themeStore.showViewTabs" :style="{'top': !props.showLayout ? '0' : '' }"/>
          <!--内容-->
          <Content class="layout-content"/>
        </a-layout-content>
      </a-layout>
    </a-layout>
    <!--  小窗口菜单遮罩  -->
    <Mask :show-mask="showMask" :z-index="100" @click="handleCloseSider"/>
  </div>
</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
import { usePermissionStore } from "@/stores/permission";
import Logo from "@/layout/logo/index.vue";
import {useThemeStore} from "@/stores/theme";
import {ref} from "vue";
import Mask from "@/components/mask/index.vue";
const themeStore = useThemeStore()
const permissionStore = usePermissionStore()

// 是否显示layout
const props = defineProps<{ showLayout: boolean }>()

// 小屏下抽屉样式遮罩
const showMask = ref<boolean>(false)

// 展开时打开遮罩
const handleChangeCollapse = (collapsed: boolean) => {
  if (!collapsed) {
    showMask.value = true
  }
}

// 处理关闭菜单
const handleCloseSider = () => {
  permissionStore.collapsed = true
  showMask.value = false
}

// 路由变化时自动关闭菜单
const handleRouteChange = () => {
  if (showMask.value) {
    handleCloseSider()
  }
}
</script>

<style scoped>
.drawer-navigation-header {
  z-index: 5;
  padding: 0;
  height: var(--lihua-layout-height);
  line-height: var(--lihua-layout-height);
  backdrop-filter: var(--lihua-backdrop-filter-lg);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-lg);
  box-shadow: var(--lihua-layout-box-shadow);
}

.drawer-navigation-sider {
  z-index: 101;
  height: 100vh;
  position: fixed;
  top: 0;
  box-shadow: var(--lihua-layout-box-shadow);
}

.min-sider-content {
  height: calc(100vh - var(--lihua-layout-height));
}

.head {
  margin-right: var(--lihua-layout-head-space);
}
.logo {
  padding: 0 0 0 var(--lihua-space-sm);
  margin-left: var(--lihua-layout-head-space);
}
.sider-logo {
  height: var(--lihua-layout-height);
}
.view-tabs {
  backdrop-filter: var(--lihua-backdrop-filter-lg);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-lg);
  position: relative;
  z-index: 1
}
</style>

<style lang="scss">
[head-affix = enable] {
  .drawer-navigation-header {
    position: sticky;
    top: 0;
  }
  .view-tabs {
    position: sticky;
    z-index: 2;
    top: var(--lihua-layout-height);
  }
}
</style>

