<template>
  <div>
    <a-avatar class="modify" :size="size" :style="type !== 'image' ? {background: backgroundColor} : {}" :src="type === 'image' ? url : ''">
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
import {defineProps} from 'vue';

const { type, backgroundColor, value, size } = defineProps({
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
</script>