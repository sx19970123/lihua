<template>
  <div class="login-setting scrollbar">
    <a-carousel class="login-setting-carousel" :dots="false" ref="carouselRef">
      <template v-for="(component,index) in componentList" :key="index">
<!--        防止移动端滑动和pc端通过修改css直接显示到最后一步，只加载当前显示的组件，由transition实现切换动画，keep-alive记录上一页的值-->
        <transition :name="transitionType" mode="out-in" v-if="activeComponent === component">
          <keep-alive :include="componentList" v-if="!isComplete">
            <component :is="component"
                       @back="handleBack"
                       @skip="handleSkip"
                       @next="handleNext"
                       @complete="handleComplete"
                       @goLogin="handleGoLogin"
            />
          </keep-alive>
        </transition>
      </template>
    </a-carousel>
  </div>

</template>

<script setup lang="ts">
import {ref, useTemplateRef} from "vue";
import type {CarouselRef} from "ant-design-vue/es/carousel";
import {useUserStore} from "@/stores/user.ts";
import Token from "@/utils/Token.ts";
import router from "@/router";

const emits = defineEmits(['goLogin'])

// 需要加载的设置项集合
const componentList = [
  'LoginSettingStart',
  'LoginSettingComplete'
]
// 接收需要加载的配置项
const props = defineProps<{
  componentNames: string[];
}>()
// 组合配置项
componentList.splice(1, 0, ...props.componentNames)
// 用户store
const userStore = useUserStore();
// 走马灯组件ref
const carouselRef = useTemplateRef<CarouselRef>("carouselRef")
// 显示的组件
const activeComponent = ref<string>("LoginSettingStart")
// 动画类型
const transitionType = ref<'next'|'back'>('next')
// 是否完成
const isComplete = ref<boolean>(false)
// 上一页
const handleBack = () => {
  activeComponent.value = componentList[componentList.indexOf(activeComponent.value) - 1]
  transitionType.value = 'back'
  carouselRef.value?.prev()
}

// 下一页
const handleNext = (loading:boolean) => {
  if (!loading) {
    activeComponent.value = componentList[componentList.indexOf(activeComponent.value) + 1]
    transitionType.value = 'next'
    carouselRef.value?.next()
  }
}

// 跳过
const handleSkip = (loading:boolean) => {
  handleNext(loading)
}

// 完成
const handleComplete = () => {
  isComplete.value = true
  Token.setLoginSettingResult()
  setTimeout(() => router.push("/index"), 200)
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
  transition: all 0.2s cubic-bezier(0.25, 0.0, 1, 1);
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
