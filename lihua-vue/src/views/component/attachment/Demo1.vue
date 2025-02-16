<template>
  <attachment-upload v-model="modelValue" chunk/>
</template>

<script setup lang="ts">
import AttachmentUpload from "@/components/attachment-upload/index.vue";
import {onMounted, ref, watch} from "vue";
const modelValue = ref<string>("1890985428726280194")


watch(() => modelValue.value, () => {
  console.log("modelValue", modelValue)
})
</script>