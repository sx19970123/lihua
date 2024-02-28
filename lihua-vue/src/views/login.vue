<template>
  <a-flex class="login-background" justify="center" align="center">
    <a-flex class="login-content" justify="space-around" align="center">
      <div style="width: 50%">
        <a-typography-title style="margin-top: 64px;margin-right: 64px;margin-left: 64px">狸花猫</a-typography-title>
        <a-typography-title :level="2" style="margin-right: 64px;margin-left: 64px">基于SpringBoot 3.2 和 vue3.0的 后台管理系统</a-typography-title>
      </div>
      <div style="width: 50%">
        <transition name="card" mode="out-in">
          <a-card class="login-card" v-if="showCard">
            <transition name="form" mode="out-in">
              <div style="margin: 32px" v-if="showForm">
                  <div class="login-title" >
                    <a-typography-title>欢迎登陆狸花猫</a-typography-title>
                    <a-typography-text>没有账户？</a-typography-text>
                    <a-typography-link>立即注册 <RightOutlined /></a-typography-link>
                  </div>
                  <a-form :model="loginForm" @finish="showVerify" :rules="loginRoles">
                    <a-form-item name="username">
                      <a-input class="login-form-item" autocomplete="off" v-model:value="loginForm.username" placeholder="用户名">
                        <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
                      </a-input>
                    </a-form-item>
                    <a-form-item name="username">
                      <a-input-password class="login-form-item" v-model:value="loginForm.password" placeholder="密码">
                        <template #prefix><LockOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
                      </a-input-password>
                    </a-form-item>
                    <a-form-item>
                      <a-flex justify="space-between">
                        <a-checkbox>记住账号</a-checkbox>
                        <a-typography-link>忘记密码</a-typography-link>
                      </a-flex>
                    </a-form-item>
                    <a-form-item>
                      <a-button html-type="submit" type="primary" class="login-form-item" style="width: 100%">登录</a-button>
                    </a-form-item>
                  </a-form>
<!--                    <a-divider plain>其他方式</a-divider>-->
<!--                    <a-flex justify="space-around">-->
<!--                      <a-button size="large" shape="circle"><WeiboCircleOutlined /></a-button>-->
<!--                      <a-button size="large" shape="circle"><WechatOutlined /></a-button>-->
<!--                      <a-button size="large" shape="circle"><AlipayCircleOutlined /></a-button>-->
<!--                      <a-button size="large" shape="circle"><QqOutlined /></a-button>-->
<!--                      <a-button size="large" shape="circle"><GitlabOutlined /></a-button>-->
<!--                    </a-flex>-->
              </div>
            </transition>
          </a-card>
        </transition>
      </div>
    </a-flex>
    <!--    验证码-->
    <transition name="verify" mode="out-in">
      <verify
          @success="login"
          @error="loadVerify"
          ref="verifyRef"
          :captcha-type="captchaType"
          :imgSize="{ width: '330px', height: '155px' }"
      />
    </transition>
  </a-flex>
</template>

<script setup lang="ts">
import { useUserStore } from "@/stores/modules/user"
import { useRouter } from 'vue-router'
import {reactive, ref} from "vue"
import { message } from "ant-design-vue";
import Verify from "@/components/verifition/index.vue";
import type {Rule} from "ant-design-vue/es/form";

const router = useRouter()
const verifyRef = ref<InstanceType<typeof Verify>>()

// 用户登录
const userLogin = () => {
  interface LoginFormType {
    username: string ,
    password: string
  }
  const loginForm = reactive<LoginFormType>({
    username: '',
    password: ''
  })
  const loginRoles:Record<string, Rule[]> = {
    username: [ { required: true, message: '请输入账号', trigger: 'change' } ],
    password: [ { required: true, message: '请输入密码', trigger: 'change' } ]
  }
  // 登录请求
  const login = ({captchaVerification}: {captchaVerification: string}) => {

    const userStore = useUserStore();
    userStore.login(loginForm.username ,loginForm.password, captchaVerification).then((resp) => {
      if (resp.code === 200) {
        router.push("/index")
      } else {
        message.error(resp.msg)
      }
    })
  }
  return {
    loginForm,
    loginRoles,
    login
  }
}

// 验证码
const captcha = () => {
  const captchaType = ref<string>('blockPuzzle');
  // 显示验证码
  const showVerify = () => {
    loadVerify()
    verifyRef.value.show()
  }
  // 刷新验证模式
  const loadVerify = () => {
    if (Math.floor(Math.random() * 10) % 2 === 0 ) {
      captchaType.value = 'blockPuzzle'
    } else {
      captchaType.value = 'clickWord'
    }
  }

  return {
    showVerify,
    loadVerify,
    captchaType
  }
}

// 动画效果
const transition = () => {
  const showCard = ref<boolean>(false)
  const showForm = ref<boolean>(false)
  setTimeout(() => {
    showCard.value = true
  },10)
  setTimeout(() => {
    showForm.value = true
  },100)
  return {
    showCard,
    showForm
  }
}

const {showCard, showForm} = transition()
const {loginForm, loginRoles, login} = userLogin()
const {showVerify,loadVerify,captchaType} = captcha()
</script>

<style>
.login-background {
  position: relative;
  width: 100%;
  height: 100vh;
  background-image: url("../assets/login-bg.jpg");
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
