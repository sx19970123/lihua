<template>
  <div class="menu-fold">
    <menu-unfold-outlined
        v-if="permissionStore.collapsed"
        class="menu-fold-icon"
        @click="() => permissionStore.closeCollapsed()"
    />
    <menu-fold-outlined class="menu-fold-icon" v-else @click="() => permissionStore.openCollapsed()" />
  </div>
</template>
<script setup lang="ts">
import {MenuFoldOutlined, MenuUnfoldOutlined} from "@ant-design/icons-vue";
import { usePermissionStore } from "@/stores/modules/permission";
const permissionStore = usePermissionStore()
</script>
<style scoped>
.menu-fold {
  margin-left: 16px;
  margin-top: 3px;
  height: 45px;
}
.menu-fold-icon {
  font-size: 20px
}
</style>
