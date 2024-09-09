<template>
  <a-flex class="login-background" id="login-container" justify="center" align="center">
    <transition name="down" mode="in-out">
      <a-flex class="login-content" justify="space-around" align="center" v-if="!showSetting">
      <div class="slogan">
        <a-typography-title style="margin-top: 64px;margin-right: 64px;margin-left: 64px">狸花猫后台管理系统</a-typography-title>
        <a-typography-title :level="2" style="margin-right: 64px;margin-left: 64px">基于SpringBoot 3.X 和 vue3.X 的
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
                  <a-typography-text v-if="errorMessage" type="danger">{{errorMessage}}</a-typography-text>
                  <a-typography-text v-if="false">没有账户？</a-typography-text>
                  <a-typography-link v-if="false">立即注册
                    <RightOutlined/>
                  </a-typography-link>
                </div>
                <a-form :model="loginForm" @finish="handleFinish" :rules="loginRoles">
                  <a-form-item name="username" hasFeedback>
                    <a-input class="login-form-item"
                             autocomplete="off"
                             v-model:value="loginForm.username"
                             placeholder="用户名"
                    >
                      <template #prefix>
                        <UserOutlined style="color: rgba(0, 0, 0, 0.25)"/>
                      </template>
                    </a-input>
                  </a-form-item>
                  <a-form-item name="password" hasFeedback>
                    <a-input-password class="login-form-item"
                                      v-model:value="loginForm.password"
                                      placeholder="密码"
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
    </transition>
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
      <transition name="verify" mode="out-in">
        <login-setting :component-names="settingComponentNames" v-if="showSetting"></login-setting>
      </transition>
  </a-flex>
</template>

<script setup lang="ts">
import {useUserStore} from "@/stores/modules/user"
import {useRouter} from 'vue-router'
import {onMounted, reactive, ref} from "vue"
import {message} from "ant-design-vue";
import Verify from "@/components/verifition/index.vue";
import type {Rule} from "ant-design-vue/es/form";
import {enable} from "@/api/system/captcha/Captcha.ts";
import token from "@/utils/Token.ts"
import HeadThemeSwitch from "@/components/light-dark-switch/index.vue"
import LoginSetting from "@/components/login-setting/index.vue"
import type {ResponseType} from "@/api/global/Type.ts";
import {init} from "@/utils/AppInit.ts"
const router = useRouter()
const verifyRef = ref<InstanceType<typeof Verify>>()
const rememberMe = ref<boolean>(token.enableRememberMe())
const showSetting = ref<boolean>(false)
const settingComponentNames = ref<string[]>([])
// 用户登录
interface LoginFormType {
  username: string,
  password: string
}

const loginForm = reactive<LoginFormType>({
  username: '',
  password: ''
})
const loginLoading = ref<boolean>()
const errorMessage = ref<string>()

const loginRoles: Record<string, Rule[]> = {
  username: [{required: true, message: '请输入账号', trigger: 'change'}],
  password: [{required: true, message: '请输入密码', trigger: 'change'}]
}

// 启用记住账号后赋值账号密码
const initLogin = () => {
  if (rememberMe.value) {
    const usernamePassword = token.getUsernamePassword()
    loginForm.username = usernamePassword.username
    loginForm.password = usernamePassword.password
  }
}
initLogin()

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
      await init()
      // 判断是否需要在登录前进行设置
      const settingComponents = userStore.checkLoginAfter()
      if (settingComponents.length > 0) {
        showSetting.value = true
        settingComponentNames.value.push(...settingComponents)
      } else {
        message.success("登录成功")
        await router.push("/index");
      }
      // 路由跳转完成后的后续逻辑可以放在这里
    } else {
      message.error(msg);
      loginLoading.value = false
    }
  } catch (error) {
    console.error("登录失败:", error);
    // 处理登录失败的逻辑，例如显示错误提示
    loginLoading.value = false
  }
};

// 触发登录
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
  try {
    const resp = await enable()
    if (resp.code === 200) {
      enableCaptcha.value = resp.data
    } else {
      console.error(resp.msg)
    }
  } catch (e) {
    errorMessage.value = '无法访问服务器，请检查网络连接'
  }
}
captcha()

// 判断重定向回来的参数，给用户合适的提示
// 提示信息显示完成后销毁，刷新页面时不再提示
const handleRedirect = () => {
  if (history.state.msg) {
    message.error(history.state.msg)
    history.state.msg = undefined
  }
}
handleRedirect()

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

// 指定背景动画起始秒数
const nowDynamicBackground = () => {
  // 获取当前时间
  const now = new Date();
  // 今天的起始时间
  const startOfDay = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
  // 当前时间的毫秒数
  const secondsSinceStartOfDay = (now.getTime() - startOfDay.getTime()) / 1000;
  const htmlElement = document.querySelector(".login-background") as HTMLElement
  // 设置 login-background 背景动画起始秒数
  if (htmlElement) {
    htmlElement.style.animationDelay = secondsSinceStartOfDay + 's'
  }
}

onMounted(() => {
  nowDynamicBackground()
})

</script>

<style>
.login-background {
  position: relative;
  width: 100%;
  height: 100vh;
  /**background-image: url("../assets/images/login-background.jpg");*/
  background-image:linear-gradient(-135deg,#C2FFD8 10%,#465EFB 100%);
  background-size: 200% 200%;
  animation: gradientAnimation 86400s ease infinite;
}
@keyframes gradientAnimation {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

[data-theme = dark] {
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
  max-width: 380px;
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
