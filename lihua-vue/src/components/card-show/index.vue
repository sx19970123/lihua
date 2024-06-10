<template>
  <div>
    <!-- 展示 overview 和 detail 的容器-->
    <div
        class="card-show-container"
        ref="containerRef"
        :class="props.cardKey"
        :style="style"
        @click="handleClickCard"
        @mouseover="handleMouseOverCard"
        @mouseleave="handleMouseLeaveCard"
    >
      <!-- 展示概述信息 -->
      <div v-if="showStatus === 'ready'">
        <slot name="overview"></slot>
      </div>
      <!-- 过渡时展示自定义封面 -->
      <div v-if="showStatus === 'activity'" class="card-show-middle">
        <slot name="middle"></slot>
      </div>
      <!-- 当 showStatus !== 'complete' 时，执行动画前为获取其高度，会暂时将display设置为block，这不能在页面显示，所以showStatus === 'complete'之前设置为全透明 -->
      <div v-show="showStatus === 'complete'" ref="detailRef" :style="showStatus === 'complete' ? 'opacity: 1' : 'opacity: 0'">
        <slot name="detail"></slot>
      </div>
    </div>

    <!-- 占位元素，复刻slot:title，会随着页面视口变化而变化，返回动画参数从该组建中获取 -->
    <div v-if="showStatus !== 'ready'" style="opacity: 0" ref="placeholderRef">
      <slot name="overview"></slot>
    </div>

    <!-- mask 打开时背景蒙版 -->
    <Teleport to="body">
      <div class="card-show-mask" v-if="showStatus !== 'ready'" @click="handleClose"></div>
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
    type: Number
  },
  // 展开后的高度
  expandedHeight: {
    type: Number
  },
  // 展开后距离页面顶端像素
  expandedTop: {
    type: Number,
    default: 100
  },
  // 鼠标悬浮缩放倍率
  hoverScale: {
    type: Number,
    default: 1.05
  },
  // 自动完成，是否通过外部控制组件middle状态
  autoComplete: {
    type: Boolean,
    default: true
  },
  // 当 autoComplete false 时，是用 isComplete 控制 middle 遮罩是否关闭
  isComplete: {
    type: Boolean
  },
  // 是否展示详情
  isDetailVisible: {
    type: Boolean,
    default: true
  }
})

// 动画状态类型
type StatusType = 'ready' | 'activity' | 'complete'

// 定义向外抛出的函数
/**
 * cardClick 点击卡片时触发
 * cardComplete 卡片展开完成时触发
 * cardReady 卡片折叠完成时触发
 * cardReadyOver 鼠标移入卡片时触发
 * cardReadyLeave 鼠标移出卡片时触发
 * */
const emits = defineEmits(['cardClick','cardComplete','cardReady','cardReadyOver','cardReadyLeave','maskClick'])

const initClick = () => {
  // 占位元素的ref
  const placeholderRef = ref()
  // 容器元素ref
  const containerRef = ref()
  // 详情ref
  const detailRef = ref()

  // 展示的状态
  const showStatus = ref<StatusType>('ready')
  // 展开后改变css定位布局
  const style = ref<CSSProperties>({position: 'static'})
  // 详情可见
  const detailVisible = showStatus.value === 'ready' && props.isDetailVisible

  // 点击卡片
  const handleClickCard = () => {
    // todo 抛出函数
    emits('cardClick', { key: props.cardKey, detailVisible: detailVisible})

    // 只有就绪状态才可点击
    if (!detailVisible) {
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
        gsap.fromTo('.' + props.cardKey, {
          width: getOverviewWidth()
        },{
          left: innerWidth.value / 2 - getDetailWidth() / 2,
          top: props.expandedTop,
          width: getDetailWidth(),
          height: getDetailHeight(),
          duration: 0.3,
          ease: 'power1.out',
          onComplete: () => {
            // todo 抛出函数
            if (props.autoComplete || props.isComplete) {
              showStatus.value = 'complete'
              emits('cardComplete', props.cardKey)
            }
          }
        })
      }
    })
  }

  // 关闭详情卡片
  const handleClose = () => {
    // 动画播放完才可关闭
    if (showStatus.value !== 'complete') {
      return;
    }
    // todo 点击遮罩函数
    emits('maskClick', props.cardKey)
    const bounding = placeholderRef.value.getBoundingClientRect()
    // 状态修改为进行时
    showStatus.value = 'activity'
    // 执行主要动画
    gsap.to('.' + props.cardKey, {
      width: bounding.width,
      height: bounding.height,
      top: bounding.top,
      left: bounding.left,
      duration: 0.3,
      ease: 'power1.out',
      onComplete: () => {
        // 恢复 container 默认的静态布局
        style.value = {position: 'static', width: '', height: '', top: '', left: ''}
        // 动画执行完成后，状态修改为就绪
        showStatus.value = 'ready'
        // todo 抛出函数
        emits('cardReady', props.cardKey)
      }
    })
  }

  // 获取概述宽度
  const getOverviewWidth = () => {
    return containerRef.value.getBoundingClientRect().width
  }

  // 获取展开后高度
  const getDetailHeight = () => {
    if (props.expandedHeight) {
      return props.expandedHeight
    }
    detailRef.value.style.display = 'block'
    const bounding = detailRef.value.getBoundingClientRect()
    detailRef.value.style.display = 'none'
    return bounding.height
  }

  // 获取展开后的宽度
  const getDetailWidth = () => {
    if (props.expandedWidth) {
      return props.expandedWidth
    }
    detailRef.value.style.display = 'block'
    const bounding = detailRef.value.getBoundingClientRect()
    detailRef.value.style.display = 'none'
    return bounding.width
  }
  return {
    showStatus,
    style,
    placeholderRef,
    containerRef,
    detailRef,
    detailVisible,
    handleClose,
    handleClickCard,
    getDetailWidth
  }
}
const {showStatus, style, placeholderRef, containerRef, detailRef, detailVisible, handleClose, handleClickCard,getDetailWidth } = initClick()


// 加载鼠标在卡片悬浮相关逻辑
const initHover = () => {
  // 缩放状态分为 ready就绪 activity活动中 complete已完成
  const hoverStatus = ref<StatusType>('ready')
  // 鼠标悬浮于卡片
  const handleMouseOverCard = () => {
    if (hoverStatus.value === 'ready' || showStatus.value === 'ready') {
      // todo 抛出函数
      emits('cardReadyOver', props.cardKey)

      hoverStatus.value = 'activity'
      handleAddHoverStyle()
      gsap.to('.' + props.cardKey, {
        scale: props.hoverScale,
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
          // todo 抛出函数
          emits('cardReadyLeave', props.cardKey)
        }
      })
    }
  }
  // 添加 hover 样式
  const handleAddHoverStyle = () => {
    if (props.isDetailVisible) {
      style.value.cursor = 'pointer'
    }
    style.value.boxShadow = '0 8px 16px 0 rgba(0, 0, 0, 0.08)'
  }
  // 移除 hover 样式
  const handleRemoveHoverStyle = () => {
    style.value.cursor = ''
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
    const left = window.innerWidth / 2 - getDetailWidth() / 2
    style.value.left = left + 'px';
  }
}

// 监听 isComplete 变化，当 autoComplete 为 false 时，isComplete 为true 改变 showStatus 状态
watch(() => props.isComplete, (value) => {
  if (!props.autoComplete && value && detailVisible) {
    showStatus.value = 'complete'
  }
})
</script>

<style scoped>
.card-show-container {
  position: relative;
  z-index: 100;
}
.card-show-middle {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  background-color: rgba(0,0,0,0.1)
}
.card-show-mask {
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