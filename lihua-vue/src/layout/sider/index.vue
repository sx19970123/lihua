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
import router from "@/router";
import { computed } from "vue";
import {useRouter} from "vue-router";
const urouter = useRouter()

const permission = usePermissionStore()
const data = computed(() => permission.sidebarRouters)


const selectMenu = ({ item, key }) => {
  console.log(router)

  urouter.push(key)
}
</script>
