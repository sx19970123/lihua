<template>
  <div class="logo">
    <a-flex gap="middle" align="center" justify="center" v-if="themeStore.layoutType === 'header-sider' || themeStore.layoutType === 'header-content' || !permissionStore.collapsed">
      <div>
        <a-avatar :style="{ backgroundColor: '#f56a00'}">
          <span>LiHua</span>
        </a-avatar>
      </div>
      <a-typography-title class="title" ellipsis :level="4">
        <span :class="themeStore.layoutType !== 'header-sider' && themeStore.siderTheme === 'dark' ? 'main-name': ''">LiHua Pro</span>
      </a-typography-title>
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
import { useThemeStore } from "@/stores/modules/theme";

const permissionStore = usePermissionStore()
const themeStore = useThemeStore()
</script>

<style scoped>
.logo {
  cursor: pointer;
  .title {
    margin: 0;
    overflow: hidden;
  }
}
.main-name {
  color: rgba(255, 255, 255, 0.85);
}
</style>
