<template>
  <div>
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
onMounted(() => handleOpen())

// 组件销毁
onUnmounted(() =>  sessionStorage.removeItem('isRefreshed' + src.value))
</script>
<style>
.lihua-iframe {
  width: 100%;
  border-radius: 8px;
  border: none;
}

[view-tabs=show][show-hide-layout=show] .lihua-iframe {
  min-height: calc(100vh - (48px + 52px + 35px + 2px));
}
[view-tabs=hide][show-hide-layout=show] .lihua-iframe {
  min-height: calc(100vh - (48px + 35px));
}
[view-tabs=show][show-hide-layout=hide] .lihua-iframe {
  min-height: calc(100vh - (52px + 35px));
}
</style>
