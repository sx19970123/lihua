<template>
<!--  小屏状态切换至header-sider布局-->
  <header-sider v-if="isMiniWindow" :show-layout="props.showLayout"/>
<!--  大屏状态使用header-content布局-->
  <a-layout v-else>
    <div class="hc-header">
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-header class="hc-layout-header"
                         v-show="props.showLayout"
                         :style="themeStore.siderTheme === 'light' ?
                          { background: themeStore.layoutBackgroundColor } : ''">
          <a-flex align="center" justify="space-between" style="margin-right: 32px; margin-left: 32px">
            <!--logo-->
            <Logo class="logo"/>
            <!--导航-->
            <Side class="sider" v-rollDisable="true"/>
            <!--页头-->
            <Head></Head>
          </a-flex>
        </a-layout-header>
      </transition>
      <!--多标签-->
      <view-tabs v-if="themeStore.showViewTabs" :style="{background: themeStore.layoutBackgroundColor}" style="padding-top: 6px"/>
    </div>
    <a-layout-content>
      <!--内容-->
      <Content class="layout-content"/>
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
import Logo from "@/layout/logo/index.vue";
import HeaderSider from "@/layout/layout-type/HeaderSider.vue";
import {useThemeStore} from "@/stores/theme";
import {computed, onMounted, onUnmounted, ref} from "vue";
import settings from "@/settings.ts";
import {showOverflowY} from "@/utils/Scrollbar.ts";
const themeStore = useThemeStore()
const props = defineProps<{showLayout: boolean }>()
const bodyWidth = ref<number>(document.body.offsetWidth)
const menuToggleWidth = settings.menuToggleWidth
const isMiniWindow = computed(() => bodyWidth.value < menuToggleWidth)

// 处理视口尺寸变化
const handleResize = () => {
  bodyWidth.value = document.body.offsetWidth;

  // 处于小屏状态时，设置layout-type为header-sider，导航类型为inline。脱离小屏状态时还原
  if (isMiniWindow.value) {
    document.documentElement.setAttribute("layout-type", "header-sider")
    themeStore.changeSiderMode('inline')
  } else {
    document.documentElement.setAttribute("layout-type", "header-content")
    themeStore.changeSiderMode('horizontal')
  }
  showOverflowY()
}

onMounted(() => {
  window.addEventListener("resize", handleResize)
  handleResize()
})

onUnmounted(() => {
  window.removeEventListener("resize", handleResize)
})

</script>

<style scoped>
.hc-header {
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  position: relative;
  z-index: 10;
}
.hc-layout-header {
  padding: 0;
  height: 48px;
  line-height: 48px;
  box-shadow: var(--lihua-layout-light-box-shadow);
}

.logo {
  padding-left: 8px;
}

.sider {
  flex: 1 1 0;
  min-width: 0;
  margin-left: 32px;
}
</style>

<style>
[data-head-affix = affix] {
  .hc-header {
    position: sticky;
    top: 0;
  }
}
[data-theme = dark] {
  .hc-layout-header {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
}
</style>
