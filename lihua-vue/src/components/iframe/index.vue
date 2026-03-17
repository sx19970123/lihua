<template>
  <iframe class="lihua-iframe" v-if="isInner" :src="src"/>
  <div v-else>
    <a-card class="lihua-iframe">
      <a-flex :gap="16" justify="center" vertical align="center" style="margin-top: 48px">
        <component is="XiaoMiaoHappy" style="font-size: 96px"/>
        <a-typography-title style="margin: 0">页面已加载至浏览器新标签页</a-typography-title>
        <a-typography-link @click="open">再次打开</a-typography-link>
      </a-flex>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import {useRoute} from "vue-router";
import {onMounted, onUnmounted, ref} from "vue";

const route = useRoute()
const props = defineProps<{
  src?: string,
  isInner?: boolean
}>()

const src = ref<string>()
const isInner = ref<boolean>()

if (props.src) {
  src.value = props.src
} else {
  src.value = route.meta.link as string
}

if (props.isInner) {
  isInner.value = props.isInner
} else {
  isInner.value = route.meta.linkOpenType === 'inner'
}

const handleOpen = () => {
  // 只有第一次进入页面时会打开连接
  if (!sessionStorage.getItem('isRefreshed' + src.value)) {
    // 不为内部链接时打开新标签页
    if (!isInner.value && src.value) {
      open()
    }
    sessionStorage.setItem('isRefreshed' + src.value, 'true');
  }
}

// 打开页面
const open = () => {
  window.open(src.value)
}

// 组件加载完成
onMounted(() => {
  handleOpen()
})

// 组件销毁
onUnmounted(() =>  sessionStorage.removeItem('isRefreshed' + src.value))
</script>
<style>
.lihua-iframe {
  width: 100%;
  border-radius: var(--lihua-radius-sm);
  border: none;
}

/* 根据layout和view-tabs是否显示进行高度计算 */
/*
 var(--lihua-layout-height)：顶部导航｜head高度
 var(--lihua-space-base)：上下外边距高度
 54px：view-tabs高度
 3px：微调偏移量
 */

:root {
  --tab-height: 54px;
  --border-width: 3px;
}

[footer='show'] {
  --footer-display-height: var(--footer-height);
}
[footer='hide'] {
  --footer-display-height: 0px;
}

[view-tabs='show'] {
  --tab-display-height: var(--tab-height);
}
[view-tabs='hide'] {
  --tab-display-height: 0px;
}

[layout='show'] {
  --layout-display-height: var(--lihua-layout-height);
}
[layout='hide'] {
  --layout-display-height: 0px;
}

.lihua-iframe {
  height: calc(100vh - var(--layout-display-height) - var(--tab-display-height) - var(--lihua-space-base) - var(--lihua-space-base) - var(--border-width) - var(--footer-display-height));
}
</style>
