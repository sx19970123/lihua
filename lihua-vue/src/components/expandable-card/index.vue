<template>
  <div>
    <!-- 展示 overview 和 detail 的容器-->
    <div class="expandable-card"
         ref="containerRef"
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
      <div v-show="showStatus === 'activity' || showStatus === 'kill'"
           class="expandable-card-middle">
<!--        使用了自定义过渡插槽-->
        <slot name="middle" v-if="hasMiddleSlot"/>
<!--        没使用自定义过渡，过渡时展示详情插槽-->
        <slot name="detail" v-else/>
      </div>
      <!-- 需要动态设置detail内的元素高度，所以需要保留detail的dom节点，所以showStatus === 'complete' 之前设置为全透明 -->
      <div v-show="showStatus === 'complete'" ref="detailRef" :style="{opacity: showStatus === 'complete' ? 1 : 0}">
        <slot name="detail"></slot>
      </div>
    </div>

    <!-- 占位元素，复刻slot:title，会随着页面视口变化而变化，返回动画参数从该组建中获取 -->
    <div v-if="showStatus !== 'ready'" style="opacity: 0" ref="placeholderRef">
      <slot name="overview"></slot>
    </div>

    <!-- mask 打开时背景蒙版 -->
    <Mask :show-mask="showMask" @click="handleClose($event, 'mask')"/>
  </div>
</template>

<script setup lang="ts">
import Mask from "@/components/mask/index.vue"
import { gsap } from 'gsap';
import {nextTick, onMounted, onUnmounted, ref, useSlots, useTemplateRef, watch} from "vue";
import type { CSSProperties } from 'vue';
import {hiddenOverflowY} from "@/utils/Scrollbar.ts";
// 是否使用具名插槽middle
const slots = useSlots();
const hasMiddleSlot = !!slots.middle
// 接受父组件参数
const props = defineProps({
  // 展开后的宽度
  expandedWidth: {
    type: Number,
    required: true
  },
  // 展开后的高度
  expandedHeight: {
    type: Number,
    required: true
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
  },
  // 窗口缩小时的最小间距
  minWindowSpace: {
    type: Number,
    default: 16
  }
})

// 动画状态类型
type StatusType = 'ready' | 'activity' | 'complete' | 'kill'

// 定义向外抛出的函数
/**
 * cardClick          点击卡片触发（卡片就绪状态下点击卡片触发）
 * beforeCardExpand   卡片展开前触发（卡片就绪状态下点击卡片触发）
 * afterCardExpand    卡片展开前触后（卡片展开完成后触发）
 * beforeCardClose    卡片关闭前触发（卡片展开状态下触发关闭时触发）
 * afterCardClose     卡片关闭后触发（卡片关闭完成后触发）
 * onMouseEnter       鼠标移入卡片时触发
 * onMouseLeave       鼠标移出卡片时触发
 * */
const emits = defineEmits(['cardClick','beforeCardExpand','afterCardExpand','beforeCardClose','afterCardClose','onMouseEnter','onMouseLeave'])

