<template>
  <login-setting-base-component title="完 成"
                                icon="CheckCircleOutlined"
                                mainBtnTitle="进入系统"
                                description="一切就绪！点击下方按钮进入系统。"
                                :back="false"
                                :show-main-btn-icon="false"
                                :skip="false"
                                @next="toIndex"

  />
</template>

<script setup lang="ts">
import LoginSettingBaseComponent from "@/components/login-setting/LoginSettingBaseComponent.vue";
import router from "@/router";
import Token from "@/utils/Token.ts";

const toIndex = async () => {
  Token.setLoginSettingResult()
  await router.push("/index")
}
</script>

<style scoped>

</style>