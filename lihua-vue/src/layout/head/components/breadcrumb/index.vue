<template>
  <transition :name="theme.$state.routeTransition" mode="out-in">
    <a-breadcrumb :routes="pageRoute" v-if="pageRoute && pageRoute.length > 1 && pageRoute[0].path !== ''">
      <template #itemRender="{route}">
        {{ route.meta.label }}
      </template>
    </a-breadcrumb>
  </transition>
</template>
<script lang="ts" setup>
import {type RouteRecordRaw, useRoute} from "vue-router";
import {ref, watch} from "vue";
import {useThemeStore} from "@/stores/theme.ts";
const theme = useThemeStore();
const route = useRoute()
const pageRoute = ref<RouteRecordRaw[]>();
if (route.fullPath !== '/index') {
  pageRoute.value = route.matched
  pageRoute.value.forEach((route) => route.children = [])
}

// 监听路由变化
watch(() => route,(value) => {
  if (value.fullPath !== '/index') {
    pageRoute.value = value.matched
    pageRoute.value.forEach((route) => route.children = [])
  } else {
    pageRoute.value = []
  }
}, {deep: true})
</script>
