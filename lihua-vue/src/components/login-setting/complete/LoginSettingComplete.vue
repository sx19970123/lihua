<template>
  <login-setting-base-component title="完 成"
                                icon="CheckCircleOutlined"
                                mainBtnTitle="进入系统"
                                description="一切就绪！点击下方按钮进入系统。"
                                :show-main-btn-icon="false"
                                :back="false"
                                :skip="false"
                                @next="emits('complete')"
  />
</template>

<script setup lang="ts">
import LoginSettingBaseComponent from "@/components/login-setting/LoginSettingBaseComponent.vue";
const emits = defineEmits(['complete'])
</script>