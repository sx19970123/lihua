<template>
  <a-flex :gap="12" vertical>
    <a-flex :gap="12">
      <div class="card"
           @click="handleClickCard"
           @mousemove="handleMouseMove"
           @mouseout="handleMouseOut"
           :style="{ zIndex: cardZIndex, position: cardPosition}">
        <a-typography-title>hello world</a-typography-title>
      </div>
      <div
          class="card2"
      >
        <a-typography-title>hello world</a-typography-title>
      </div>
    </a-flex>
    <a-flex :gap="12">
      <div class="card2">
        <a-typography-title>hello world</a-typography-title>
      </div>
      <div
          class="card2"
      >
        <a-typography-title>hello world</a-typography-title>
      </div>
    </a-flex>
    <Teleport to="body">
      <div class="mask" v-if="mask" @click="handleMaskClick"></div>
    </Teleport>
  </a-flex>
</template>

<script setup lang="ts">
import { gsap  } from 'gsap';
import { ref } from 'vue';

const mask = ref<boolean>(false);
const cardZIndex = ref<number>(800);
const cardPosition = ref<string>('')
const card = ref<boolean>(true)
const spread = ref<boolean>(false)
// 鼠标悬停卡片悬浮
const handleMouseMove = () => {
  if (!spread.value) {
    gsap.to('.card', { scale: 1.02, duration: 0.1 });
  }

};

// 鼠标移除取消悬浮
const handleMouseOut = () => {
  if (!spread.value) {
    gsap.to('.card', {scale: 1, duration: 0.1});
  }
};

// 点击卡片事件
const handleClickCard = () => {
  const innerWidth = window.innerWidth
  const innerHeight = window.innerHeight

  mask.value = true;
  card.value = true
  cardZIndex.value = 1000;  // 确保卡片在蒙版上方
 //  cardPosition.value = 'fixed'
  spread.value = true
  gsap.to('.card', {x: innerWidth / 2 - 500, y: innerHeight / 2 - 400 ,width: 500, height: 500, borderRadius: 16 ,duration: 0.5 ,  ease: 'power1.inOut'});
};

// 点击蒙版隐藏蒙版和复位卡片
const handleMaskClick = () => {
  mask.value = false;
  cardZIndex.value = 800;  // 复位z-index
  spread.value = false
  gsap.to('.card', {x: 0, y: 0, width: 200, height: 200,borderRadius: 8, duration: 0.5 ,ease: 'power1.inOut'});
};
</script>

<style>
.card2 {
  padding: 16px;
  border-radius: 8px;
  height: 200px;
  width: 200px;
  border: 1px solid #d9d9d9;
}
.card {
  padding: 16px;
  border-radius: 8px;
  height: 200px;
  width: 200px;
  border: 1px solid #d9d9d9;
  position: relative; /* 确保卡片在定位时不会影响其他元素 */
  transition: z-index 0.3s;
  background-color: #fff;
}
.card:hover {
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.mask {
  position: fixed;
  z-index: 900;  /* 确保蒙版在卡片的初始层级上 */
  background: rgba(0, 0, 0, 0.7);
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>
