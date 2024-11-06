<template>
  <div>
    <a-avatar class="modify"
              :size="size"
              :style="type !== 'image' ? {background: avatarBackgroundColor} : {}"
              :src="type === 'image' ? url : ''"
    >
      <template v-if="type === 'icon'" #icon>
        <component :is="value"/>
      </template>
      <template v-if="type === 'text'">
        {{ value }}
      </template>
    </a-avatar>
  </div>
</template>

<script setup lang="ts">
import {defineProps, ref, watch} from 'vue';
import {useThemeStore} from "@/stores/theme.ts";
const themeStore = useThemeStore();
const { type, backgroundColor, value, size, url } = defineProps({
  // 头像类型
  type: {
    type: String,
    default: ''
  },
  // 背景颜色
  backgroundColor: {
    type: String,
    default: ''
  },
  // 头像值（图标/文本）
  value: {
    type: String,
    default: ''
  },
  // 头像尺寸
  size: {
    type: Number,
    default: 32
  },
  // 头像链接（图片）
  url: {
    type: String,
    default: ''
  }
});
defineEmits(['update:type', 'update:backgroundColor', 'update:value', 'update:size', 'update:url']);

// 当设置颜色为跟随系统时，获取colorPrimary设置为背景颜色
const avatarBackgroundColor = ref<string>(backgroundColor)
const setColorPrimary = () => {
  if (backgroundColor.startsWith("conic-gradient")) {
    avatarBackgroundColor.value = themeStore.getColorPrimary()
  } else {
    avatarBackgroundColor.value = backgroundColor
  }
}
setColorPrimary()

// 监听系统主题变化
watch(() => themeStore.$state.antColorPrimary, () => {
  setColorPrimary()
})
watch(() => backgroundColor, () => {
  setColorPrimary()
})
</script>
