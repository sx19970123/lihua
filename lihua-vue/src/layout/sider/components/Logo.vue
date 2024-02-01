<template>
  <div class="logo">
    <a-flex gap="middle" align="center" justify="center" v-if="!permissionStore.collapsed">
      <div>
        <a-avatar :style="{ backgroundColor: '#f56a00'}" :size="38">
          <span>LiHua</span>
        </a-avatar>
      </div>
        <a-typography-title class="title" content="LiHua Pro" ellipsis :level="4"/>
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
const permissionStore = usePermissionStore()
</script>

<style scoped>
.logo {
  height: 64px;
  padding: 12px;
  color: azure;
  cursor: pointer;
  .title {
    margin: 0;
    overflow: hidden;
  }
}
</style>
