<template>
  <div>
    <a-layout>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-header class="mn-header" v-show="props.showLayout" :style="themeStore.siderTheme === 'light' || !isSplitMenu ? { background: themeStore.layoutBackgroundColor } : ''">
          <div ref="headerRef">
            <a-flex align="center" justify="space-between">
              <Logo class="logo" :show-title="!isSmallWindow"/>
              <!-- 开启分栏-->
              <Side class="sider"
                    is-mix-top
                    :siderTheme="themeStore.siderTheme"
                    :menu="cloneDeep(permission.menuRouters).map((item: MenuItemGroupType) => { delete item.children; return item})"
                    sider-mode="horizontal"
                    @route-change="(keys: string[]) => loadSideMenu(keys[0], false)"
                    @mounted="(keys: string[]) => loadSideMenu(keys[0], false)"
                    @menu-click="(key) => loadSideMenu(key, true)"
                    v-if="isSplitMenu && !isSmallWindow"
              />
              <!--页头-->
              <Head class="head"/>
            </a-flex>
          </div>
        </a-layout-header>
      </transition>

      <a-layout>
        <transition :name="themeStore.routeTransition" mode="out-in" v-if="!isSplitMenu || isSmallWindow || subMenu.length > 0">
          <a-layout-sider :class="siderClass"
                          v-show="props.showLayout"
                          :style="themeStore.groundGlass && themeStore.siderTheme === 'light' ? { background: themeStore.layoutBackgroundColor } : ''"
                          :theme="isSmallWindow || !isSplitMenu ? themeStore.siderTheme : 'light'"
                          :width="themeStore.siderWith"
                          v-model:collapsed="permissionStore.collapsed"
                          :collapsedWidth="collapsedWidth"
                          @collapse="handleChangeCollapse"
                          :trigger="showMask ? null : ''"
                          breakpoint="xl"
                          collapsible
          >
            <!-- 窗口缩小到阈值后特殊侧边栏logo-->
            <div class="sider-logo" :style="{width: !permissionStore.collapsed && isSmallWindow ? themeStore.siderWith + 'px' : '0px'}">
              <a-flex align="center" justify="center" v-if="!permissionStore.collapsed && isSmallWindow">
                <Logo style="margin: 0; padding-right: var(--lihua-space-sm)"/>
              </a-flex>
            </div>
            <!-- 侧边栏-->
            <Side class="sider-content sider-scrollbar"
                  :sider-theme="isSmallWindow || !isSplitMenu ? undefined: 'light'"
                  :menu="isSmallWindow || !isSplitMenu ? undefined : subMenu"
                  :class="{'small-sider-content': isSmallWindow ,'header-invisible-sider-content': !headerVisible}"
                  @route-change="handleRouteChange"
            />
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
    <!--  小窗口菜单遮罩  -->
    <Mask :show-mask="showMask" :z-index="100" @click="handleCloseSider"/>
  </div>
</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
import {usePermissionStore} from "@/stores/permission";
import Logo from "@/layout/logo/index.vue";
import {useThemeStore} from "@/stores/theme";
import {computed, onMounted, onUnmounted, ref, useTemplateRef, watch} from "vue";
import Mask from "@/components/mask/index.vue";
import {cloneDeep} from 'lodash-es'
import type {MenuItemGroupType} from "ant-design-vue/es/menu/src/hooks/useItems";
import type {ItemType} from "ant-design-vue";
import {useRouter} from "vue-router";

