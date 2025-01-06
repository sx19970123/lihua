<template>
  <login-setting-base-component title="默认部门"
                                description="请设置您的默认部门"
                                icon="ApartmentOutlined"
                                skip-msg="可在系统顶部栏设置"
                                :skip="!userStore.$state.defaultDeptCode"
                                @next="handleNext"
                                @skip="handleSkip"
                                @back="emits('back')"
  >
    <template #content>
      <default-dept @dept-select="handleChangeDept" :show-dept-code="false"/>
    </template>
  </login-setting-base-component>
</template>
<script setup lang="ts">

import LoginSettingBaseComponent from "@/components/login-setting/LoginSettingBaseComponent.vue";
import DefaultDept from "@/components/default-dept-select/index.vue"
import type {Ref} from "vue";
import {message} from "ant-design-vue";
import {useUserStore} from "@/stores/user.ts";
const userStore = useUserStore();
// 向外抛出函数
const emits = defineEmits(['back', 'skip', 'next'])

// 处理更新默认部门
const handleChangeDept = (loading:Ref<boolean>) => {
  loading.value = false
}

// 处理下一步
const handleNext = (loading:Ref<boolean>) => {
  if (userStore.$state.defaultDeptCode) {
    emits('next', loading.value)
  } else {
    message.warn('请选择默认部门')
  }
}

// 处理上一步
const handleSkip = (loading:Ref<boolean>) => {
  loading.value = false
  emits('skip', loading.value)
}
</script>

<style scoped>

</style>
