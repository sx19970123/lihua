<template>
  <a-dropdown v-model:open="open" trigger="click" :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
    <a-tooltip title="默认部门" placement="bottom" :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
      <span class="header-right-item header-right" style="max-width: 130px">
        <a-typography-text ellipsis
                           class="text-default-color"
                           :type="userStore.defaultDeptName ? '' : 'secondary'"
                           :content="userStore.defaultDeptName ? userStore.defaultDeptName : '设置默认部门'"
                           style="vertical-align: middle"/>
      </span>
    </a-tooltip>
    <template #overlay>
      <div class="default-dept-card">
        <default-dept @dept-select="handleDeptSelect"/>
      </div>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import {useUserStore} from "@/stores/user.ts";
import DefaultDept from "@/components/default-dept-select/index.vue"
import {ref} from "vue";
import {message} from "ant-design-vue";
import type {ResponseType} from "@/api/global/Type.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
const userStore = useUserStore();
const open = ref<boolean>(false)

// 切换部门成功关闭tooltip并提示
const handleDeptSelect = (resp: ResponseType<SysDept>) => {
  open.value = false
  message.success(resp.msg)
}
</script>

<style scoped>
.default-dept-card {
  max-height: 500px;
  box-shadow: var(--lihua-light-box-shadow);
  border-radius: 8px;
}
</style>

