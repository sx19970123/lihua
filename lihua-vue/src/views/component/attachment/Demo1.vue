<template>
  <attachment-upload v-model="modelValue" chunk/>
</template>

<script setup lang="ts">
import AttachmentUpload from "@/components/attachment-upload/index.vue";
import {onMounted, ref, watch} from "vue";
const modelValue = ref<string>("D:\\home\\lihua\\7b6c43b441e24ba5a81da8e5f3841a60.HEIC,D:\\home\\lihua\\AttachmentIndex\\2025\\01\\30\\df846293d57e4a0b92e74122616505ca.jpeg,D:\\home\\lihua\\935bcbfced56472ea076b03fe56f1ca0.mp4,D:\\home\\lihua\\981e75cab21346a39d4c920708e318a4.mp4")


watch(() => modelValue.value, () => {
  console.log("modelValue", modelValue)
})
</script>