<template>
  <a-flex class="login-background" justify="center" align="center">
    <a-flex class="login-content" justify="space-around" align="center">
      <div class="slogan">
        <a-typography-title style="margin-top: 64px;margin-right: 64px;margin-left: 64px">狸花猫</a-typography-title>
        <a-typography-title :level="2" style="margin-right: 64px;margin-left: 64px">基于SpringBoot 3.2 和 vue3.0 的
          后台管理系统
        </a-typography-title>
      </div>
      <div class="form">
        <transition name="card" mode="out-in">
          <a-card class="login-card" v-if="showCard">
            <transition name="form" mode="out-in">
              <div style="margin: 16px" v-if="showForm">
                <div class="login-title">
                  <a-typography-title :level="2">欢迎登陆狸花猫</a-typography-title>
                  <a-typography-text v-if="false">没有账户？</a-typography-text>
                  <a-typography-link v-if="false">立即注册
                    <RightOutlined/>
                  </a-typography-link>
                </div>
                <a-form :model="loginForm" @finish="handleFinish" :rules="loginRoles">
                  <a-form-item name="username" :validate-status="validateStatus" hasFeedback>
                    <a-input class="login-form-item"
                             autocomplete="off"
                             v-model:value="loginForm.username"
                             placeholder="用户名"
                             @change="changeInput"

                    >
                      <template #prefix>
                        <UserOutlined style="color: rgba(0, 0, 0, 0.25)"/>
                      </template>
                    </a-input>
                  </a-form-item>
                  <a-form-item name="password" :validate-status="validateStatus" :help="statusMsg" hasFeedback>
                    <a-input-password class="login-form-item"
                                      v-model:value="loginForm.password"
                                      placeholder="密码"
                                      @change="changeInput"
                    >
                      <template #prefix>
                        <LockOutlined style="color: rgba(0, 0, 0, 0.25)"/>
                      </template>
                    </a-input-password>
                  </a-form-item>
                  <a-form-item>
                    <a-flex justify="space-between">
                      <a-checkbox v-model:checked="rememberMe">记住账号</a-checkbox>
                      <a-typography-link v-if="false">忘记密码</a-typography-link>
                    </a-flex>
                  </a-form-item>
                  <a-form-item>
                    <a-button html-type="submit"
                              type="primary"
                              class="login-form-item"
                              :loading="loginLoading"
                              style="width: 100%">登录
                    </a-button>
                  </a-form-item>
                </a-form>
                <a-divider plain v-if="false">其他方式</a-divider>
                <a-flex justify="space-around" v-if="false">
                  <a-button size="large" shape="circle"><WeiboCircleOutlined /></a-button>
                  <a-button size="large" shape="circle"><WechatOutlined /></a-button>
                  <a-button size="large" shape="circle"><AlipayCircleOutlined /></a-button>
                  <a-button size="large" shape="circle"><QqOutlined /></a-button>
                  <a-button size="large" shape="circle"><GitlabOutlined /></a-button>
                </a-flex>
              </div>
            </transition>
          </a-card>
        </transition>
      </div>
    </a-flex>
    <!--    验证码-->
    <transition name="verify" mode="out-in">
      <verify
          v-if="enableCaptcha"
          @success="login"
          @error="loadVerify"
          ref="verifyRef"
          :captcha-type="captchaType"
          :imgSize="{ width: '330px', height: '155px' }"
      />
    </transition>
    <head-theme-switch class="head-theme-switch"/>
  </a-flex>
</template>

<script setup lang="ts">
import {useUserStore} from "@/stores/modules/user"
import {useRouter} from 'vue-router'
import {reactive, ref} from "vue"
import {message} from "ant-design-vue";
import Verify from "@/components/verifition/index.vue";
import type {Rule} from "ant-design-vue/es/form";
import {enable} from "@/api/system/captcha/captcha";
import token from "@/utils/token"
import HeadThemeSwitch from "@/components/light-dark-switch/index.vue"
import type {ResponseType} from "@/api/type";

const router = useRouter()
const verifyRef = ref<InstanceType<typeof Verify>>()
const rememberMe = ref<boolean>(token.enableRememberMe())

// 用户登录
interface LoginFormType {
  username: string,
  password: string
}

