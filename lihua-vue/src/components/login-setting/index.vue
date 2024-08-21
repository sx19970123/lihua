<template>
  <div class="login-setting">
    <a-carousel style="width: 600px;border: none; border-radius: 8px;" :dots="false" ref="carouselRef">
      <component :is="component"
                 :key="index"
                 v-for="(component, index) in componentList"
                 @back="handleBack"
                 @skip="handleSkip"
                 @next="handleNext"
      />
    </a-carousel>
  </div>

</template>

<script setup lang="ts">

import {ref} from "vue";
const carouselRef = ref()
const componentList = [
    'LoginSettingStart',
    'LoginSettingResetPassword',
    'LoginSettingIndividuation',
    'LoginSettingBaseInfo',
    'LoginSettingDefaultDept',
    'LoginSettingComplete'
]

const handleBack = () => {
  carouselRef.value.prev()
}

const handleSkip = (loading:boolean) => {
  if (!loading) {
    carouselRef.value.next()
  }
}

const handleNext = (loading:boolean) => {
  if (!loading) {
    carouselRef.value.next()
  }

}
</script>

<style scoped>
.login-setting{
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
:deep(.slick-list) {
  border-radius: 8px;
}
</style>