<template>
  <a-typography-title :level="4">基础用法</a-typography-title>
  <a-typography-text>选中的颜色：{{targetColor}}</a-typography-text>
  <a-typography-text>选中的值：{{targetKey}}</a-typography-text>
  <color-select :data-source="colorSource" v-model:color="targetColor" v-model:value="targetKey"/>
</template>

<script setup lang="ts">
import ColorSelect from "@/components/color-select/index.vue"
import {ref} from "vue";
const colorSource = [
  {
    name: '拂晓蓝',
    color: 'rgb(22, 119, 255)',
    key: '1'
  },
  {
    name: '薄暮',
    color: 'rgb(245, 34, 45)',
    key: '2'
  },
  {
    name: '火山',
    color: 'rgb(250, 84, 28)',
    key: '3'
  },
  {
    name: '日暮',
    color: 'rgb(250, 173, 20)',
    key: '4'
  },
  {
    name: '明青',
    color: 'rgb(19, 194, 194)',
    key: '5'
  },
  {
    name: '极光绿',
    color: 'rgb(82, 196, 26)',
    key: '6'
  },
  {
    name: '极客蓝',
    color: 'rgb(47, 84, 235)',
    key: '7'
  },
  {
    name: '酱紫',
    color: 'rgb(114, 46, 209)',
    key: '8'
  }
]

const targetColor = ref<string>()
const targetKey = ref<string>()
</script>