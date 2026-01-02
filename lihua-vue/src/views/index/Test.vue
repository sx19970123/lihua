<script setup lang="ts">
import {onMounted} from "vue";

onMounted(() => {
  console.log('插槽组件加载了')
})
</script>

<template>
<div>我是插槽</div>
</template>

<style scoped>

</style>