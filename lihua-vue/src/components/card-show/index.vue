<template>
  <div>
    <!-- 展示 overview 和 detail 的容器-->
    <div
        class="container"
        :class="props.cardKey"
        :style="style"
        :id="props.cardKey"
        @click="handleClickCard"
        @mouseover="handleMouseOverCard"
        @mouseleave="handleMouseLeaveCard"
    >
      <slot name="overview" v-if="showStatus === 'ready'"></slot>
    </div>

    <!-- 传送组建，点开内容时，将 content 传送到 id 为 props.cardKey 的元素下 -->
    <Teleport :to="'#' + props.cardKey " v-if="showStatus === 'complete'">
      <slot name="detail"></slot>
    </Teleport>

    <!-- 占位元素，复刻slot:title，会随着页面视口变化而变化，返回动画参数从该组建中获取 -->
    <div v-if="showStatus !== 'ready'" style="opacity: 0" ref="placeholder">
      <slot name="overview"></slot>
    </div>

    <!-- mask 打开时背景蒙版 -->
    <Teleport to="body">
      <div class="mask" v-if="showStatus !== 'ready'" @click="handleClose"></div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { gsap } from 'gsap';
import {onMounted, ref, watch} from "vue";
import type { CSSProperties } from 'vue';

// 接受父组件参数
const props = defineProps({
  // 组建key，用于标记组件唯一
  cardKey: {
    type: String,
    required: true
  },
  // 展开后的宽度
  expandedWidth: {
    type: Number,
    default: 500
  },
  // 展开后的高度
  expandedHeight: {
    type: Number,
    default: 500
  },
  // 展开后距离页面顶端像素
  expandedTop: {
    type: Number,
    default: 100
  }
})

const initClick = () => {
  // 占位元素的ref
  const placeholder = ref()
  // 展示的状态
  const showStatus = ref<'ready' | 'activity' | 'complete'>('ready')
  // 展开后改变css定位布局
  const style = ref<CSSProperties>({position: 'static'})
  // 点击卡片
  const handleClickCard = () => {
    // 只有就绪状态才可点击
    if (showStatus.value !== 'ready') {
      return
    }
    // 执行动画，先将缩放还原
    gsap.to('.' + props.cardKey, {
      scale: 1,
      duration: 0,
      // 缩放还原后再进行主要动画
      onComplete: () => {
        // 状态修改为进行时
        showStatus.value = 'activity'
        // container 设置为固定定位
        style.value = {position: 'fixed'}
        // 执行主要动画
        gsap.to('.' + props.cardKey, {
          left: innerWidth.value / 2 - props.expandedWidth / 2,
          top: props.expandedTop,
          width: props.expandedWidth,
          height: props.expandedHeight,
          borderRadius: 16 ,
          duration: 0.3,
          ease: 'power1.out',
          onComplete: () => {
            // 动画执行完成后，状态修改为已完成
            showStatus.value = 'complete'
          }
        })
      }
    })
  }

  // 关闭详情卡片
  const handleClose = () => {
    // 动画播放完才可关闭
    // if (showStatus.value !== 'complete') {
    //   return;
    // }
    const bounding = placeholder.value.getBoundingClientRect()
    // 状态修改为进行时
    showStatus.value = 'activity'
    // 执行主要动画
    gsap.to('.' + props.cardKey, {
      width: bounding.width,
      height: bounding.height,
      top: bounding.top,
      left: bounding.left,
      borderRadius: 8 ,
      duration: 0.3,
      ease: 'power1.out',
      onComplete: () => {
        // 恢复 container 默认的静态布局
        style.value = {position: 'static', width: '', height: '', top: '', left: ''}
        // 动画执行完成后，状态修改为就绪
        showStatus.value = 'ready'
      }
    })
  }
  return {
    showStatus,
    style,
    placeholder,
    handleClose,
    handleClickCard,
  }
}
const {showStatus, style, placeholder, handleClose, handleClickCard } = initClick()


// 加载鼠标在卡片悬浮相关逻辑
const initHover = () => {
  // 缩放状态分为 ready就绪 activity活动中 complete已完成
  const hoverStatus = ref<'ready' | 'activity' | 'complete'>('ready')
  // 鼠标悬浮于卡片
  const handleMouseOverCard = () => {
    if (hoverStatus.value === 'ready' || showStatus.value === 'ready') {
      hoverStatus.value = 'activity'
      handleAddHoverStyle()
      gsap.to('.' + props.cardKey, {
        scale: 1.05,
        duration: 0.1,
        onComplete: () => {
          hoverStatus.value = 'complete'
        }
      })
    }
  }
  // 鼠标从卡片移出
  const handleMouseLeaveCard = () => {
    if (showStatus.value === 'ready') {
      gsap.to('.' + props.cardKey, {
        scale: 1,
        duration: 0.1,
        onComplete: () => {
          hoverStatus.value = 'ready'
          handleRemoveHoverStyle()
        }
      })
    }
  }
  // 添加 hover 样式
  const handleAddHoverStyle = () => {
    style.value.cursor = 'pointer'
    style.value.borderRadius = '8px'
    style.value.boxShadow = '0 8px 16px 0 rgba(0, 0, 0, 0.08)'
  }
  // 移除 hover 样式
  const handleRemoveHoverStyle = () => {
    style.value.cursor = ''
    style.value.borderRadius = ''
    style.value.boxShadow = ''
  }
  return {
    handleMouseOverCard,
    handleMouseLeaveCard
  }
}

const {handleMouseOverCard, handleMouseLeaveCard } = initHover()

// 视口的宽度，用于定位展开后元素位置
const innerWidth = ref<number>(window.innerWidth)

// 创建监听窗口变化函数
onMounted(() => {
  window.addEventListener('resize', windowWidthResize)
})

// 窗口变化后重置 innerWidth 和 left 属性
const windowWidthResize = () => {
  innerWidth.value = window.innerWidth
  if (showStatus.value === 'complete') {
    const left = window.innerWidth / 2 - props.expandedWidth / 2
    style.value.left = left + 'px';
  }
}
</script>

<style scoped>
.container {
  border-radius: 8px;
  z-index: 100;
  background-color: #fff;
}
.card-show-title:hover {
  cursor: pointer;
  border-radius: 8px;
  box-shadow:0 8px 16px 0 rgba(0, 0, 0, 0.08)
}
.mask {
  position: fixed;
  z-index: 20;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(6px);
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>