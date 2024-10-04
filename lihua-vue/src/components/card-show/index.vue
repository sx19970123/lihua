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
      <div v-if="showStatus === 'activity' || showStatus === 'kill'" class="card-show-middle">
        <div style="display: flex; height: 100%; width: 100%" :style="props.middleStyle">
          <div style="margin: auto">
            <!-- 居中显示的元素 -->
            <slot name="middle"></slot>
          </div>
        </div>
      </div>
      <!-- 当 showStatus !== 'complete' 时，执行动画前为获取其高度，会暂时将display设置为block，这不能在页面显示，所以showStatus === 'complete' 之前设置为全透明 -->
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
      <div class="card-show-mask" v-show="showMask" @click="handleClose($event, 'mask')"></div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { gsap } from 'gsap';
import {onMounted, onUnmounted, ref, useTemplateRef, watch} from "vue";
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
  // 展开后距离页面顶端像素
  expandedTop: {
    type: Number,
    default: 100
  },
  // 过渡中的css
  middleStyle: {
    type: Object
  },
  // 鼠标悬浮缩放倍率
  hoverScale: {
    type: Number,
    default: 1.05
  },
  // 自动完成，是否通过外部控制组件middle状态
  // 设置为 false 时，可通过外部参数控制 isComplete 进行内容显示
  // 比如异步调用时，在响应返回之前，可通过参数 将 isComplete 设置为false，这时当动画播放完成也不会显示展开后的内容
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
type StatusType = 'ready' | 'activity' | 'complete' | 'kill'

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
  const placeholderRef = useTemplateRef<HTMLElement>("placeholderRef")
  // 容器元素ref
  const containerRef = useTemplateRef<HTMLElement>("containerRef")
  // 详情ref
  const detailRef = useTemplateRef<HTMLElement>("detailRef")

  // 展示的状态
  const showStatus = ref<StatusType>('ready')
  // 展开后改变css定位布局
  const style = ref<CSSProperties>({position: 'static'})

  // 显示遮罩
  const showMask = ref<boolean>(false)

  // 点击卡片
  const handleClickCard = () => {
    // 详情可见
    const detailVisible = showStatus.value === 'ready' && props.isDetailVisible
    // 卡片点击事件抛出
    emits('cardClick', { key: props.cardKey, detailVisible: detailVisible})

    // 只有就绪状态才可点击
    if (!detailVisible) {
      return
    }

    const bounding = containerRef.value?.getBoundingClientRect()
    // 执行动画，先将缩放还原
    gsap.to('.' + props.cardKey, {
      scale: 1,
      duration: 0.1,
      // 缩放还原后再进行主要动画
      onComplete: () => {
        // 打开遮罩
        showMask.value = true
        // 关闭Y轴滚动条
        hiddenOverflowY()
        // 状态修改为进行时
        showStatus.value = 'activity'
        // container 设置为固定定位
        style.value = {position: 'fixed'}
        // 执行主要动画
        gsap.fromTo('.' + props.cardKey, {
          width: bounding?.width,
          left: bounding?.left,
          top:  bounding?.top
        },{
          left: innerWidth.value / 2 - getDetailWidth() / 2,
          top: props.expandedTop,
          width: getDetailWidth(),
          height: getDetailHeight(),
          duration: 0.40,
          ease: 'power2.out',
          onComplete: () => {
            // 动画播完 或 外部控制为已完成 并且 动画状态不为kill时，展示卡片内容
            if ((props.autoComplete || props.isComplete) && showStatus.value !== 'kill') {
              showStatus.value = 'complete'
              emits('cardComplete', props.cardKey)
            }
          }
        })
      }
    })
  }

  // 关闭详情卡片
  const handleClose = (event: KeyboardEvent | MouseEvent, type: string) => {

    // 关闭状态的卡片无法被触发
    if (showStatus.value === 'ready') {
      return;
    }

    // 键盘触发后判断是不是 esc 键
    if (type === 'keydown' && event instanceof KeyboardEvent && event.key !== 'Escape') {
      return;
    }

    // 动画播放完才可关闭
    if (showStatus.value !== 'complete') {
      showStatus.value = 'kill'
      // return;
    }

    // 点击遮罩后抛出
    emits('maskClick', props.cardKey)
    const bounding = placeholderRef.value?.getBoundingClientRect()
    // 状态修改为进行时
    if (showStatus.value !== 'kill') {
      showStatus.value = 'activity'
    }
    // 打开y轴滚动条
    showOverflowY()
    // 关闭遮罩
    showMask.value = false
    // 执行主要动画
    gsap.to('.' + props.cardKey, {
      width: bounding?.width,
      height: bounding?.height,
      top: bounding?.top,
      left: bounding?.left,
      duration: 0.4,
      ease: 'power2.out',
      onComplete: () => {
        // 恢复 container 默认的静态布局
        style.value = {position: 'static', width: '', height: '', top: '', left: ''}
        // 动画执行完成后，状态修改为就绪
        showStatus.value = 'ready'
        // 卡片关闭动画完成后抛出
        emits('cardReady', props.cardKey)
      }
    })
  }

  // 获取展开后高度
  const getDetailHeight = () => {
    const ref = detailRef.value
    if (ref) {
      ref.style.display = 'block'
      const bounding = ref.getBoundingClientRect()
      ref.style.display = 'none'
      return bounding?.height
    }

    return 0;
  }

  // 获取展开后的宽度
  const getDetailWidth = () => {
    if (props.expandedWidth) {
      return props.expandedWidth
    }
    const ref = detailRef.value

    if (ref) {
      ref.style.display = 'block'
      const bounding = ref.getBoundingClientRect()
      ref.style.display = 'none'
      return bounding.width
    }

    return 0;
  }

  // 隐藏滚动条
  const hiddenOverflowY = () => {
    if (hasScrollbar()) {
      document.body.style.overflowY = 'hidden'
      document.body.style.width = 'calc(100% - 15px)'

    }
  }

  // 显示滚动条
  const showOverflowY = () => {
    if (hasScrollbar()) {
      document.body.style.overflowY = 'auto'
      document.body.style.width = '100%'
    }
  }

  // 判断是否出现了滚动条
  const hasScrollbar = (): boolean => {
    // 获取页面的总高度和视口的高度
    const body = document.body;
    const html = document.documentElement;

    const scrollHeight = Math.max(body.scrollHeight, body.offsetHeight,
        html.clientHeight, html.scrollHeight, html.offsetHeight);
    const clientHeight = window.innerHeight;

    // 判断是否出现滚动条
    return scrollHeight > clientHeight;
  }

  return {
    showStatus,
    showMask,
    style,
    placeholderRef,
    containerRef,
    detailRef,
    handleClose,
    handleClickCard,
    getDetailWidth
  }
}
const {showStatus, showMask, style, placeholderRef, containerRef, detailRef, handleClose, handleClickCard,getDetailWidth } = initClick()


