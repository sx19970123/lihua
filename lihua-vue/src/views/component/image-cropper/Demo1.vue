<template>
  <a-typography-title :level="4">基础用法</a-typography-title>
  <a-typography-text>绑定数据：{{value}}</a-typography-text>
  <a-typography-text>图片数据：{{image}}</a-typography-text>
  <a-typography-text>图片预览：  <div v-if="value && value.html" v-html="value.html"></div></a-typography-text>

  <image-cropper v-model:real-time="value" v-model:img="image" ref="imageCropperRef"/>
</template>

<script setup lang="ts">
import ImageCropper from "@/components/image-cropper/index.vue"
import {ref, useTemplateRef} from "vue";
import type {CropperDataType} from "@/components/image-cropper/CropperType.ts";
// 图片裁剪组件的ref
const imageCropperRef = useTemplateRef<InstanceType<typeof ImageCropper>>("imageCropperRef")
const value = ref<CropperDataType>()
const image = ref<string>()

</script>