const router = useRouter()
const permission = usePermissionStore()
const themeStore = useThemeStore()
const permissionStore = usePermissionStore()
// header dom
const headerRef = useTemplateRef<HTMLDivElement>("headerRef")
// header是否可见，用于在不固定header时动态调整sider高度
const headerVisible = ref<boolean>(true)
// 是否显示layout
const props = defineProps<{ showLayout: boolean }>()
// 是否为小窗
const isSmallWindow = ref<boolean>(themeStore.isSmallWindow)
// 菜单收起宽度，根据当前视口大小变化
const collapsedWidth = ref<0|80>( isSmallWindow.value ? 0 : 80)
// 菜单样式class，分为正常和小屏下抽屉样式
const siderClass = ref<'mn-sider' | 'small-mn-sider'>(isSmallWindow.value ? 'small-mn-sider' :'mn-sider')
// 是否为分割菜单
const isSplitMenu = computed(() => themeStore.mixSplitMenu)
// 小屏下抽屉样式遮罩
const showMask = ref<boolean>(false)
// 处理视口变化操作
const handleResize = () => {
  if (isSmallWindow.value) {
    permissionStore.collapsed = true
    showMask.value = false
    collapsedWidth.value = 0
    siderClass.value = 'small-mn-sider'
  } else {
    showMask.value = false
    collapsedWidth.value = 80
    siderClass.value = 'mn-sider'
  }
  // 同时满足菜单分割和菜单分组时，窗口变化重新加载菜单
  if (themeStore.mixSplitMenu && themeStore.siderGroup) {
    permissionStore.reloadMenu()
  }

}

// 展开时打开遮罩
const handleChangeCollapse = (collapsed: boolean) => {
  if (isSmallWindow.value && !collapsed) {
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

// 键盘esc关闭菜单
const handleKeyUp = (event: KeyboardEvent) => {
  if (event.key === 'Escape' && showMask.value) {
    handleCloseSider()
  }
}

// 视口观察器，判断header是否消失在视口中
let observer: IntersectionObserver
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

// 初始化分割菜单相关
const initSplitMenu = () => {
  // 分割后的左侧菜单
  const subMenu = ref<Array<ItemType>>([])

  // 处理点击菜单（顶部）
  const loadSideMenu = (key: string, autoClick: boolean) => {
    // 加载侧边菜单
    const targetMenu = permission.menuRouters.filter((item: ItemType) => item && item.key === key)
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


// 监听窗口变化
watch(() => themeStore.isSmallWindow, (value) => {
  isSmallWindow.value = value
  handleResize()
})

// dom渲染完毕后添加窗口监听
onMounted(() => {
  window.addEventListener("keyup", handleKeyUp)
  createObserver()
});

// 组件销毁后删除监听
onUnmounted(() => {
  window.removeEventListener("keyup", handleKeyUp)
  cleanupObserver()
});
</script>

<style scoped>
.mn-header {
  z-index: 5;
  padding: 0;
  height: var(--lihua-layout-height);
  line-height: var(--lihua-layout-height);
  backdrop-filter: var(--lihua-backdrop-filter-lg);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-lg);
  box-shadow: var(--lihua-layout-box-shadow);
}
.mn-sider {
  position: sticky;
  height: calc(100vh - var(--lihua-layout-height));
  z-index: 4;
  top: 0;
  box-shadow: var(--lihua-layout-box-shadow);
}
.small-mn-sider {
  z-index: 101;
  height: 100vh;
  position: fixed;
  top: var(--lihua-layout-height);
  box-shadow: var(--lihua-layout-box-shadow);
}
.sider-content {
  height: calc(100vh - var(--lihua-layout-height) - var(--lihua-layout-height));
}
.header-invisible-sider-content {
  height: calc(100vh - var(--lihua-layout-height));
}
.small-sider-content {
  height: calc(100vh - var(--lihua-layout-height));
  background-color: var(--lihua-background-color-level-2);
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
  line-height: var(--lihua-layout-height);
  background-color: var(--lihua-background-color-level-2);
  position: fixed;
  top: 0;
  transition: all 0.2s, background 0s;
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
[data-head-affix = affix] {
  .mn-header {
    position: sticky;
    top: 0;
  }
  .mn-sider {
    position: sticky;
    top: var(--lihua-layout-height);
  }
  .view-tabs {
    position: sticky;
    z-index: 3;
    top: var(--lihua-layout-height);
  }
}

[sider-dark = dark] {
  .small-sider-content {
    background-color: var(--lihua-sider-dark-color);
  }
  .sider-logo {
    span {
      color: rgba(255, 255, 255, 0.85);
    }
    background-color: var(--lihua-sider-dark-color);
  }
}
</style>

