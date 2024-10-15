<template>
  <a-layout style="min-height: 100vh">
    <transition :name="themeStore.routeTransition" mode="out-in">
      <a-layout-sider class="sh-sider sider-scrollbar"
                      v-show="props.showLayout"
                      :style="themeStore.groundGlass && themeStore.siderTheme === 'light' ? { background: themeStore.layoutBackgroundColor } : ''"
                      :theme="themeStore.siderTheme"
                      :trigger="null"
                      :width="themeStore.siderWith"
                      v-model:collapsed="permission.collapsed"
                      collapsible
                      breakpoint="xl"
                      :collapsedWidth="collapsedWidth"
      >
        <Logo class="logo"/>
        <!-- 侧边栏-->
        <Side/>
      </a-layout-sider>
    </transition>
    <a-layout>
      <a-layout-header class="sh-header"
                       :style="{'background': themeStore.layoutBackgroundColor}">
        <transition :name="themeStore.routeTransition" mode="out-in">
        <Head class="sh-head" v-show="props.showLayout"/>
        </transition>
        <view-tabs  v-if="themeStore.showViewTabs"/>
      </a-layout-header>
      <a-layout-content class="layout-content">
        <!--内容-->
        <Content/>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import Head from "@/layout/head/index.vue"
import ViewTabs from "@/layout/view-tabs/index.vue";
import Side from "@/layout/sider/index.vue"
import Content from "@/layout/content/index.vue"
import Logo from "@/layout/logo/index.vue";
import { usePermissionStore } from "@/stores/modules/permission";
import {useThemeStore} from "@/stores/modules/theme";
import {onMounted, onUnmounted, ref} from "vue";
const themeStore = useThemeStore()
const permission = usePermissionStore()
const props = defineProps<{  showLayout: boolean }>()

const collapsedWidth = ref<0|80>(document.body.offsetWidth < 768 ? 0 : 80)
const handleResize = () => {
  if (document.body.offsetWidth < 768) {
    collapsedWidth.value = 0
  } else {
    collapsedWidth.value = 80
  }
}

// dom渲染完毕后添加窗口监听
onMounted(() => {
  window.addEventListener("resize", handleResize)
});

// 组件销毁后删除监听
onUnmounted(() => {
  window.removeEventListener("resize", handleResize);
});
</script>

<style scoped>
.sh-header {
  z-index: 1;
  height: auto;
  padding: 0;
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  line-height: 48px;
}
.sh-head {
  box-shadow: var(--lihua-layout-light-box-shadow);
  padding-right: 32px;
}
.sh-sider {
  position: sticky;
  height: 100vh;
  top: 0;
  z-index: 2;
  box-shadow: var(--lihua-layout-light-box-shadow);
}
.logo {
  margin: 16px;
}
</style>

<style>
[data-head-affix = affix] {
  .sh-header {
    position: sticky;
    top: 0;
    z-index: 1;
  }
}
[data-theme = dark] {
  .sh-sider {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
  .sh-head {
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
}
</style>