// 加载鼠标在卡片悬浮相关逻辑
const initHover = () => {
  // 缩放状态分为 ready就绪 activity活动中 complete已完成
  const hoverStatus = ref<StatusType>('ready')
  // 鼠标悬浮于卡片
  const handleMouseOverCard = () => {
    // 动画在下面三种状态时，不会触发卡片悬浮
    if (showStatus.value === 'activity' || showStatus.value === 'kill' || showStatus.value === 'complete') {
      return
    }

    if (hoverStatus.value === 'ready' || hoverStatus.value === 'complete' ) {
      // 鼠标悬浮时抛出方法
      emits('cardReadyOver', props.cardKey)
      handleAddHoverStyle()
      hoverStatus.value = 'activity'
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
          // 鼠标悬浮结束后抛出
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
    style.value.borderRadius = '8px'
  }
  // 移除 hover 样式
  const handleRemoveHoverStyle = () => {
    style.value.cursor = ''
    style.value.boxShadow = ''
    style.value.borderRadius = ''
  }
  return {
    handleMouseOverCard,
    handleMouseLeaveCard
  }
}

const {handleMouseOverCard, handleMouseLeaveCard } = initHover()

// 视口的宽度，用于定位展开后元素位置
const innerWidth = ref<number>(window.innerWidth)

// 监听窗口变化和键盘事件
onMounted(() => {
  window.addEventListener('resize', windowWidthResize)
  window.addEventListener("keydown", (event) => handleClose(event, 'keydown'));
})
// 卸载组件前删除监听函数
onUnmounted(() => {
  window.removeEventListener('resize', windowWidthResize)
  window.removeEventListener("keydown", (event) => handleClose(event, 'keydown'));
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
  if (!props.autoComplete && props.isDetailVisible && showStatus.value === 'activity' && value) {
    showStatus.value = 'complete'
  }
})
</script>

<style scoped>
.card-show-container {
  position: relative;
  z-index: 101;
}
.card-show-middle {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  background-color: rgba(0,0,0,0)
}
.card-show-mask {
  position: fixed;
  z-index: 100;
  background: rgba(0, 0, 0, 0.45);
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}
</style>

<style>
[data-ground-glass = glass] {
  .card-show-mask {
    backdrop-filter: blur(6px);
  }
}
</style>