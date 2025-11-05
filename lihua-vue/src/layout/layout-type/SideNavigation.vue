<template>
  <div>
    <a-layout style="min-height: 100vh">
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-sider
                        :class="siderClass"
                        v-show="props.showLayout"
                        :style="themeStore.groundGlass && themeStore.siderTheme === 'light' ? { background: themeStore.layoutBackgroundColor } : ''"
                        :theme="themeStore.siderTheme"
                        :trigger="null"
                        :width="themeStore.siderWith"
                        v-model:collapsed="permissionStore.collapsed"
                        collapsible
                        breakpoint="xl"
                        :collapsedWidth="collapsedWidth"
        >
          <Logo class="logo" :class="isSmallWindow ? 'sider-logo' : ''"/>
          <!-- 侧边栏-->
          <div class="sider sider-scrollbar">
            <Side @route-change="handleRouteChange" :class="isSmallWindow ? 'small-sider-content' : ''"/>
          </div>
        </a-layout-sider>
      </transition>
      <a-layout>
        <a-layout-header class="sn-header"
                         :style="{'background': themeStore.layoutBackgroundColor}">
          <transition :name="themeStore.routeTransition" mode="out-in">
            <!--    菜单收缩-->
            <a-flex class="sn-head" justify="space-between" v-show="props.showLayout">
              <a-flex align="center" :gap="16">
                <!--菜单开关-->
                <HeadCollapsed @collapsed-change="handleChangeCollapse"/>
                <!--面包屑 宽度不足时隐藏-->
                <Breadcrumb v-if="!isSmallWindow"/>
              </a-flex>
              <!-- 右侧头部-->
              <Head/>
            </a-flex>
          </transition>
          <view-tabs v-if="themeStore.showViewTabs"/>
        </a-layout-header>
        <a-layout-content class="layout-content">
          <!--内容-->
          <Content/>
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
import Logo from "@/layout/logo/index.vue";
import Mask from "@/components/mask/index.vue";
import {usePermissionStore} from "@/stores/permission";
import {useThemeStore} from "@/stores/theme";
import {nextTick, onMounted, onUnmounted, ref, watch} from "vue";
import HeadCollapsed from "@/layout/head/components/collapsed/index.vue";
import Breadcrumb from "@/layout/head/components/breadcrumb/index.vue";

const themeStore = useThemeStore()
const permissionStore = usePermissionStore()
const props = defineProps<{showLayout: boolean}>()
const isSmallWindow = ref<boolean>(themeStore.isSmallWindow)

// 小屏下抽屉样式遮罩
const showMask = ref<boolean>(false)
// 菜单收起宽度，根据当前视口大小变化
const siderClass = ref<'sn-sider'|'small-sn-sider'>(isSmallWindow.value ? 'small-sn-sider' :'sn-sider')
// 菜单样式class，分为正常和小屏下抽屉样式
const collapsedWidth = ref<0|80>(isSmallWindow.value ? 0 : 80)
// 处理视口变化操作
const handleResize = () => {
  if (isSmallWindow.value) {
    permissionStore.collapsed = true
    showMask.value = false
    collapsedWidth.value = 0
    siderClass.value = 'small-sn-sider'
  } else {
    showMask.value = false
    collapsedWidth.value = 80
    siderClass.value = 'sn-sider'
  }
  nextTick(() => permissionStore.reloadMenu())
}

// 处理展开折叠操作
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

// 监听窗口变化
watch(() => themeStore.isSmallWindow, (value) => {
  isSmallWindow.value = value
  handleResize()
})

// dom渲染完毕后添加窗口监听
onMounted(() => {
  window.addEventListener("keyup", handleKeyUp)
});

// 组件销毁后删除监听
onUnmounted(() => {
  window.removeEventListener("keyup", handleKeyUp)
});
</script>

<style scoped>
.sn-header {
  z-index: 3;
  height: auto;
  padding: 0;
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  line-height: 48px;
}
.sn-head {
  box-shadow: var(--lihua-layout-light-box-shadow);
  padding-right: 32px;
}
.sider {
  height: calc(100vh - 48px);
}
.logo {
  padding: 8px 16px 8px 16px
}
.sn-sider {
  position: sticky;
  height: 100vh;
  top: 0;
  z-index: 4;
  box-shadow: var(--lihua-layout-light-box-shadow);
}
.small-sn-sider {
  z-index: 101;
  position: fixed;
  top: 0;
  box-shadow: var(--lihua-layout-light-box-shadow);
  background-color: #fff !important;
}
.small-sider-content {
  height: calc(100vh - 32px - 32px);
}
.sider-logo {
  background-color: #fff;
}
</style>

<style lang="scss">
[data-head-affix = affix] {
  .sn-header {
    position: sticky;
    top: 0;
  }
}
[data-theme = dark] {
  .sn-sider {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
  .sn-head {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
  .sider-logo {
    background-color: #141414;
  }
  .small-sn-sider {
    background-color: #141414 !important;
  }
}
[sider-dark = dark] {
  .small-sn-sider {
    background-color: rgba(0,21,41) !important;
  }
  .sider-logo {
    span {
      color: rgba(255, 255, 255, 0.85);
    }
    background-color: rgba(0,21,41);
  }
}
</style>

