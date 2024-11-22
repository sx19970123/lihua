<template>
  <login-setting-base-component title="默认部门"
                                description="请设置您的默认部门"
                                icon="ApartmentOutlined"
                                skip-msg="可在系统顶部栏设置"
                                @next="() => message.warn('请选择默认部门')"
                                @skip="handleSkip"
                                @back="emits('back')"
  >
    <template #content>
      <default-dept @dept-select="handleNext"/>
    </template>
  </login-setting-base-component>
</template>
<script setup lang="ts">

import LoginSettingBaseComponent from "@/components/login-setting/LoginSettingBaseComponent.vue";
import DefaultDept from "@/components/default-dept-select/index.vue"
import type {Ref} from "vue";
import {message} from "ant-design-vue";

// 向外抛出函数
const emits = defineEmits(['back', 'skip', 'next'])
const handleNext = (loading:Ref<boolean>) => {
  setTimeout(() => {
    loading.value = false
    emits('next', loading.value)
  },500)
}

const handleSkip = (loading:Ref<boolean>) => {
  loading.value = false
  emits('skip', loading.value)
}
</script>

<style scoped>

</style>
