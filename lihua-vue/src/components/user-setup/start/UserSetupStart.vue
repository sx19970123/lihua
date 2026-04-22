<template>
  <user-setup-base-component title="欢迎使用"
                                icon="SettingOutlined"
                                description="进入系统之前需要一些简单的设置，这个过程不会花费很长时间"
                                mainBtnTitle="开 始"
                                :back="false"
                                :show-main-btn-icon="false"
                                :skip="false"
                                :showGoLoginBtn="true"
                                @go-login="emits('goLogin')"
                                @next="emits('next')"
  />
</template>

<script setup lang="ts">
import UserSetupBaseComponent from "@/components/user-setup/UserSetupBaseComponent.vue";
const emits = defineEmits(['next','goLogin'])


</script>

<style scoped>

</style>
