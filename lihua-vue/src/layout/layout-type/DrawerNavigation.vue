<template>
  <div>
    <a-layout class="layout">
      <!--   左侧导航   -->
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-sider class="drawer-navigation-sider"
                        v-show="props.showLayout"
                        :theme="themeStore.siderTheme"
                        :trigger="permissionStore.collapsed ? null : '×'"
                        :width="siderWidth"
                        v-model:collapsed="permissionStore.collapsed"
                        :collapsedWidth="0"
                        collapsible
        >
          <Logo class="logo" :show-title="!permissionStore.collapsed"/>
          <!-- 侧边栏-->
          <div class="sider sider-scrollbar">
            <Side sider-mode="inline" class="small-sider-content" @route-change="closeSide"/>
          </div>
        </a-layout-sider>
      </transition>
      <!--   右侧head和content   -->
      <a-layout>
        <a-layout-header class="drawer-navigation-header background-glass">
          <transition :name="themeStore.routeTransition" mode="out-in">
            <!--    菜单收缩-->
            <a-flex class="drawer-navigation-head" justify="space-between" v-show="props.showLayout">
              <a-flex align="center" :gap="16">
                <!--菜单开关-->
                <HeadCollapsed/>
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
    <!--  小屏菜单遮罩  -->
    <Mask :show-mask="!permissionStore.collapsed" :z-index="100" @click="closeSide"/>
  </div>
</template>

<script setup lang="ts">
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue";
import Logo from "@/layout/logo/index.vue";
import {usePermissionStore} from "@/stores/permission";
import {useThemeStore} from "@/stores/theme";
import HeadCollapsed from "@/layout/head/components/collapsed/index.vue";
import Mask from "@/components/mask/index.vue";
import {computed} from "vue";
import PageFooter from "@/layout/footer/index.vue";

const themeStore = useThemeStore()
const permissionStore = usePermissionStore()
const props = defineProps<{showLayout: boolean}>()

// 菜单栏宽度大于当前屏幕宽度时，减少60像素，保证可正常关闭
const siderWidth = computed(() => {
  const innerWidth = window.innerWidth
  return themeStore.siderWith > innerWidth - 60 ? innerWidth - 60 : themeStore.siderWith
})

// 关闭菜单
const closeSide = () => {
  permissionStore.collapsed = true
}

closeSide()
</script>

<style scoped>
.drawer-navigation-header {
  z-index: 3;
  height: auto;
  padding: 0;
  backdrop-filter: var(--lihua-backdrop-filter-lg);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-lg);
  line-height: var(--lihua-layout-height);
}
.drawer-navigation-head {
  box-shadow: var(--lihua-layout-box-shadow);
  padding-right: var(--lihua-layout-head-space);
}
.sider {
  height: calc(100vh - var(--lihua-layout-height));
}
.logo {
  padding: var(--lihua-space-sm) var(--lihua-space-base)
}
.drawer-navigation-sider {
  position: fixed;
  height: 100vh;
  top: 0;
  z-index: 101;
  box-shadow: var(--lihua-layout-box-shadow);
}
</style>

<style lang="scss">
[head-affix = enable] {
  .drawer-navigation-header {
    position: sticky;
    top: 0;
  }
}
.ant-layout-sider-zero-width-trigger::after {
  border-radius: 0  var(--lihua-radius-xs) var(--lihua-radius-xs) 0;
}
</style>

