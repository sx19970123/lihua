<template>
  <a-menu :theme="themeStore.siderTheme"
          :mode="themeStore.siderMode"
          v-model:selected-keys="state.selectedKeys"
          v-model:open-keys="state.openKeys"
          :style="themeStore.layoutType === 'header' ? { width: themeStore.siderWith + 'px'} : ''"
          style="border: none; background-color: rgba(255,255,255,0)"
  >
    <Menu :data="data"/>
  </a-menu>
</template>

<script setup lang="ts">
import Menu from "@/layout/sider/components/Menu.vue";
import { usePermissionStore } from "@/stores/modules/permission";
import { useThemeStore } from "@/stores/modules/theme";
import { useRoute } from "vue-router";
import {computed, reactive, watch} from "vue";
const themeStore = useThemeStore()
const route = useRoute()
// pinia 中获取菜单数据
const permission = usePermissionStore()
const data = computed(() => permission.sidebarRouters)
const state = reactive({
  selectedKeys: route.matched.map(r => r.path),
  openKeys: route.matched.map(r => r.path)
})
// 导航回显
watch(()=> route.matched,(value)=> {
  state.selectedKeys = route.matched.map(r => r.path)
  state.openKeys = value.map(r => r.path)
})
// 收起/展开
watch(() => permission.collapsed,(value) => {
  value? themeStore.foldSiderWidth(): themeStore.unfoldSiderWidth()
})
</script>
