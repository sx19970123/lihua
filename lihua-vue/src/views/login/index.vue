<template>
  <a-flex class="login-background" justify="center" align="center">
    <a-flex align="center" :gap="208" v-if="!showUserSetup">
<!--      主题切换开关-->
      <theme-switch class="theme-switch"/>
<!--        左侧标题-->
      <div class="title">
        <transition name="fade" mode="out-in">
          <div v-show="showTitle">
            <a-typography-title>狸花猫后台管理系统
              <a-tag :bordered="false">{{ settings.version }}</a-tag>
            </a-typography-title>
            <a-typography-title :level="2">
              基于SpringBoot 4.x 和 vue3.x
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
              <component :is="activeComponent" @change-component="handleChangeComponent" @start-user-setup="startUserSetup"/>
            </transition>
          </a-card>
        </transition>
      </div>
    </a-flex>
<!--    进入系统前基础信息设置-->
    <transition name="setting" mode="out-in">
      <user-setup-index :component-names="componentNameList" v-if="showUserSetup" @go-login="handleGoLogin" />
    </transition>
  </a-flex>
</template>

<script setup lang="ts">
import {markRaw, onMounted, provide, ref} from "vue"
import ThemeSwitch from "@/components/light-dark-switch/index.vue"
import UserSetupIndex from "@/components/user-setup/index.vue"
import UserRegister from "@/views/login/components/Register.vue"
import UserLogin from "@/views/login/components/Login.vue"
import settings from "@/settings.ts"
import userSetup from "@/helpers/user-setup.ts"
import {screenUnlock} from "@/helpers/lock-screen.ts"
import {enableOverflowY} from "@/utils/scrollbar.ts"
// 显示登录卡片
const showCard = ref<boolean>(false)
// 显示左侧title
const showTitle = ref<boolean>(false)

// 注册的用户数据，定义registerUsername后，注册组件通过inject接收值，并在注册成功后赋值为用户名，登录组件可获取后进行处理
provide("registerUsername", ref<string>())

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

const initUserSetup = () => {
  // 是否展示登录后检查相关组件
  const showUserSetup = ref<boolean>(false)
  // 需要完善信息的组件
  const componentNameList = ref<string[]>([])
  // 开始完善登录后信息
  const startUserSetup = (items: string[]) => {
    if (items && items.length > 0) {
      showTitle.value = false
      showCard.value = false
      showUserSetup.value = true
      componentNameList.value = items
    }
  }
  // 前置检查
  const checkUserSetup = () => {
    const checkItem = userSetup.getData()
    if (checkItem && checkItem.length > 0) {
      setTimeout(() => startUserSetup(checkItem))
    }
  }
  // 从配置页面退回到登录页面
  const handleGoLogin = async () => {
    // 关闭设置页面
    showUserSetup.value = false
    showCard.value = false
    userSetup.clearData()
    componentNameList.value = []
    setTimeout(() => {
      // 登录卡片弹出动画
      handleShowCard()
    }, 200)
  }

  return {
    showUserSetup,
    componentNameList,
    checkUserSetup,
    startUserSetup,
    handleGoLogin
  }
}
const {showUserSetup, componentNameList, checkUserSetup, startUserSetup, handleGoLogin} = initUserSetup()

// 显示卡片
const handleShowCard = () => {
  showCard.value = false
  showTitle.value = true
  setTimeout(() => showCard.value = true, 100)
}

onMounted(() => {
  // 默认显示login
  handleChangeComponent("login")
  // 进入登录页的用户关闭锁屏
  screenUnlock()
  // 启用y轴滚动条，防止锁屏状态下登录失效后滚动条消失的问题
  enableOverflowY()
  // 检查是否存在登录必要配置的项
  checkUserSetup()
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
  padding-left: var(--lihua-space-base);
  padding-right: var(--lihua-space-base);
  border-radius: var(--lihua-radius-lg);
}

/* 表单 */
.form {
  width: 378px;
}

/* 视口宽度小于378时，卡片取96视口宽度 居中 */
@media screen and (max-width: 378px) {
  .login-card {
    width: calc(100vw - var(--lihua-space-xl));
    margin: auto;
  }
}

/* 暗色模式切换开关 */
.theme-switch {
  position: absolute;
  top: var(--lihua-space-base);
  right: var(--lihua-space-lg);
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

/* 公共优化 */
.setting-enter-active,
.setting-leave-active {
  will-change: transform, opacity;
  transform: translate3d(0, 0, 0);
}

/* 登录后设置卡片呼出 */
.setting-enter-active {
  transition:
      transform 0.7s cubic-bezier(0.22, 1, 0.36, 1),
      opacity   0.5s ease-out;
}

.setting-enter-from {
  transform: translate3d(0, 100%, 0);
  opacity: 0;
}

.setting-enter-to {
  transform: translate3d(0, 0, 0);
  opacity: 1;
}

/* 登录后设置卡片隐藏 */
.setting-leave-active {
  transition:
      transform 0.35s cubic-bezier(0.4, 0.0, 0.2, 1),
      opacity   0.25s ease-in;
}

.setting-leave-from {
  transform: translate3d(0, 0, 0);
  opacity: 1;
}

.setting-leave-to {
  transform: translate3d(0, 100%, 0);
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
