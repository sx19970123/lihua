<template>
  <div>
    <a-layout>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-header class="drawer-navigation-header background-glass" v-show="props.showLayout">
          <a-flex align="center" justify="space-between">
            <Logo class="logo" :show-title="false"/>
            <!--页头-->
            <div id="lihua-layout-head" class="head" />
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
                          collapsible
          >
            <template #trigger v-if="!permissionStore.collapsed">
              <CloseOutlined />
            </template>
            <!-- 小屏模式-->
            <a-flex align="center" justify="center" class="sider-logo">
              <Logo :max-width="themeStore.siderWith"/>
            </a-flex>
            <!-- 侧边栏-->
            <Side class="sider-scrollbar min-sider-content" sider-mode="inline" @route-change="closeSide"/>
          </a-layout-sider>
        </transition>
        <!--    菜单开合开关-->
        <a-layout-content>
          <view-tabs class="view-tabs background-glass" v-if="themeStore.showViewTabs" :style="{'top': !props.showLayout ? '0' : '' }"/>
          <!--内容-->
          <div id="lihua-layout-content" class="layout-content"/>
        </a-layout-content>
      </a-layout>
    </a-layout>
    <!--  小屏菜单遮罩  -->
    <Mask :show-mask="!permissionStore.collapsed" :z-index="100" @click="closeSide"/>
  </div>
</template>

<script setup lang="ts">
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import { usePermissionStore } from "@/stores/permission";
import Logo from "@/layout/logo/index.vue";
import {useThemeStore} from "@/stores/theme";
import Mask from "@/components/mask/index.vue";
const themeStore = useThemeStore()
const permissionStore = usePermissionStore()

// 是否显示layout
const props = defineProps<{ showLayout: boolean }>()

// 关闭菜单
const closeSide = () => {
  permissionStore.collapsed = true
}

closeSide()
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

.ant-layout-sider-zero-width-trigger::after {
  border-radius: 0  var(--lihua-radius-xs) var(--lihua-radius-xs) 0;
}
</style>

