<template>
  <a-typography-title :level="4">异步展开</a-typography-title>
    <expandable-card :expanded-height="600"
                     :expanded-width="600"
                     :hover-scale="1.01"
                     :auto-complete="false"
                     :is-complete="loadSuccess"
                     @before-card-expand="handleCardClick"
                     @after-card-close="handleCardReady"
                     style="width: 30%"
    >
      <template #overview>
        <a-card>
          <a-typography-title>
            这里是封面
          </a-typography-title>
        </a-card>
      </template>
      <template #middle>
        <a-card style="width: 100%;">
          过渡状态持续1.5s后展示展开卡片
        </a-card>
      </template>
      <template #detail>
        <a-card>
          <a-typography-title>
            这里是展开
          </a-typography-title>
        </a-card>
      </template>
    </expandable-card>
</template>

<script setup lang="ts">
import ExpandableCard from '@/components/expandable-card/index.vue'
import {ref} from "vue";
// 控制卡片是否切换到展开状态
const loadSuccess = ref<boolean>(false)
// 处理点击卡片模拟异步请求
const handleCardClick = () => {
  setTimeout(() => {
    loadSuccess.value = true
  }, 1500)
}
// 关闭卡片
const handleCardReady = () => {
  loadSuccess.value = false
}
</script>
