<template>
  <login-setting-base-component title="修改密码"
                                icon="LockOutlined"
                                skip-msg="可在个人中心设置"
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