<template>
  <a-row>
    <a-col :span="8">
      <a-card>
        <SwapLeftOutlined />
        <VerticalAlignBottomOutlined />
        <CloseOutlined />
        <PauseCircleTwoTone />
        <a-form :model="loginForm" @finish="login">
          <a-form-item label="username" name="username">
            <a-input v-model:value="loginForm.username"></a-input>
          </a-form-item>
          <a-form-item label="password" name="username">
            <a-input-password v-model:value="loginForm.password"></a-input-password>
          </a-form-item>
          <a-form-item>
            <a-button html-type="submit">登录</a-button>
          </a-form-item>
        </a-form>
      </a-card>
    </a-col>
  </a-row>

</template>

<script setup lang="ts">

import { useUserStore } from "@/stores/modules/user"
import { useRouter } from 'vue-router'
import { reactive } from "vue"
const router = useRouter()

const userLogin = () => {
  interface LoginFormType {
    username: string ,
    password: string
  }

  const loginForm = reactive<LoginFormType>({
    username: '',
    password: ''
  })

  const login = () => {
    const userStore = useUserStore();
    userStore.login(loginForm.username ,loginForm.password).then(() => {
      router.push("/")
    })
  }

  return {
    loginForm,
    login
  }
}


const {loginForm, login} = userLogin()

</script>

<style scoped>

</style>
