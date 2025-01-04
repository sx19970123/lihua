<template>
  <div class="login-setting scrollbar">
    <a-carousel class="login-setting-carousel" :dots="false" ref="carouselRef">
      <component :is="component"
                 :key="index"
                 v-for="(component, index) in componentList"
                 @back="handleBack"
                 @skip="handleSkip"
                 @next="handleNext"
                 @goLogin="handleGoLogin"
      />
    </a-carousel>
  </div>

</template>

<script setup lang="ts">
import {ref, useTemplateRef} from "vue";
import type {CarouselRef} from "ant-design-vue/es/carousel";
import {useUserStore} from "@/stores/user.ts";

const userStore = useUserStore();
const carouselRef = useTemplateRef<CarouselRef>("carouselRef")
const emits = defineEmits(['goLogin'])
// 需要加载的设置项集合
const componentList = ref<string[]>(
    [
      'LoginSettingStart',
      'LoginSettingComplete'
    ]
)
// 接收需要加载的配置项
const props = defineProps<{
  componentNames: string[];
}>()

// 组合配置项
componentList.value.splice(1, 0, ...props.componentNames)

// 上一页
const handleBack = () => {
  carouselRef.value?.prev()
}
// 跳过
const handleSkip = (loading:boolean) => {
  if (!loading) {
    carouselRef.value?.next()
  }
}
// 下一页
const handleNext = (loading:boolean) => {
  if (!loading) {
    carouselRef.value?.next()
  }
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
    width: 96vw;
    margin: auto;
  }
}

:deep(.slick-list) {
  border-radius: 8px;
}
</style>
