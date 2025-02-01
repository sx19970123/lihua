<template>
  <attachment-upload v-model="modelValue"/>
</template>

<script setup lang="ts">
import AttachmentUpload from "@/components/attachment-upload/index.vue";
import {onMounted, ref, watch} from "vue";
const modelValue = ref<string>("D:\\home\\lihua\\AttachmentIndex\\2025\\01\\30\\df846293d57e4a0b92e74122616505ca.jpeg,D:\\home\\lihua\\AttachmentIndex\\2025\\02\\01\\93dbd4d40f104376839bdf8737e7d619.png")


watch(() => modelValue.value, () => {
  console.log("modelValue", modelValue)
})
</script>