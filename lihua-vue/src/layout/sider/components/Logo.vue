<template>
  <div class="logo">
    <a-flex gap="middle" align="center" justify="center" v-if="!permissionStore.collapsed">
      <div>
        <a-avatar :style="{ backgroundColor: '#f56a00'}">
          LiHua
        </a-avatar>
      </div>
      <div class="title">
        <span>LiHua Pro</span>
      </div>
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
  padding: 16px;
  color: azure;
  cursor: pointer;
  .title {
    height: 32px;
    overflow: hidden;
    /**color: #fff;*/
    color: #000;
    font-weight: 600;
    font-size: 18px;
    line-height: 32px;
  }
}
</style>
