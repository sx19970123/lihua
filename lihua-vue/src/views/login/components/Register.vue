<template>
  <div>
    <div class="register-title">
      <a-typography-title :level="2">欢迎注册狸花猫</a-typography-title>
      <a-typography-text>已有账户？</a-typography-text>
      <a-typography-link @click="handleChangeComponent('login')">前往登录
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
                        :showProgress="!!userRegister.password && userRegister.password.length >= 6"
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
                  :loading="registerLoading"
                  style="width: 100%">注册
        </a-button>
      </a-form-item>
    </a-form>
    <!--    验证码-->
    <tianai-captcha ref="tianaiCaptchaRef" @success="handleRegister"/>
  </div>
</template>

<script setup lang="ts">
import {inject, type Ref, ref, useTemplateRef} from "vue";
import PasswordInput from "@/components/password-input/index.vue"
import type {Rule} from "ant-design-vue/es/form";
import {checkUserName, register} from "@/api/system/login/Login.ts";
import {message} from "ant-design-vue";
import {rasEncryptPassword} from "@/utils/Crypto.ts";
import {ResponseError} from "@/api/global/Type.ts";
import TianaiCaptcha from "@/components/tianai-captcha/index.vue";
const registerLoading = ref<boolean>()
const {enableCaptcha} = defineProps<{
  enableCaptcha: boolean
}>()
// 向父组件抛出切登录方法
const emits = defineEmits(['changeComponent'])

// 处理切换组件
const handleChangeComponent = (name: string) => {
  emits('changeComponent', name)
}
// 通过inject接收provide数据，注册成功后可对registerUsername进行修改，告诉login组件用户注册完成并传递用户名
const registerUsername = inject<Ref<string>>("registerUsername")

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
    try {
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
    } catch (e) {
      if (e instanceof ResponseError) {
        return Promise.reject(e.msg)
      } else {
        console.error(e)
        return Promise.reject("未知异常")
      }
    }
  }
}

// 表单验证
const rules: Record<string, Rule[]> = {
  username: [
    { required: true,message: "请输入用户名",trigger: ['change','blur']},
    {pattern: /^[a-zA-Z0-9@.]+$/, message: "用户名只允许大小写英文、数字、@、.", trigger: ['change','blur']},
    { validator: handleCheckUsername, trigger:'blur'}
  ],
  password: [
    { required: true,message: "请输入密码",trigger: ['change','blur']},
    { min: 6, max: 30, message: '密码长度6-30位', trigger: ['change','blur']}
  ],
  confirmPassword: [
    { required: true,message: "请再次输入密码",trigger: ['change','blur']},
    { validator: equalToPassword, trigger: ['change','blur'] }
  ]
}

// 触发注册
const handleFinish = () => {
  if (enableCaptcha) {
    showVerify()
  } else {
    handleRegister("registerCaptcha")
  }
}

// 验证码InstanceType<typeof Verify>
const verifyRef = useTemplateRef<InstanceType<typeof TianaiCaptcha>>("tianaiCaptchaRef")

// 表单验证通过后提交注册
const showVerify = () => {
  verifyRef.value?.show()
}

// 用户注册
const handleRegister = async (captchaVerification: string) => {
  registerLoading.value = true
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
      if (registerUsername) {
        registerUsername.value = username
      }
      setTimeout(() => {
        handleChangeComponent('login')
        registerLoading.value = false
      },1000)
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
    registerLoading.value = false
  }
}
</script>

<style scoped>
.register-form-item {
  height: 48px
}

.register-title {
  margin-top: 24px;
  margin-bottom: 56px;
}
</style>
