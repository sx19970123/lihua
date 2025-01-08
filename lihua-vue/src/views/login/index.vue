<template>
  <a-flex class="login-background" justify="center" align="center">
    <a-flex align="center" :gap="208" v-if="!showSetting">
      <!--      主题切换开关-->
      <head-theme-switch class="head-theme-switch"/>
<!--        左侧标题-->
      <div class="title">
        <transition name="fade" mode="out-in">
          <div v-show="showTitle">
            <a-typography-title>狸花猫后台管理系统
              <a-tag :bordered="false">{{ settings.version }}</a-tag>
            </a-typography-title>
            <a-typography-title :level="2">
              基于SpringBoot 3.x 和 vue3.x
            </a-typography-title>
          </div>
        </transition>
      </div>
<!--      右侧表单-->
      <div class="form">
        <transition name="card" mode="out-in" v-show="showCard">
          <a-card class="login-card">
            <transition name="form" mode="out-in" v-show="showCard">
              <!-- 用户登录/注册等卡片内表单在这儿通过组件形式切换 -->
              <component :is="activeComponent"
                         :enable-captcha="enableCaptcha"
                         :error-message="errorMessage"
                         @change-component="handleChangeComponent"
                         @show-login-setting="startLoginSetting"
              />
            </transition>
          </a-card>
        </transition>
      </div>
    </a-flex>
    <!--    登录设置-->
    <transition name="setting" mode="out-in">
      <login-setting :component-names="settingComponentNames"
                     v-if="showSetting"
                     @go-login="handleGoLogin"
      ></login-setting>
    </transition>
  </a-flex>

</template>

<script setup lang="ts">
import {markRaw, onMounted, provide, ref} from "vue"
import {enable} from "@/api/system/captcha/Captcha.ts";
import HeadThemeSwitch from "@/components/light-dark-switch/index.vue"
import LoginSetting from "@/components/login-setting/index.vue"
import UserRegister from "@/views/login/components/Register.vue"
import UserLogin from "@/views/login/components/Login.vue"
import {message} from "ant-design-vue";
import {ResponseError} from "@/api/global/Type.ts";
import settings from "@/settings"

// 显示登录卡片
const showCard = ref<boolean>(false)
// 显示左侧title
const showTitle = ref<boolean>(false)
// 验证码
const enableCaptcha = ref<boolean>(false)
// 错误信息
const errorMessage = ref<string>()
// 注册的用户数据，定义registerUsername后，注册组件通过inject接收值，并在注册成功后赋值为用户名，登录组件可获取后进行处理
provide("registerUsername",ref<string>())

// 初始化组件切换相关逻辑
const initChangeComponent = () => {
  // 当前显示的组件
  const activeComponent = ref()
  // 全部组件
  const allComponents = [
    {
      name: "login",
      com: markRaw(UserLogin)
    },
    {
      name: "register",
      com: markRaw(UserRegister)
    },
  ]
  // 处理切换组件
  const handleChangeComponent = (name: string) => {
    const target = allComponents.filter(component => component.name === name)
    if (!target || target.length === 0) {
      console.error("组件name未注册")
      return
    }

    activeComponent.value = target[0].com
    handleShowCard()
  }

  return {
    activeComponent,
    handleChangeComponent
  }
}
const {activeComponent, handleChangeComponent} = initChangeComponent()

