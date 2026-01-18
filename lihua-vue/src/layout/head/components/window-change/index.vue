<template>
  <div v-if="!themeStore.isSmallWindow">
    <a-button type="text" @click="toggleFullscreen">
      <template #icon>
        <a-tooltip
            placement="bottom"
            :title="isFullscreen ? '退出全屏' : '进入全屏'"
        >
          <FullscreenExitOutlined class="icon-default-color" v-if="isFullscreen"/>
          <FullscreenOutlined class="icon-default-color" v-else/>
        </a-tooltip>
      </template>
    </a-button>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from "vue";
import { useThemeStore } from "@/stores/theme.ts";

const themeStore = useThemeStore();

// 是否全屏
const isFullscreen = ref<boolean>(!!document.fullscreenElement);

// 全屏状态更新
const updateFullscreenState = () => {
  isFullscreen.value = !!document.fullscreenElement;
};

// 切换全屏
const toggleFullscreen = async () => {
  try {
    if (!document.fullscreenElement) {
      await document.documentElement.requestFullscreen();
    } else {
      if (document.exitFullscreen) {
        await document.exitFullscreen();
      }
    }
  } catch (err) {
    console.error(`全屏切换失败: ${err}`);
  }
};

onMounted(() => {
  // 监听全屏切换
  document.addEventListener('fullscreenchange', updateFullscreenState);
});

onUnmounted(() => {
  document.removeEventListener('fullscreenchange', updateFullscreenState);
});
</script>