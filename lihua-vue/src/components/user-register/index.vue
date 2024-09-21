<template>
  <div>
    <a-card class="register">
      <transition name="form" mode="out-in">
        <div style="margin: 16px" v-if="showForm">
          <div class="register-title">
            <a-typography-title :level="2">欢迎注册狸花猫</a-typography-title>
            <a-typography-text>已有账户？</a-typography-text>
            <a-typography-link @click="goLogin(false)">前往登录
              <RightOutlined/>
            </a-typography-link>
          </div>
          <a-form :model="userRegister" :rules="rules" @finish="handleFinish">
            <a-form-item name="username" hasFeedback>
              <a-input class="register-form-item"
                       autocomplete="off"
                       placeholder="用户名"
                       v-model:value="userRegister.username"
              >
                <template #prefix>
                  <UserOutlined style="color: rgba(0, 0, 0, 0.25)"/>
                </template>
              </a-input>
            </a-form-item>
            <a-form-item class="form-item-single-line" name="password" hasFeedback>
              <a-input-password class="register-form-item"
                                placeholder="密码"
                                v-model:value="userRegister.password"
                                @change="handleChange"
              >
                <template #prefix>
                  <LockOutlined style="color: rgba(0, 0, 0, 0.25)"/>
                </template>
              </a-input-password>
            </a-form-item>
            <a-flex style="margin-bottom: 16px;">
              <a-progress class="form-item-width"
                          style="margin: 8px"
                          :showInfo="false"
                          :size="[90,3]"
                          :steps="3"
                          :percent="passwordLevel"
                          :strokeColor="['#ff4d4f','#faad14','#52c41a']"/>
            </a-flex>
            <a-form-item name="confirmPassword" hasFeedback>
              <a-input-password class="register-form-item"
                                placeholder="再次输入密码"
                                v-model:value="userRegister.confirmPassword"
              >
                <template #prefix>
                  <LockOutlined style="color: rgba(0, 0, 0, 0.25)"/>
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
import type {Rule} from "ant-design-vue/es/form";
import {checkUserName, register} from "@/api/system/login/Login.ts";
import {message} from "ant-design-vue";
import Verify from "@/components/verifition/index.vue";

// 向父组件抛出切登录方法
const emits = defineEmits(['goLogin'])
const goLogin = (clearLoginForm: boolean) => {
  emits('goLogin', clearLoginForm)
}

// 控制表单动画
const showForm = ref<boolean>(false)
setTimeout(() => {
  showForm.value = true
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
// 密码等级
const passwordLevel = ref<number>(0)
// 处理密码强度提示
const handleChange = () => {
  const weakRegex = /^(?=.*[a-zA-Z])[\w!@#$%^&*]{6,}$/;
  const mediumRegex = /^(?=.*\d)(?=.*[a-zA-Z])[\w!@#$%^&*]{8,}$/;
  const strongRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)(?=.*[\w!@#$%^&*]).{10,}$/;
  const password = userRegister.value.password
  if (password) {
    if (weakRegex.test(password)){
      passwordLevel.value = 30
    }
    if (mediumRegex.test(password)){
      passwordLevel.value = 60
    }
    if (strongRegex.test(password)){
      passwordLevel.value = 90
    }
  } else {
    passwordLevel.value = 0
  }
}

// 对比两次密码输入是否相同
const equalToPassword = (rule: any, value: string, callback:Function) => {
  if (userRegister.value.password !== value) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
}

// 失焦时触发检查用户名是否可用
const handleCheckUsername = (rule: any, value: string, callback:Function) => {
  if (value) {
    checkUserName(value).then(resp => {
      if (resp.code === 200) {
        if (!resp.data) {
          callback(new Error("该用户名已存在"))
        } {
          callback();
        }
      } else {
        message.error(resp.msg)
      }
    })
  } else {
    callback()
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
  const resp = await register(userRegister.value.username,userRegister.value.password,userRegister.value.confirmPassword, captchaVerification)

  if (resp.code === 200) {
    message.success("注册成功，即将前往登录")
    setTimeout(() => {
      goLogin(true)
    },1000)
  } else {
    message.error(resp.msg)
  }
}
</script>

<style scoped>
.register {
  max-width: 380px;
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
