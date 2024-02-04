<template>
  <div class="logo">
    <a-flex gap="middle" align="center" justify="center" v-if="themeStore.layoutType === 'head-sider' || !permissionStore.collapsed">
      <div>
        <a-avatar :style="{ backgroundColor: '#f56a00'}">
          <span>LiHua</span>
        </a-avatar>
      </div>
        <span v-if="!themeStore.dataDark && themeStore.siderTheme === 'dark' && themeStore.layoutType !== 'head-sider'" style="color: aliceblue;font-size: 20px">LiHua Pro</span>
        <a-typography-title v-else class="title" content="LiHua Pro" ellipsis :level="4"/>
    </a-flex>
    <a-flex align="center" justify="center" v-else>
      <div>
        <a-avatar :style="{ backgroundColor: '#f56a00'}">
          LiHua
        </a-avatar>
      </div>
    </a-flex>
  </div>
</template>

<script setup lang="ts">
import { usePermissionStore } from "@/stores/modules/permission";
import { useTheme } from "@/stores/modules/theme";

const permissionStore = usePermissionStore()
const themeStore = useTheme()
</script>

<style scoped>
.logo {
  cursor: pointer;
  .title {
    margin: 0;
    overflow: hidden;
  }
}
</style>