const initLoginSetting = () => {
  // 是否显示setting组件
  const showSetting = ref<boolean>(false)
  // setting组件名
  const settingComponentNames = ref<string[]>([])
  // 登录后配置
  const startLoginSetting = (settingComponentNameList: string[]) => {
    if (settingComponentNameList && settingComponentNameList.length > 0) {
      showTitle.value = false
      showCard.value = false
      showSetting.value = true
      settingComponentNames.value = settingComponentNameList
    }
  }
  // 当需要登陆后配置时，刷新页面读取路由携带参数，加载配置页面
  const routerCheckLoginSetting = () => {
    startLoginSetting(history.state.settingComponentNameList)
  }
  // 从配置页面退回到登录页面
  const handleGoLogin = async () => {
    // 关闭设置页面
    showSetting.value = false
    showCard.value = false
    settingComponentNames.value = []
    // 清空路由参数
    if (history.state.settingComponentNameList) {
      history.state.settingComponentNameList = undefined
    }
    setTimeout(() => {
      // 登录卡片弹出动画
      handleShowCard()
    }, 200)
  }

  return {
    showSetting,
    settingComponentNames,
    startLoginSetting,
    routerCheckLoginSetting,
    handleGoLogin
  }
}
const {showSetting, settingComponentNames, startLoginSetting, routerCheckLoginSetting, handleGoLogin} = initLoginSetting()


// 是否启用验证码
const captcha = async () => {
  try {
    const resp = await enable()
    if (resp.code === 200) {
      enableCaptcha.value = resp.data
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      errorMessage.value = '无法访问服务器'
    } else {
      console.error(e)
    }
  }
}

// 判断重定向回来的参数，给用户合适的提示
// 提示信息显示完成后销毁，刷新页面时不再提示
const handleRedirect = () => {
  if (history.state.msg) {
    message.error(history.state.msg)
    history.state.msg = undefined
  }
}

// 显示卡片
const handleShowCard = () => {
  showCard.value = false
  showTitle.value = true
  setTimeout(() => showCard.value = true, 100)
}

onMounted(() => {
  // 默认显示login
  handleChangeComponent("login")
  // 是否启用验证码
  captcha()
  // 检查history.state中是否存在登陆后配置
  routerCheckLoginSetting()
  // 检查history.state中是否存在异常提示
  handleRedirect()
})
</script>

<style scoped>
/* 登录背景 */
.login-background {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
  background-image:linear-gradient(-135deg,#C2FFD8 10%,#465EFB 100%);
  background-size: 200% 200%;
  animation: gradientAnimation 30s ease infinite;
}

/* 渐变动画 */
@keyframes gradientAnimation {
  0% {
    background-position: 0 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}

/* 视口小于1300 像素时，隐藏title */
@media screen and (max-width: 1200px) {
  .title {
    display: none;
  }
}

/* 登录卡片 */
.login-card {
  max-width: 380px;
  padding-left: 16px;
  padding-right: 16px;
  border-radius: 24px;
}

/* 表单 */
.form {
  width: 378px;
}

/* 视口宽度小于378时，卡片取96视口宽度 居中 */
@media screen and (max-width: 378px) {
  .login-card {
    width: calc(100vw - 32px);
    margin: auto;
  }
}

/* 暗色模式切换开关 */
.head-theme-switch {
  position: absolute;
  top: 16px;
  right: 24px;
}

.card-enter-active {
  transition: all 0.8s ease-in-out;
}

.card-enter-from {
  transform: translateY(80px) scale(88%);
  opacity: 0;
}

.form-enter-active {
  transition: all 0.6s ease-in-out;
}

.form-enter-from {
  transform: translateY(24px);
  opacity: 0;
}

/* 登录后设置卡片呼出 */
.setting-enter-active {
  transition: all 0.7s cubic-bezier(0.4, 0.0, 0.10, 1);
}
.setting-enter-from {
  transform: translateY(100%);
  opacity: 0;
}
.setting-enter-to {
  transform: translateY(0);
  opacity: 1;
}

/* 登录后设置卡片隐藏 */
.setting-leave-active {
  transition: all 0.2s cubic-bezier(0.25, 0.0, 1, 1);
}
.setting-leave-from {
  transform: translateY(0);
  opacity: 1;
}
.setting-leave-to {
  transform: translateY(100%);
  opacity: 0;
}

</style>

<style>
[data-theme = dark] {
  .login-background {
    background-image: linear-gradient(-135deg, #1F7A56 0%, #2C3690 100%);
  }
}
</style>
