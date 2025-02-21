<template>
  <div>
    <a-layout>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-header class="hs-header" v-show="props.showLayout" :style="{'background': themeStore.layoutBackgroundColor}">
          <div ref="headerRef">
            <a-flex align="center" justify="space-between">
              <Logo class="logo" :show-title="siderClass !== 'min-hs-sider'"/>
              <!--页头-->
              <Head class="head"></Head>
            </a-flex>
          </div>
        </a-layout-header>
      </transition>

      <a-layout>
        <transition :name="themeStore.routeTransition" mode="out-in">
          <a-layout-sider :class="siderClass"
                          v-show="props.showLayout"
                          :style="themeStore.groundGlass && themeStore.siderTheme === 'light' ? { background: themeStore.layoutBackgroundColor } : ''"
                          :theme="themeStore.siderTheme"
                          :width="themeStore.siderWith"
                          v-model:collapsed="permissionStore.collapsed"
                          :collapsedWidth="collapsedWidth"
                          @collapse="handleChangeCollapse"
                          :trigger="showMask ? null : ''"
                          breakpoint="xl"
                          collapsible
          >
            <!-- 窗口缩小到阈值后特殊侧边栏logo-->
            <div class="sider-logo" :style="{width: showMask ? themeStore.siderWith + 'px' : '0px'}">
              <a-flex align="center" justify="center" v-if="showMask">
                <Logo style="margin: 0"/>
              </a-flex>
            </div>
            <!-- 侧边栏-->
            <Side class="sider-content sider-scrollbar"
                  :class="{'min-sider-content': siderClass === 'min-hs-sider' ,'header-invisible-sider-content': !headerVisible}"
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
import { usePermissionStore } from "@/stores/permission";
import Logo from "@/layout/logo/index.vue";
import {useThemeStore} from "@/stores/theme";
import {onMounted, onUnmounted, ref, useTemplateRef} from "vue";
import Mask from "@/components/mask/index.vue";
import settings from "@/settings.ts";
const themeStore = useThemeStore()
const permissionStore = usePermissionStore()
const menuToggleWidth = settings.menuToggleWidth
const bodyWidth = ref<number>(document.body.offsetWidth)
// header dom
const headerRef = useTemplateRef<HTMLDivElement>("headerRef")
// header是否可见，用于在不固定header时动态调整sider高度
const headerVisible = ref<boolean>(true)
// 是否显示layout
const props = defineProps<{ showLayout: boolean }>()
// 菜单收起宽度，根据当前视口大小变化
const collapsedWidth = ref<0|80>( bodyWidth.value < menuToggleWidth ? 0 : 80)
// 菜单样式class，分为正常和小屏下抽屉样式
const siderClass = ref<'hs-sider' | 'min-hs-sider'>(bodyWidth.value < menuToggleWidth ? 'min-hs-sider' :'hs-sider')
// 小屏下抽屉样式遮罩
const showMask = ref<boolean>(false)
// 处理视口变化操作
const handleResize = () => {
  bodyWidth.value = document.body.offsetWidth
  if (bodyWidth.value < menuToggleWidth) {
    collapsedWidth.value = 0
    siderClass.value = 'min-hs-sider'
  } else {
    collapsedWidth.value = 80
    siderClass.value = 'hs-sider'
    showMask.value = false
  }
}

// 展开时打开遮罩
const handleChangeCollapse = (collapsed: boolean) => {
  if (bodyWidth.value < menuToggleWidth && !collapsed) {
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

// dom渲染完毕后添加窗口监听
onMounted(() => {
  window.addEventListener("resize", handleResize)
  window.addEventListener("keyup", handleKeyUp)
  createObserver()
});

// 组件销毁后删除监听
onUnmounted(() => {
  window.removeEventListener("resize", handleResize)
  window.removeEventListener("keyup", handleKeyUp)
  cleanupObserver()
});
</script>

<style scoped>
.hs-header {
  z-index: 5;
  padding: 0;
  height: 48px;
  line-height: 48px;
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  box-shadow: var(--lihua-layout-light-box-shadow);
}
.hs-sider {
  position: sticky;
  height: calc(100vh - 48px);
  z-index: 4;
  top: 0;
  box-shadow: var(--lihua-layout-light-box-shadow);
}
.min-hs-sider {
  z-index: 101;
  height: 100vh;
  position: fixed;
  top: 48px;
  box-shadow: var(--lihua-layout-light-box-shadow);
}
.sider-content {
  height: calc(100vh - 48px - 48px);
}
.header-invisible-sider-content {
  height: calc(100vh - 48px);
}
.min-sider-content {
  height: calc(100vh - 48px);
  background-color: #fff;
}
.head {
  margin-right: 32px;
}
.logo {
  padding: 0 8px 0 8px;
  margin-left: 32px;
}
.sider-logo {
  height: 48px;
  line-height:48px;
  background-color: #fff;
  position: fixed;
  top: 0;
  transition: all 0.2s, background 0s;
}
.view-tabs {
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  position: relative;
  z-index: 3
}
</style>

<style lang="scss">
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
    z-index: 3;
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
  .min-hs-sider {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
  .sider-logo {
    background-color: #141414;
  }
  .min-sider-content {
    background-color: #141414;
  }
}
[sider-dark = dark] {
  .min-sider-content {
    background-color: rgba(0,21,41);
  }
  .sider-logo {
    span {
      color: rgba(255, 255, 255, 0.85);
    }
    background-color: rgba(0,21,41);
  }
}
</style>