const loginForm = reactive<LoginFormType>({
  username: '',
  password: ''
})
const validateStatus = ref<string>()
const statusMsg = ref<string>()
const loginLoading = ref<boolean>()

const loginRoles: Record<string, Rule[]> = {
  username: [{required: true, message: '请输入账号', trigger: 'change'}],
  password: [{required: true, message: '请输入密码', trigger: 'change'}]
}

// 启用记住账号后赋值账号密码
const init = () => {
  if (rememberMe.value) {
    const usernamePassword = token.getUsernamePassword()
    loginForm.username = usernamePassword.username
    loginForm.password = usernamePassword.password
  }
}
init()

// 登录请求
const login = async ({captchaVerification}: { captchaVerification: string }) => {
  loginLoading.value = true
  try {
    const userStore = useUserStore();
    const resp = await userStore.login(loginForm.username, loginForm.password, captchaVerification);
    const {code, msg} = resp as ResponseType<string>;
    if (code === 200) {
      if (rememberMe.value) {
        token.rememberMe(loginForm.username, loginForm.password)
      } else {
        token.forgetMe()
      }
      message.success("登录成功")
      await router.push("/index");
      // 路由跳转完成后的后续逻辑可以放在这里
    } else {
      message.error(msg);
      validateStatus.value = 'error'
      statusMsg.value = msg
      loginLoading.value = false
    }
  } catch (error) {
    console.error("登录失败:", error);
    // 处理登录失败的逻辑，例如显示错误提示
    loginLoading.value = false
  }
};
const changeInput = () => {
  validateStatus.value = ''
  statusMsg.value = ''
}
const handleFinish = () => {
  if (enableCaptcha.value) {
    showVerify()
  } else {
    login({captchaVerification: 'offCaptcha'})
  }
}

// 验证码
const enableCaptcha = ref<boolean>(false)
const captchaType = ref<string>('blockPuzzle')
// 随机加载滑块/点击验证码
const loadVerify = () => {
  setTimeout(() => {
    captchaType.value = Math.random() < 0.5 ? 'blockPuzzle' : 'clickWord';
  },700)

}
// 显示验证码
const showVerify = () => {
  verifyRef.value.show()
}
// 是否启用验证码
const captcha = async () => {
  const resp = await enable()
  if (resp.code === 200 && resp.data) {
    enableCaptcha.value = true
  }
}
captcha()


// 动画效果
const transition = () => {
  const showCard = ref<boolean>(false)
  const showForm = ref<boolean>(false)
  setTimeout(() => {
    showCard.value = true
  }, 10)
  setTimeout(() => {
    showForm.value = true
  }, 100)
  return {
    showCard,
    showForm
  }
}
const {showForm, showCard} = transition()
</script>

<style>
.login-background {
  position: relative;
  width: 100%;
  height: 100vh;
  background-image: url("../assets/images/login-bg.jpg");
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
    background-color: rgba(0, 0, 0, 0.5);
  }
}

@media screen and (max-width: 1000px) {
  .slogan {
    transition: all 0.8s ease-in-out;
    display: none;
    width: 0%
  }
}

.form {
  width: 50%;
  min-width: 488px
}

.head-theme-switch {
  position: fixed;
  top: 16px;
  right: 24px;
}

.login-content {
  max-width: 1180px;
  width: 85vw;
  height: 70vh;
}

.login-card {
  margin: 64px;
  border-radius: 24px;
}

.login-title {
  margin-top: 32px;
  margin-bottom: 64px;
}

.login-form-item {
  height: 48px
}


.card-enter-active {
  transition: all 0.8s ease-in-out;
}

.card-enter-from {
  transform: translateY(80px) scale(88%) rotateY(24deg);
  opacity: 0;
}

.form-enter-active {
  transition: all 0.6s ease-in-out;
}

.form-enter-from {
  transform: translateY(24px);
  opacity: 0;
}

.verify-enter-active {
  transition: all 0.3s ease-in-out;
}

.verify-enter-from {
  transform: translateY(10px);
  opacity: 0;
}

.verify-leave-active {
  transition: all 0.25s ease-out;
}

.verify-leave-to {
  opacity: 0;
}
</style>