// 初始化ref
const init = () => {
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
  // 展开后的高度
  const expandedHeight = ref<number>(props.expandedHeight)
  // 显示遮罩
  const showMask = ref<boolean>(false)

  // 点击卡片
  const handleClickCard = () => {
    // 详情可见
    const detailVisible = showStatus.value === 'ready' && props.isDetailVisible
    // 卡片点击事件抛出
    emits('cardClick', detailVisible)

    // 只有就绪状态才可点击
    if (!detailVisible) {
      return
    }
    const bounding = containerRef.value?.getBoundingClientRect()

    // 即将执行动画前触发
    emits('beforeCardExpand')
    // 执行动画，先将缩放还原
    gsap.to(containerRef.value, {
      scale: 1,
      duration: 0.1,
      onStart: () => {
        // 缩放状态设置为进行中
        hoverStatus.value = 'activity'
      },
      // 缩放还原后再进行主要动画
      onComplete: () => {
        // container 设置为固定定位
        style.value = {position: 'fixed'}
        // 获取展开后参数
        const side = innerWidth.value / 2 - getDetailWidth() / 2
        const height = getDetailHeight()
        const width = getDetailWidth()
        const top = detailTop.value
        // 为展开后高度赋值
        expandedHeight.value = height
        // 执行主要动画
        gsap.fromTo(containerRef.value, {
          width: bounding?.width,
          left: bounding?.left,
          right: bounding?.right,
          top:  bounding?.top,
          opacity: 0,
        },{
          left: side,
          right: side,
          top: top,
          width: width,
          height: height,
          opacity: 1,
          duration: 0.4,
          ease: 'power2.out',
          onStart: () => {
            // 打开遮罩
            showMask.value = true
            // 状态修改为进行时
            showStatus.value = 'activity'
          },
          onComplete: () => {
            // 动画播完 或 外部控制为已完成 并且 动画状态不为kill时，展示内容
            if ((props.autoComplete || props.isComplete) && showStatus.value !== 'kill') {
              handleExpandComplete()
            }
          }
        })
      }
    })
  }

  // 监听键盘触发关闭
  const keydownClose = (event: KeyboardEvent | MouseEvent) => {
    handleClose(event, 'keydown')
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
    }

    const bounding = placeholderRef.value?.getBoundingClientRect()
    // 执行主要动画
    gsap.to(containerRef.value, {
      width: bounding?.width,
      height: bounding?.height,
      top: bounding?.top,
      left: bounding?.left,
      duration: 0.4,
      ease: 'power2.out',
      onStart: () => {
        // 状态修改为进行时
        if (showStatus.value !== 'kill') {
          showStatus.value = 'activity'
        }
        // 卡片关闭前触发
        emits('beforeCardClose')
        // 关闭遮罩
        showMask.value = false
      },
      onComplete: () => {
        // 恢复 container 默认的静态布局
        style.value = {position: 'static', width: '', height: '', top: '', left: ''}
        // 动画执行完成后，状态修改为就绪
        showStatus.value = 'ready'
        hoverStatus.value = 'complete'
        // 卡片关闭动画完成后抛出
        emits('afterCardClose')
      }
    })
  }

  // 获取展开后高度
  const getDetailHeight = () => {
    let height = props.expandedHeight
    let top = props.expandedTop
    const minWindowSpace = props.minWindowSpace
    // height + top > 视口 - 上下边距，表示此时视口内容不下容器和top值了，优先缩减top值
    if (height + top > innerHeight.value - minWindowSpace * 2) {
      // height 小于 视口边距时，缩小top值
      if (height < innerHeight.value - minWindowSpace * 2) {
        // 设置position top值
        detailTop.value = (innerHeight.value - height) / 2
      }
      // 反之top值设置为默认最小值，开始缩小卡片值
      else {
        detailTop.value = minWindowSpace
        // 设置高度为视口高度 - 上下间距
        height = innerHeight.value - minWindowSpace * 2
      }
    }
    // 防止缩小窗口状态下打开卡片，之后再放大窗口，应用的detailTop和height还沿用小窗口模式，在这里再次初始化值
    else {
      detailTop.value = props.expandedTop
      height = props.expandedHeight
    }
    return height;
  }

  // 设置展开后详情元素的高度，并添加滚动条样式
  const setExpandHeight = (height: number) => {
    // 设置展开后插槽内元素的高度
    if (detailRef.value) {
      const firstChild = detailRef.value.firstElementChild as HTMLElement
      if (firstChild) {
        firstChild.classList.add('scrollbar')
        nextTick(() => firstChild.style.setProperty('height', height + 'px', 'important'))
      }
    }
  }

  // 获取展开后的宽度
  const getDetailWidth = () => {
    let width = props.expandedWidth
    const minWindowSpace = props.minWindowSpace
    if (width > innerWidth.value - minWindowSpace * 2) {
      width = innerWidth.value - minWindowSpace * 2
    }
    return width;
  }

  // 获取展开后的top值
  const detailTop = ref<number|string>(props.expandedTop)

  // 处理展开完成
  const handleExpandComplete = () => {
    showStatus.value = 'complete'
    hoverStatus.value = 'complete'
    emits('afterCardExpand')
    setExpandHeight(expandedHeight.value)
  }

  return {
    showStatus,
    showMask,
    style,
    placeholderRef,
    containerRef,
    detailRef,
    keydownClose,
    handleClose,
    handleClickCard,
    getDetailWidth,
    getDetailHeight,
    handleExpandComplete
  }
}
const {showStatus, showMask, style, placeholderRef, containerRef, detailRef, keydownClose, handleClose, handleClickCard, getDetailWidth, handleExpandComplete} = init()


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

    if (hoverStatus.value === 'ready' || hoverStatus.value === 'complete') {
      gsap.to(containerRef.value, {
        scale: props.hoverScale,
        duration: 0.1,
        onStart: () => {
          // 鼠标悬浮时抛出方法
          emits('onMouseEnter')
          handleAddHoverStyle()
          hoverStatus.value = 'activity'
        },
        onComplete: () => {
          hoverStatus.value = 'complete'
        }
      })
    }
  }
  // 鼠标从卡片移出
  const handleMouseLeaveCard = () => {
    if (showStatus.value === 'ready') {
      gsap.to(containerRef.value, {
        scale: 1,
        duration: 0.1,
        onComplete: () => {
          hoverStatus.value = 'ready'
          handleRemoveHoverStyle()
          // 鼠标悬浮结束后抛出
          emits('onMouseLeave')
        }
      })
    }
  }
  // 添加 hover 样式
  // 缩放大于1才添加缩放样式
  const handleAddHoverStyle = () => {
    if (props.hoverScale > 1) {
      style.value.cursor = 'pointer'
      style.value.boxShadow = '0 8px 16px 0 rgba(0, 0, 0, 0.08)'
      style.value.borderRadius = '8px'
    }
  }
  // 移除 hover 样式
  const handleRemoveHoverStyle = () => {
    style.value.cursor = ''
    style.value.boxShadow = ''
    style.value.borderRadius = ''
  }
  return {
    hoverStatus,
    handleMouseOverCard,
    handleMouseLeaveCard
  }
}

