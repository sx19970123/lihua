<template>
    <a-menu theme="light"
            mode="inline"
            style="border: none;"
            v-model:selected-keys="state.selectedKeys"
            v-model:open-keys="state.openKeys"
    >
      <Menu :data="data"/>
    </a-menu>
</template>

<script setup lang="ts">
import Menu from "@/layout/sider/components/Menu.vue";
import { usePermissionStore } from "@/stores/modules/permission";
import { useRoute } from "vue-router";
import {computed, reactive, ref, watch} from "vue";
const route = useRoute()
// pinia 中获取菜单数据
const permission = usePermissionStore()
const data = computed(() => permission.sidebarRouters)
// 菜单侧边栏回显
const state = reactive({
  selectedKeys: [route.path],
  openKeys: route.matched.map(r => r.path)
})
watch(()=> route.path,(value)=> {
  state.selectedKeys = [value]
})
watch(()=> route.matched,(value)=> {
  state.openKeys = value.map(r => r.path)
})
</script>
