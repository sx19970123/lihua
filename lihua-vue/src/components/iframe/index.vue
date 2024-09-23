<template>
  <iframe  class="lihua-iframe" v-if="isInner" :src="src"/>
  <div v-else>
    <div class="new-page-container">
      <p class="new-page-icon"><component style="font-size: 40px" is="PixelCat"/></p>
      <h1 class="new-page-title">页面已加载至浏览器新标签页</h1>
    </div>
  </div>
</template>
<script setup lang="ts">
import {useRoute} from "vue-router";
import {ref} from "vue";
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

// 不为内部链接时打开新标签页
if (!isInner.value && src.value) {
  window.open(src.value)
}
</script>
<style>
.lihua-iframe {
  width: 100%;
  border-radius: 8px;
  border: none;
}
[view-tabs=show][show-hide-layout=show] .lihua-iframe {
  min-height: calc(100vh - (48px + 56px + 16px + 2px));
}
[view-tabs=hide][show-hide-layout=show] .lihua-iframe {
  min-height: calc(100vh - (48px + 16px + 2px));
}
[view-tabs=show][show-hide-layout=hide] .lihua-iframe {
  min-height: calc(100vh - (56px + 16px + 2px));
}
</style>
<style scoped>
.new-page-container {
  display: flex;
  height: 400px;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.new-page-title {
  font-size: 2.5rem;
  font-weight: bold;
  color: #333;
}

.new-page-icon {
  margin-top: 1rem;
  font-size: 1.2rem;
  line-height: 1.5;
  color: #666;
}
</style>
