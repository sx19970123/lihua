<template>
  <div style="margin-left: 16px">
    <menu-unfold-outlined
        v-if="permissionStore.collapsed"
        :style="{'fontSize':'20px'}"
        @click="() => permissionStore.closeCollapsed()"
    />
    <menu-fold-outlined v-else :style="{'fontSize':'20px'}" class="trigger" @click="() => permissionStore.openCollapsed()" />
  </div>
</template>
<script setup lang="ts">
import {MenuFoldOutlined, MenuUnfoldOutlined} from "@ant-design/icons-vue";
import { usePermissionStore } from "@/stores/modules/permission";
const permissionStore = usePermissionStore()
</script>
<style scoped>

</style>
