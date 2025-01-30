<template>
  <attachment-upload v-model="modelValue"/>
</template>

<script setup lang="ts">
import AttachmentUpload from "@/components/attachment-upload/index.vue";
import {onMounted, ref, watch} from "vue";
const modelValue = ref<string>("D:\\home\\lihua\\AttachmentIndex\\2025\\01\\30\\df846293d57e4a0b92e74122616505ca.jpeg,D:\\home\\lihua\\AttachmentIndex\\2025\\01\\30\\7233a2c4329f47599aea34eae4600794.jpeg,D:\\home\\lihua\\AttachmentIndex\\2025\\01\\30\\f8833814bd9b4cd88b50be6639b7cbd4.png,D:\\home\\lihua\\AttachmentIndex\\2025\\01\\30\\37c5aef1e84740e8887cfc0f54e8aa01.md")


watch(() => modelValue.value, () => {
  console.log("modelValue", modelValue)
})
</script>