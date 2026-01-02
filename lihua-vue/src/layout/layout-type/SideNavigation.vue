<template>
  <div>
    <a-layout style="min-height: 100vh">
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-sider :class="[siderClass, glassClass]"
                        v-show="props.showLayout"
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
            <Side @route-change="handleRouteChange"/>
          </div>
        </a-layout-sider>
      </transition>
      <a-layout>
        <a-layout-header class="sn-header background-glass">
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
        <a-layout-content>
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
import Logo from "@/layout/logo/index.vue";
import Mask from "@/components/mask/index.vue";
import {usePermissionStore} from "@/stores/permission";
import {useThemeStore} from "@/stores/theme";
import {computed, nextTick, onMounted, onUnmounted, ref, watch} from "vue";
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

const glassClass = computed(() => {
  return themeStore.siderTheme === 'light' ? 'background-glass' : ''
})

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
  backdrop-filter: var(--lihua-backdrop-filter-lg);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-lg);
  line-height: var(--lihua-layout-height);
}
.sn-head {
  box-shadow: var(--lihua-layout-box-shadow);
  padding-right: var(--lihua-layout-head-space);
}
.sider {
  height: calc(100vh - var(--lihua-layout-height));
}
.logo {
  padding: var(--lihua-space-sm) var(--lihua-space-base)
}
.sn-sider {
  position: sticky;
  height: 100vh;
  top: 0;
  z-index: 4;
  box-shadow: var(--lihua-layout-box-shadow);
}
.small-sn-sider {
  z-index: 101;
  position: fixed;
  top: 0;
  box-shadow: var(--lihua-layout-box-shadow);
  background-color: var(--lihua-background-color-level-2) !important;
}

.sider-logo {
  background-color: var(--lihua-background-color-level-2);
}
</style>

<style lang="scss">
[head-affix = enable] {
  .sn-header {
    position: sticky;
    top: 0;
  }
}

[sider-dark = dark] {
  .small-sn-sider {
    background-color: var(--lihua-sider-dark-color) !important;
  }
  .sider-logo {
    span {
      color: rgba(255, 255, 255, 0.85);
    }
    background-color: var(--lihua-sider-dark-color)
  }
}
</style>

