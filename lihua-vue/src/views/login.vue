<template>
  <a-flex class="login-background" justify="center" align="center">
    <a-flex class="login-content" justify="space-around" align="center">
      <div style="width: 50%">
        <a-typography-title style="margin-top: 64px;margin-right: 64px;margin-left: 64px">狸花猫</a-typography-title>
        <a-typography-title :level="2" style="margin-right: 64px;margin-left: 64px">基于SpringBoot 3.2 和 vue3.0的 后台管理系统</a-typography-title>
      </div>
      <div style="width: 50%">
        <a-card class="login-card">
          <div style="margin: 32px">
            <div class="login-title">
              <a-typography-title>欢迎登陆狸花猫</a-typography-title>
              <a-typography-text>没有账户？</a-typography-text>
              <a-typography-link>立即注册 <RightOutlined /></a-typography-link>
            </div>
            <a-form :model="loginForm" @finish="login">
              <a-form-item name="username">
                <a-input class="login-form-item" autocomplete="off" v-model:value="loginForm.username" placeholder="用户名" size="large"></a-input>
              </a-form-item>
              <a-form-item name="username">
                <a-input-password class="login-form-item" v-model:value="loginForm.password" placeholder="密码" size="large"></a-input-password>
              </a-form-item>
              <a-form-item name="username">
                <a-flex justify="space-between" :gap="8">
                  <a-input class="login-form-item" v-model:value="loginForm.password" placeholder="验证码" size="large"></a-input>
                  <div style="height: 48px;width: 240px;background: #f3f3f3;border-radius: 6px"></div>
                </a-flex>
              </a-form-item>
              <a-form-item>
                <a-flex justify="space-between">
                  <a-checkbox>记住账号</a-checkbox>
                  <a-typography-link>忘记密码</a-typography-link>
                </a-flex>
              </a-form-item>
              <a-form-item>
                <a-button html-type="submit" class="login-form-item" style="width: 100%">登录</a-button>
              </a-form-item>
            </a-form>
            <a-divider plain>其他方式</a-divider>
            <a-flex justify="space-around">
              <a-button size="large" shape="circle"><WeiboCircleOutlined /></a-button>
              <a-button size="large" shape="circle"><WechatOutlined /></a-button>
              <a-button size="large" shape="circle"><AlipayCircleOutlined /></a-button>
              <a-button size="large" shape="circle"><QqOutlined /></a-button>
              <a-button size="large" shape="circle"><GitlabOutlined /></a-button>
            </a-flex>
          </div>
        </a-card>
      </div>
    </a-flex>
  </a-flex>
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

<style>
.login-background {
  position: relative;
  width: 100%;
  height: 100vh;
  background-image: url("./../../public/login-bg.jpg");
  background-size: cover;
  background-position: center;
}
[data-dark = dark] {
  .login-background:before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.3);
  }
}
.login-content {
  max-width: 1180px;
  width: 85vw;
  height: 70vh;
}
.login-card {
  margin: 64px;
  border-radius: 32px;
}
.login-title {
  margin-top: 32px;
  margin-bottom: 64px;
}
.login-form-item {
  height: 48px
}
</style>
