<template>
  <section>
    <router-view v-slot="{ Component, route}">
      <!-- zoom： 变焦 fade：淡入淡出 breathe：呼吸 top：上升 down：切换 switch：交换 trick：整活  -->
      <transition :name="themeStore.routeTransition" mode="out-in">
        <keep-alive :include = "viewTabsStore.$state.componentAlive" :max="5">
          <component :is="Component" :key="route.path + viewTabsStore.$state.contentComponentKey"/>
        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script setup lang="ts">
import { useThemeStore } from "@/stores/theme";
import { useViewTabsStore} from "@/stores/viewTabs";

const themeStore = useThemeStore()
const viewTabsStore = useViewTabsStore()
</script>
