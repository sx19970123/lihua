<template>
  <div class="login">
    <a-card>
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
  </div>
</template>

<script setup lang="ts">

import { useUserStore } from "@/stores/modules/user"
import { useRouter } from 'vue-router'
import { reactive } from "vue"
import { message } from "ant-design-vue";

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
    userStore.login(loginForm.username ,loginForm.password).then((resp) => {
      if (resp.code === 200) {
        router.push("/index")
      } else {
        message.error(resp.msg)
      }
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
.login {
  position: fixed;
  top: 30%;
  right: 20%;
}
</style>
