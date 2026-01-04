<template>
  <a-layout>
    <transition :name="themeStore.routeTransition" mode="out-in">
      <a-layout-header class="mix-navigation-header" :class="{'background-glass': themeStore.siderTheme === 'light'}" v-show="props.showLayout">
        <div ref="headerRef">
          <a-flex align="center" justify="space-between">
            <!-- 顶部logo-->
            <Logo class="logo"/>
            <!-- 顶部导航栏-->
            <Side class="sider"
                  is-mix-top
                  :menu="cloneDeep(permissionStore.menuRouters).map((item: MenuItemGroupType) => {delete item.children; return item})"
                  sider-mode="horizontal"
                  @route-change="(keys: string[]) => loadSideMenu(keys[0], false)"
                  @mounted="(keys: string[]) => loadSideMenu(keys[0], false)"
                  @menu-click="(key) => loadSideMenu(key, true)"
            />
            <!-- 头部组件-->
            <Head class="head"/>
          </a-flex>
        </div>
      </a-layout-header>
    </transition>

    <a-layout>
      <!--    二级导航侧边栏    -->
      <transition :name="themeStore.routeTransition" mode="out-in" v-if="subMenu.length > 0">
        <a-layout-sider :class="{'background-glass': themeStore.siderTheme === 'light'}"
                        class="mix-navigation-sider"
                        v-show="props.showLayout"
                        theme="light"
                        :width="themeStore.siderWith"
                        v-model:collapsed="permissionStore.collapsed"
                        breakpoint="xl"
                        collapsible
        >
          <Side class="sider-scrollbar" :class="headerVisible ? 'sider-content' : 'header-invisible-sider-content'" sider-theme="light" :menu="subMenu" />
        </a-layout-sider>
      </transition>
      <!-- view-tab 和 content -->
      <a-layout-content>
        <view-tabs class="view-tabs background-glass" v-if="themeStore.showViewTabs" :style="{'top': !props.showLayout ? '0' : '' }"/>
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
import {usePermissionStore} from "@/stores/permission";
import Logo from "@/layout/logo/index.vue";
import {useThemeStore} from "@/stores/theme";
import {onMounted, onUnmounted, ref, useTemplateRef} from "vue";
import {cloneDeep} from 'lodash-es'
import type {MenuItemGroupType} from "ant-design-vue/es/menu/src/hooks/useItems";
import type {ItemType} from "ant-design-vue";
import {useRouter} from "vue-router";

const router = useRouter()
const themeStore = useThemeStore()
const permissionStore = usePermissionStore()
// header dom
const headerRef = useTemplateRef<HTMLDivElement>("headerRef")
// 是否显示layout
const props = defineProps<{ showLayout: boolean }>()

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
        router.push(menu.children[0].key as string)
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

/**
 * 初始化元素监听器
 */
const initObserver = () => {
  // 视口观察器，判断header是否消失在视口中
  let observer: IntersectionObserver
  // header是否可见，用于在不固定header时动态调整sider高度
  const headerVisible = ref<boolean>(true)
  // 创建观察
  const createObserver = () => {
    if (!observer) {
      observer = new IntersectionObserver(
          (entries) => {
            entries.forEach((entry) => {
              headerVisible.value = entry.isIntersecting
            });
          }
      );

      // 观察目标元素
      if (headerRef.value) {
        observer.observe(headerRef.value);
      }
    }
  };
  // 销毁观察
  const cleanupObserver = () => {
    if (observer) {
      // 停止观察
      observer.disconnect();
    }
  };

  return {
    headerVisible,
    createObserver,
    cleanupObserver
  }
}

const {headerVisible, createObserver, cleanupObserver} = initObserver()

onMounted(() => {
  createObserver()
})

onUnmounted(() => {
  cleanupObserver()
})
</script>

<style scoped>
.mix-navigation-header {
  z-index: 5;
  padding: 0;
  height: var(--lihua-layout-height);
  line-height: var(--lihua-layout-height);
  backdrop-filter: var(--lihua-backdrop-filter-lg);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-lg);
  box-shadow: var(--lihua-layout-box-shadow);
}
.mix-navigation-sider {
  position: sticky;
  height: calc(100vh - var(--lihua-layout-height));
  z-index: 4;
  top: 0;
  box-shadow: var(--lihua-layout-box-shadow);
}
.sider-content {
  height: calc(100vh - var(--lihua-layout-height) - var(--lihua-layout-height));
}
.header-invisible-sider-content {
  height: calc(100vh - var(--lihua-layout-height));
}
.head {
  margin-right: var(--lihua-layout-head-space);
}
.logo {
  padding: 0 0 0 var(--lihua-space-sm);
  margin-left: var(--lihua-layout-head-space);
}
.view-tabs {
  backdrop-filter: var(--lihua-backdrop-filter-lg);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-lg);
  position: relative;
  z-index: 3
}
.sider {
  flex: 1 1 0;
  min-width: 0;
  margin-left: var(--lihua-layout-head-space);
}
</style>

<style lang="scss">
[head-affix = enable] {
  .mix-navigation-header {
    position: sticky;
    top: 0;
  }
  .mix-navigation-sider {
    position: sticky;
    top: var(--lihua-layout-height);
  }
  .view-tabs {
    position: sticky;
    z-index: 3;
    top: var(--lihua-layout-height);
  }
}
</style>

