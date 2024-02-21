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
import {defineProps, watch} from 'vue';

const { type, backgroundColor, value, size } = defineProps({
  type: {
    type: String,
    default: 'image'
  },
  backgroundColor: {
    type: String,
    default: ''
  },
  value: {
    type: String,
    default: ''
  },
  size: {
    type: Number,
    default: 32
  },
  url: {
    type: String
  }
});

defineEmits(['update:type', 'update:backgroundColor', 'update:value', 'update:size']);

</script>

<style scoped>
/* 可以添加样式 */
</style>
