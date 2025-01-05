<template>
  <div class="login-setting scrollbar">

      <a-carousel class="login-setting-carousel" :dots="false" ref="carouselRef">
        <template v-for="(component,index) in componentList" :key="index">
          <transition :name="tt" mode="out-in" v-if="currentComponent === component">
            <keep-alive :include="componentList">
              <component :is="component"
                         @back="handleBack"
                         @skip="handleSkip"
                         @next="handleNext"
                         @goLogin="handleGoLogin"
              />
            </keep-alive>
          </transition>
        </template>
      </a-carousel>
  </div>

</template>

<script setup lang="ts">
import {onUnmounted, ref, useTemplateRef} from "vue";
import type {CarouselRef} from "ant-design-vue/es/carousel";
import {useUserStore} from "@/stores/user.ts";
import {message} from "ant-design-vue";

const emits = defineEmits(['goLogin'])
// 真正加载的设置项集合
const activeComponentList = ref<string[]>(['LoginSettingStart'])
// 需要加载的设置项集合
const componentList = [
  'LoginSettingStart',
  'LoginSettingComplete'
]
// 接收需要加载的配置项
const props = defineProps<{
  componentNames: string[];
}>()
const t = ref<boolean>(componentList[0] === 'LoginSettingStart')
// 组合配置项
componentList.splice(1, 0, ...props.componentNames)
// 用户store
const userStore = useUserStore();
// 走马灯组件ref
const carouselRef = useTemplateRef<CarouselRef>("carouselRef")

const currentComponent = ref<string>("LoginSettingStart")

const tt = ref<'next'|'back'>('next')
// 上一页
const handleBack = () => {
  const backComponent = componentList[componentList.indexOf(activeComponentList.value[0]) - 1]
  if (backComponent) {
    currentComponent.value = backComponent
    activeComponentList.value.unshift(backComponent)
    tt.value = 'back'
    carouselRef.value?.prev()
  } else {
   message.error("获取上一配置项异常")
  }
}
// 下一页
const handleNext = (loading:boolean) => {
  if (!loading) {
    const nextComponent = componentList[componentList.indexOf(activeComponentList.value[0]) + 1]
    if (nextComponent) {
      currentComponent.value = nextComponent
      activeComponentList.value.push(nextComponent)
      tt.value = 'next'
      carouselRef.value?.next()
    } else {
     message.error("获取下一配置项异常")
    }
  }
}
// 跳过
const handleSkip = (loading:boolean) => {
  handleNext(loading)
}
// 退回登录
const handleGoLogin = async () => {
  // 调用退出接口
  await userStore.handleLogout()
  // 调用父方法
  emits('goLogin')
}
</script>

<style scoped>
.login-setting{
  position: fixed;
  margin: auto;
  max-height: 100vh;
}
.login-setting-carousel {
  width: 600px;
  border: none;
  border-radius: 8px;
}

@media screen and (max-width: 600px) {
  .login-setting-carousel {
    width: calc(100vw - 32px);
    margin: auto;
  }
}

:deep(.slick-list) {
  border-radius: 8px;
}

.next-leave-active {
  transition: all 0.25s ease-in;
}
.next-enter-active {
  transition: all 0.4s cubic-bezier(0.4, 0.0, 0.10, 1);
}

.back-leave-active {
  transition: all 0.25s cubic-bezier(0.4, 0.0, 0.10, 1);
}
.back-enter-active {
  transition: all 0.4s ease-out;
}


.next-enter-from {
  transform: translateX(50%);
  opacity: 0;
}
.next-enter-to {
  transform: translateX(0);
  opacity: 1;
}
.next-leave-from {
  scale: 1;
  opacity: 1;
}
.next-leave-to {
  scale: .5;
  opacity: 0;
}

.back-enter-from {
  scale: .5;
  opacity: 0;
}
.back-enter-to {
  scale: 1;
  opacity: 1;
}
.back-leave-from {
  transform: translateX(0);
  opacity: 1;
}
.back-leave-to {
  transform: translateX(70%);
  opacity: 0;
}

</style>
