<template>
  <div>
    <a-card class="register">
      <transition name="form" mode="out-in">
        <div style="margin: 16px" v-show="show">
          <div class="register-title">
            <a-typography-title :level="2">欢迎注册狸花猫</a-typography-title>
            <a-typography-text>已有账户？</a-typography-text>
            <a-typography-link @click="goLogin(false)">前往登录
              <RightOutlined/>
            </a-typography-link>
          </div>
          <a-form :model="userRegister"
                  :rules="rules"
                  @finish="handleFinish"
          >
            <a-form-item name="username" hasFeedback>
              <a-input class="register-form-item"
                       autocomplete="off"
                       placeholder="用户名"
                       v-model:value="userRegister.username"
              >
                <template #prefix>
                  <UserOutlined class="input-prefix-icon-color"/>
                </template>
              </a-input>
            </a-form-item>
            <a-form-item name="password" hasFeedback>
              <password-input class="register-form-item"
                              v-model:value="userRegister.password"
                              :size="98"
                              placeholder="密码"
                              height="48px"
                              prefixIcon
                              :showProgress="!!userRegister.password"
              />
            </a-form-item>

            <a-form-item name="confirmPassword" hasFeedback>
              <a-input-password class="register-form-item"
                                placeholder="再次输入密码"
                                v-model:value="userRegister.confirmPassword"
              >
                <template #prefix>
                  <LockOutlined class="input-prefix-icon-color"/>
                </template>
              </a-input-password>
            </a-form-item>
            <a-form-item>
              <a-button html-type="submit"
                        type="primary"
                        class="register-form-item"
                        style="width: 100%">注册
              </a-button>
            </a-form-item>
        </a-form>
      </div>
      </transition>
    </a-card>

    <!--    验证码-->
    <transition name="verify" mode="out-in">
      <verify
          @success="handleRegister"
          @error="loadVerify"
          ref="verifyRef"
          :captcha-type="captchaType"
          :imgSize="{ width: '330px', height: '155px' }"
      />
    </transition>
  </div>
</template>

<script setup lang="ts">
import {ref, useTemplateRef} from "vue";
import PasswordInput from "@/components/password-input/index.vue"
import type {Rule} from "ant-design-vue/es/form";
import {checkUserName, register} from "@/api/system/login/Login.ts";
import {message} from "ant-design-vue";
import Verify from "@/components/verifition/index.vue";
import {rasEncryptPassword} from "@/utils/Crypto.ts";
const show = ref<boolean>(false)
// 向父组件抛出切登录方法
const emits = defineEmits(['goLogin'])
const goLogin = (clearLoginForm: boolean) => {
  emits('goLogin', clearLoginForm)
}

setTimeout(() => {
  show.value = true
}, 100)

// 用户注册实体
const userRegister = ref<{
  username: string,
  password: string,
  confirmPassword: string

}>({
  username: '',
  password: '',
  confirmPassword: ''
})

// 对比两次密码输入是否相同
const equalToPassword = async (rule: any, value: string) => {
  if (userRegister.value.password !== value) {
    return Promise.reject('两次输入的密码不一致')
  } else {
    return Promise.resolve();
  }
}

// 失焦时触发检查用户名是否可用
const handleCheckUsername = async (_rule: Rule, value: string) => {
  if (value) {
    const resp = await checkUserName(value)
    if (resp.code === 200) {
      if (!resp.data) {
        return Promise.reject('该用户名已存在')
      } else {
        return Promise.resolve();
      }
    } else {
      return Promise.reject(resp.msg)
    }
  }
}

// 表单验证
const rules: Record<string, Rule[]> = {
  username: [
    { required: true,message: "请输入用户名",trigger: ['change','blur']},
    { validator: handleCheckUsername, trigger:'blur'}
  ],
  password: [
    { required: true,message: "请输入密码",trigger: ['change','blur']},
  ],
  confirmPassword: [
    { required: true,message: "请再次输入密码",trigger: ['change','blur']},
    { validator: equalToPassword, trigger: ['change','blur'] }
  ]
}

// 表单验证通过后提交注册
const handleFinish = () => {
  verifyRef.value.show()
}

// 验证码
const verifyRef = useTemplateRef<InstanceType<typeof Verify>>("verifyRef")
const captchaType = ref<string>('blockPuzzle')
// 随机加载滑块/点击验证码
const loadVerify = () => {
  setTimeout(() => {
    captchaType.value = Math.random() < 0.5 ? 'blockPuzzle' : 'clickWord';
  },700)

}

// 用户注册
const handleRegister = async ({captchaVerification}: { captchaVerification: string }) => {
  const {username, password, confirmPassword} = userRegister.value
  try {
    // 对密码加密处理
    const passwordEncrypt = await rasEncryptPassword(password)
    // 对确认密码加密处理
    const confirmPasswordEncrypt = await rasEncryptPassword(confirmPassword)
    // 用户注册
    const resp = await register(
        username,
        passwordEncrypt.ciphertext,
        passwordEncrypt.requestKey,
        confirmPasswordEncrypt.ciphertext,
        confirmPasswordEncrypt.requestKey,
        captchaVerification)

    if (resp.code === 200) {
      message.success("注册成功，即将前往登录")
      setTimeout(() => {
        goLogin(true)
      },1000)
    } else {
      message.error(resp.msg)
    }
  } catch (error) {
    message.error(error as string)
  }

}
</script>

<style scoped>
.register {
  width: 380px;
  margin: 64px;
  border-radius: 24px;
}

.register-form-item {
  height: 48px
}

.register-title {
  margin-top: 32px;
  margin-bottom: 64px;
}

.form-enter-active {
  transition: all 0.6s ease-in-out;
}

.form-enter-from {
  transform: translateY(24px);
  opacity: 0;
}

</style>
