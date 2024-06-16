<template>
  <a-dropdown v-model:open="open" trigger="click">
    <a-tooltip title="默认部门">
      <span class="header-right-item header-right" style="max-width: 130px">
        <a-typography-text ellipsis :content="userStore.defaultDeptName" style="vertical-align: middle"/>
      </span>
    </a-tooltip>
    <template #overlay>
      <a-card class="dept-card scrollbar" :bordered="false" :body-style="{padding: '8px'}">
        <default-dept @dept-select="() => open = false"/>
      </a-card>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import {useUserStore} from "@/stores/modules/user.ts";
import DefaultDept from "@/components/dept-select/index.vue"
import {ref} from "vue";
const userStore = useUserStore();
const open = ref<boolean>(false)
</script>

<style scoped>
.dept-card {
  max-height: 500px;
  box-shadow: 0 2px 8px rgba(0,21,41,.12);
}
</style>