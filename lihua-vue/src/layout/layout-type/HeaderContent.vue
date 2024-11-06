<template>
  <a-layout>
    <div class="hc-header">
      <transition :name="themeStore.routeTransition" mode="out-in">
        <a-layout-header class="sider-scrollbar"
                         :class="showScrollbar ? 'hc-layout-header-scrollbar' : 'hc-layout-header'"
                         id="hcHeaderContent"
                         v-show="props.showLayout"
                         :style="themeStore.siderTheme === 'light' ?
        { background: themeStore.layoutBackgroundColor } : ''" style="overflow-y: hidden;">
          <a-flex align="center" justify="space-between" :style="showScrollbar ? 'margin-right: 8px; margin-left: 8px' :'margin-right: 32px; margin-left: 32px'">
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
import {useThemeStore} from "@/stores/theme";
import {onMounted, onUnmounted, ref} from "vue";
const themeStore = useThemeStore()
const props = defineProps<{  showLayout: boolean }>()
// 是否出现横向滚动条
const showScrollbar = ref<boolean>(false)
let element = document.getElementById("hcHeaderContent");
// 窗口尺寸变化时返回是否显示滚动条
const handleResize = () => {
  showScrollbar.value = !!(element && element.scrollWidth > element.clientWidth);
}
// dom渲染完毕后添加窗口监听
onMounted(() => {
  element = document.getElementById("hcHeaderContent")
  window.addEventListener("resize", handleResize)
});

// 组件销毁后删除监听
onUnmounted(() => {
  window.removeEventListener("resize", handleResize);
});
</script>

<style scoped>
.hc-header {
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%);
}
.hc-layout-header {
  padding: 0;
  height: 64px;
  line-height: 64px;
  box-shadow: var(--lihua-layout-light-box-shadow);
}

.hc-layout-header-scrollbar {
  padding: 0;
  height: 64px;
  line-height: 54px;
  box-shadow: var(--lihua-layout-light-box-shadow);
}
.logo {
  padding: 0 8px 0 8px;
}
.sider {
  width: 100%;
  margin: 0 32px 0 32px;
}
</style>

<style>
[data-head-affix = affix] {
  .hc-header {
    position: sticky;
    top: 0;
    z-index: 2;
  }
}
[data-theme = dark] {
  .hc-layout-header {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
  .hc-layout-header-scrollbar {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
}
</style>