const {hoverStatus, handleMouseOverCard, handleMouseLeaveCard } = initHover()

// 视口的宽度，用于定位展开后元素位置及视口宽度变化时对展开的卡片重新定位
const innerWidth = ref<number>(window.innerWidth)
// 视口的高度，视口宽高变化时对展开的卡片重新定位
const innerHeight = ref<number>(window.innerHeight)

// 监听窗口变化和键盘事件
onMounted(() => {
  window.addEventListener('resize', windowWidthResize)
  window.addEventListener("keydown", keydownClose);
})
// 卸载组件前删除监听函数
onUnmounted(() => {
  window.removeEventListener('resize', windowWidthResize)
  window.removeEventListener("keydown", keydownClose);
})

// 窗口变化后重新设置展开卡片布局
const windowWidthResize = () => {
  innerWidth.value = window.innerWidth
  innerHeight.value = window.innerHeight
  if (showStatus.value === 'complete' && containerRef.value) {
    const minWindowSpace = props.minWindowSpace
    // 隐藏Y轴滚动条
    hiddenOverflowY()
    // 处理宽度
    // 视口缩小到比展开宽度 + 两面padding 窄时，固定两边间距，缩小卡片宽度
    if (props.expandedWidth + minWindowSpace * 2 > innerWidth.value) {
      style.value.left = minWindowSpace + 'px';
      containerRef.value.style.width = innerWidth.value - minWindowSpace * 2 + 'px'
    }
    // 视口缩小到比展开宽度 + 两面padding 宽时，只修改定位的left值
    else {
      const left = window.innerWidth / 2 - getDetailWidth() / 2
      style.value.left = left + 'px'
      containerRef.value.style.width = props.expandedWidth + 'px'
    }

    // 处理高度
    if (detailRef.value) {
      const firstChild = detailRef.value.firstElementChild as HTMLElement
      if (firstChild) {
        // 插槽内子节点高度
        const firstChildHeight = firstChild.clientHeight
        // 设定的展开后高度
        const expandedHeight = props.expandedHeight

        // 元素内节点高度小于组件设定的展开高度时，元素高度跟随视口
        if (expandedHeight + props.expandedTop > innerHeight.value - minWindowSpace * 2) {
          // height 小于 视口边距时，缩小top值
          if (expandedHeight < innerHeight.value - minWindowSpace * 2) {
            // 设置position top值
            style.value.top = (innerHeight.value - firstChildHeight) / 2 + 'px'
          }
          // 反之top值设置为默认最小值，开始缩小卡片值
          else {
            style.value.top = minWindowSpace + 'px'
            // 设置高度为视口高度 - 上下间距
            firstChild.style.height = innerHeight.value - minWindowSpace * 2 + 'px'
          }
        } else {
          firstChild.style.height = expandedHeight + 'px'
          style.value.top = props.expandedTop + 'px'
        }
      }
    }
  }
}

// 监听 isComplete 变化，当 autoComplete 为 false 时，isComplete 为true 改变 showStatus 状态
watch(() => props.isComplete, (value) => {
  if (!props.autoComplete && props.isDetailVisible && showStatus.value === 'activity' && value) {
    handleExpandComplete()
  }
})
</script>

<style scoped>
.expandable-card {
  z-index: 1001
}
.expandable-card-middle {
  display: flex;
  height: 100%;
  width: 100%;
  overflow: hidden;
}
</style>
