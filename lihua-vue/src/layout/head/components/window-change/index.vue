<template>
  <div>
    <a-tooltip placement="bottom" :title="isFull ? '进入全屏' : '退出全屏'" :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
      <div class="header-right-item header-right" @click="handleWindowChange">
        <a-avatar :size="32" style="background-color: rgba(0,0,0,0)">
          <FullscreenOutlined class="icon-default-color" v-if="isFull"/>
          <FullscreenExitOutlined class="icon-default-color" v-else/>
        </a-avatar>
      </div>
    </a-tooltip>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

const isFull = ref<boolean>(true)
const handleWindowChange = () => {
  if(document.fullscreenElement) {
    isFull.value = true
    document.exitFullscreen()
  } else {
    isFull.value = false
    document.documentElement.requestFullscreen()
  }
}
</script>

<style scoped>

</style>