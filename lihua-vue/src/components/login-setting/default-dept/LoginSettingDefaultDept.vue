<template>
  <login-setting-base-component title="默认部门"
                                description="请设置您的默认部门"
                                icon="ApartmentOutlined"
                                skip-msg="可在系统顶部栏设置"
                                @next="handleNext"
                                @skip="handleSkip"
                                @back="emits('back')"
  />
</template>
<script setup lang="ts">

import LoginSettingBaseComponent from "@/components/login-setting/LoginSettingBaseComponent.vue";
import type {Ref} from "vue";

// 向外抛出函数
const emits = defineEmits(['back', 'skip', 'next'])
const handleNext = (loading:Ref<boolean>) => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    emits('next', loading.value)
  }, 2000)
}

const handleSkip = (loading:Ref<boolean>) => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    emits('skip', loading.value)
  }, 1000)
}
</script>

<style scoped>

</style>