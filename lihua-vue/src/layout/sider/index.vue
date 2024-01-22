<template>
  <Logo/>
      <a-menu theme="dark" mode="inline" @select="selectMenu">
        <Menu :data="data"/>
      </a-menu>
</template>

<script setup lang="ts">

import Logo from "@/layout/sider/components/Logo.vue";
import Menu from "@/layout/sider/components/Menu.vue";
import {usePermissionStore} from "@/stores/modules/permission";
import { computed } from "vue";
const permission = usePermissionStore()
const data = computed(() => permission.sidebarRouters)


const selectMenu = ({ item, key, selectedKeys }) => {
  console.log(item)
  console.log(key)
  console.log(selectedKeys)
}
</script>
