<template>
  <div>
    <div class="login-title">
      <a-typography-title :level="2">欢迎登陆狸花猫</a-typography-title>
      <a-typography-text v-if="errorMessage" type="danger">{{errorMessage}}</a-typography-text>
      <!--                    根据配置显示注册-->
      <div v-if="enableRegister">
        <a-typography-text>没有账户？</a-typography-text>
        <a-typography-link @click="handleChangeComponent('register')">快速注册
          <RightOutlined/>
        </a-typography-link>
      </div>
    </div>
    <a-form :model="loginForm" @finish="handleFinish" :rules="loginRoles">
      <a-form-item name="username" hasFeedback>
        <a-tooltip placement="topLeft" trigger="contextmenu" title="用户名已自动填写" v-model:open="usernameTip">
          <a-input class="login-form-item"
                   autocomplete="off"
                   v-model:value="loginForm.username"
                   placeholder="用户名"
          >
            <template #prefix>
              <UserOutlined class="input-prefix-icon-color"/>
            </template>
          </a-input>
        </a-tooltip>
      </a-form-item>
      <a-form-item name="password" hasFeedback>
        <a-input-password class="login-form-item"
                          v-model:value="loginForm.password"
                          placeholder="密码"
        >
          <template #prefix>
            <LockOutlined class="input-prefix-icon-color"/>
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
    <div v-if="false">
      <a-divider plain>其他方式</a-divider>
      <a-flex justify="space-around">
        <a-button size="large" shape="circle"><WeiboCircleOutlined /></a-button>
        <a-button size="large" shape="circle"><WechatOutlined /></a-button>
        <a-button size="large" shape="circle"><AlipayCircleOutlined /></a-button>
        <a-button size="large" shape="circle"><QqOutlined /></a-button>
        <a-button size="large" shape="circle"><GitlabOutlined /></a-button>
      </a-flex>
    </div>
    <!--    验证码-->
    <tianai-captcha ref="tianaiCaptchaRef" @success="login"/>
  </div>
</template>

<script setup lang="ts">
import {useUserStore} from "@/stores/user"
import {ResponseError, type ResponseType} from "@/api/global/Type.ts";
import TianaiCaptcha from "@/components/tianai-captcha/index.vue";
import {inject, onMounted, reactive, type Ref, ref, useTemplateRef} from "vue";
import token from "@/utils/Token.ts"
import {init} from "@/utils/AppInit.ts";
import {getLoginSetting} from "@/api/system/login/Login.ts";
import type {Rule} from "ant-design-vue/es/form";
import {message} from "ant-design-vue";
import {useRouter} from 'vue-router'
import {useSettingStore} from "@/stores/setting.ts";
import type {SignIn} from "@/api/system/setting/type/SignIn.ts";

const {enableCaptcha, errorMessage=""} = defineProps<{
  // 是否启用验证码
  enableCaptcha: boolean,
  // 错误信息
  errorMessage?: string,
}>()

const emit = defineEmits(["changeComponent","showLoginSetting"])

const router = useRouter()
const userStore = useUserStore()
const loginLoading = ref<boolean>()
const settingStore = useSettingStore()
const rememberMe = ref<boolean>(token.enableRememberMe())
const verifyRef = useTemplateRef<InstanceType<typeof TianaiCaptcha>>("tianaiCaptchaRef")
const registerUsername = inject<Ref<string|undefined>>("registerUsername")
const usernameTip = ref<boolean>(false)
// 用户登录
interface LoginFormType {
  username: string,
  password: string
}

const loginForm = reactive<LoginFormType>({
  username: '',
  password: ''
})

// 启用记住账号后赋值账号密码
const initRememberMe = () => {
  if (rememberMe.value) {
    const usernamePassword = token.getUsernamePassword()
    loginForm.username = usernamePassword.username
    loginForm.password = usernamePassword.password
  }
}

// 触发登录
const handleFinish = () => {
  if (enableCaptcha) {
    showVerify()
  } else {
    login('loginCaptcha')
  }
}

// 检查是否为注册完成跳回的页面
const checkRegister = () => {
  // 注册完成跳回的页面中registerUsername存有用户名，向表单赋值后关闭记住我功能
  if (registerUsername && registerUsername.value) {
    loginForm.username = registerUsername.value
    loginForm.password = ""
    rememberMe.value = false
    token.forgetMe()

    // 开启关闭自动带入用户名提示
    setTimeout(() => usernameTip.value = true, 900)
    setTimeout(() => usernameTip.value = false, 4000)
  }
}

// 清除注册用户名
const clearRegisterUsername = () => {
  if (registerUsername && registerUsername.value) {
    registerUsername.value = undefined
  }
}

// 登录请求
const login = async (captchaVerification: string) => {
  loginLoading.value = true
  try {
    const {code, msg}: ResponseType<string> = await userStore.login(loginForm.username, loginForm.password, captchaVerification);
    if (code === 200) {
      if (rememberMe.value) {
        token.rememberMe(loginForm.username, loginForm.password)
      } else {
        token.forgetMe()
      }
      // 清除注册用户名
      clearRegisterUsername()
      // 检查是否需要登录后设置
      const loginSettingResp = await getLoginSetting()
      if (loginSettingResp.code === 200) {
        // 登录后设置返回data为空，表示无需进行额外设置，进入首页
        if (loginSettingResp.data.length === 0) {
          message.success("登录成功")
          await router.push("/index");
        } else {
          await init()
          // 开始登录后配置
          emit("showLoginSetting", loginSettingResp.data)
        }
      } else {
        message.error(loginSettingResp.msg)
      }
    } else {
      message.error(msg);
      loginLoading.value = false
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg);
    } else {
      console.error("登录失败:", e);
    }
  } finally {
    token.removeLoginSettingResult()
    loginLoading.value = false
  }
}

// 登录表单校验
const loginRoles: Record<string, Rule[]> = {
  username: [{required: true, message: '请输入账号', trigger: 'change'}],
  password: [{required: true, message: '请输入密码', trigger: 'change'}]
}

// 显示验证码
const showVerify = () => {
  verifyRef.value?.show()
}

// 是否开启自处注册
const enableRegister = ref<boolean>(false)
const initRegisterSetting = () => {
  settingStore.getSetting<SignIn>('SignInSetting').then(resp => {
    if (resp) {
      enableRegister.value = resp.enable || false
    }
  })
}

// 处理切换组件
const handleChangeComponent = (name: string) => {
  emit('changeComponent', name)
}

onMounted(() => {
  // 检擦是否刚注册完成
  checkRegister()
  // 加载记住我
  initRememberMe()
  // 是否开启注册
  initRegisterSetting()
})
</script>

<style scoped>
.login-title {
  margin-top: 24px;
  margin-bottom: 56px;
}

.login-form-item {
  height: 48px
}
</style>