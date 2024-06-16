<template>
  <div>
    <a-tooltip placement="bottom" v-if="isFull" title="全屏">
      <div class="header-right-item header-right" @click="handleWindowChange">
        <a-avatar :size="32" style="background-color: rgba(0,0,0,0)">
          <FullscreenOutlined class="icon-default-color"/>
        </a-avatar>
      </div>
    </a-tooltip>
    <a-tooltip placement="bottom" v-else>
      <template #title>
        退出全屏
      </template>
      <div class="header-right-item header-right" @click="handleWindowChange">
        <a-avatar :size="32" style="background-color: rgba(0,0,0,0)">
          <FullscreenExitOutlined class="icon-default-color"/>
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