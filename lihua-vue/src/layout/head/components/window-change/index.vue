<template>
  <div>
      <div class="header-right-item">
        <a-button type="text" @click="handleWindowChange">
          <template #icon>
            <a-tooltip placement="bottom" :title="isFull ? '进入全屏' : '退出全屏'" :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
              <FullscreenOutlined class="icon-default-color" v-if="isFull"/>
              <FullscreenExitOutlined class="icon-default-color" v-else/>
            </a-tooltip>
          </template>
        </a-button>
      </div>
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