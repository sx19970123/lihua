<template>
  <a-upload
    :showUploadList="false"
    :max-count="1"
    :customRequest="handleCustomRequest"
  >
    <a-button> <upload-outlined/> 上 传</a-button>
  </a-upload>
</template>

<script setup lang="ts">
const emits = defineEmits(['update:modelValue'])
const handleCustomRequest = ({file}:{file: File}) => {
  emits('update:modelValue',URL.createObjectURL(file))
}
</script>

<style scoped>

</style>