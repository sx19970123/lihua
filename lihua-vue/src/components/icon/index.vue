<template>
  <component :is="props.icon"/>
</template>
<script setup lang="ts">
const props = defineProps<{
  icon: string
}>()
</script>