<template>
  <a-layout class="layout">
    <!--   左侧导航   -->
    <transition :name="themeStore.routeTransition" mode="out-in" v-if="showSider">
      <a-layout-sider :class="themeStore.siderTheme === 'light' ? 'background-glass' : ''"
                      class="side-navigation-sider"
                      v-show="props.showLayout"
                      :theme="themeStore.siderTheme"
                      :width="themeStore.siderWith"
                      v-model:collapsed="permissionStore.collapsed"
                      collapsible
                      breakpoint="xl"
      >
        <Logo class="logo" :show-title="!permissionStore.collapsed"/>
        <!-- 侧边栏-->
        <div class="sider sider-scrollbar">
          <Side sider-mode="inline" :menu="subMenu" ref="sideRef"/>
        </div>
      </a-layout-sider>
    </transition>
    <!--   右侧head和content   -->
    <a-layout>
      <a-layout-header class="side-navigation-header background-glass">
        <transition :name="themeStore.routeTransition" mode="out-in">
          <a-flex class="side-navigation-header-inner"
                  :style="{'padding-left': !showSider ? 'var(--lihua-layout-head-space)' : 0}"
                  align="center"
                  v-show="props.showLayout">
            <Logo class="top-logo" :auto-color="false" v-if="!showSider"/>
            <!--顶部导航-->
            <Side is-mix-top
                  class="top-sider"
                  :style="{'margin-left': !showSider ? 'var(--lihua-layout-head-space)' : 0}"
                  :menu="cloneDeep(permissionStore.menuRouters).map((item: MenuItemGroupType) => {delete item.children; return item})"
                  sider-theme="light"
                  sider-mode="horizontal"
                  @route-change="(keys: string[]) => loadSideMenu(keys[0], false)"
                  @mounted="(keys: string[]) => loadSideMenu(keys[0], false)"
                  @menu-click="(key) => loadSideMenu(key, true)"
            />
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
import {cloneDeep} from 'lodash-es'
import type {ItemType} from "ant-design-vue";
import type {MenuItemGroupType} from "ant-design-vue/es/menu/src/hooks/useItems";
import {computed, nextTick, ref, useTemplateRef} from "vue";
import PageFooter from "@/layout/footer/index.vue";

const themeStore = useThemeStore()
const permissionStore = usePermissionStore()
const props = defineProps<{showLayout: boolean}>()

const sideRef = useTemplateRef<InstanceType<typeof Side>>("sideRef")

/**
 * 初始化分割菜单相关
 */
const initSplitMenu = () => {
  // 分割后的左侧菜单
  const subMenu = ref<Array<ItemType>>([])

  // 处理点击菜单（顶部）
  const loadSideMenu = (key: string, autoClick: boolean) => {
    // 加载侧边菜单
    const targetMenu = permissionStore.menuRouters.filter((item: ItemType) => item && item.key === key)
    if (targetMenu && targetMenu.length > 0) {
      const menu = targetMenu[0] as MenuItemGroupType;
      subMenu.value = menu.children || []
      // 存在子菜单并设置了自动选中，则默认跳转到第一个
      if (menu.children && autoClick) {
        const key = menu.children[0].key as string
        nextTick(() => {
          if (sideRef.value) {
            sideRef.value.handleClickMenuItem({key});
          }
        })
      }
    } else {
      subMenu.value = []
    }
  }

  return {
    subMenu,
    loadSideMenu
  }
}

const {subMenu, loadSideMenu} = initSplitMenu()

// 显示侧边栏
const showSider = computed(() => {
  return subMenu.value.length > 0
})
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

.side-navigation-header-inner {
  box-shadow: var(--lihua-layout-box-shadow);
  padding-right: var(--lihua-layout-head-space);
}
.sider {
  height: calc(100vh - var(--lihua-layout-height));
}
.top-sider {
  min-width: 0;
  flex: 1
}
.logo {
  padding: var(--lihua-space-sm) var(--lihua-space-base)
}
.top-logo {
  padding-left: var(--lihua-space-sm);
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