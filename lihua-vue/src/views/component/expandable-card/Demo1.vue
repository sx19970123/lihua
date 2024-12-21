<template>
  <a-typography-title :level="4">基础用法</a-typography-title>
  <expandable-card :expanded-height="600" :expanded-width="600" :hover-scale="1.01">
    <template #overview>
      <a-card>
        <a-typography-title>这里是封面</a-typography-title>
      </a-card>
    </template>
    <template #middle>
      <a-card style="width: 100%" :bordered="false">
        <a-typography-title>这里是过渡</a-typography-title>
        <a-typography-title>这里是过渡</a-typography-title>
        <a-typography-title>这里是过渡</a-typography-title>
      </a-card>
    </template>
    <template #detail>
      <a-card>
        <a-typography-title>这里是展开</a-typography-title>
      </a-card>
    </template>
  </expandable-card>
</template>

<script setup lang="ts">
import ExpandableCard from '@/components/expandable-card/index.vue'
</script>