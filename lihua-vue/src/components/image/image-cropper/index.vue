<template>
  <div>
    <a-flex vertical :gap="8">
      <div :style="{ height: '300px',width: '500px'}">
        <vue-cropper ref="cropper"
                     :img="props.img"
                     :outputSize="props.outputSize"
                     :outputType="props.outputType"
                     :info="props.info"
                     :canScale="props.canScale"
                     :autoCrop="props.autoCrop"
                     :autoCropWidth="props.autoCropWidth"
                     :autoCropHeight="props.autoCropHeight"
                     :fixedBox="props.fixedBox"
                     :fixed="props.fixed"
                     :fixedNumber="props.fixedNumber"
                     :canMove="props.canMove"
                     :canMoveBox="props.canMoveBox"
                     :original="props.original"
                     :centerBox="props.centerBox"
                     :infoTrue="props.infoTrue"
                     :full="props.full"
                     :enlarge="props.enlarge"
                     :mode="props.mode"
                     @realTime="handleRealTime"
        >
        </vue-cropper>
      </div>
      <div>
        <a-flex :gap="8" justify="center" align="center">
          <a-button>上传</a-button>
          <a-button><RedoOutlined /></a-button>
          <a-button><UndoOutlined /></a-button>
          <a-button><PlusOutlined /></a-button>
          <a-button><MinusOutlined /></a-button>
        </a-flex>
      </div>
    </a-flex>

  </div>

</template>
<script setup lang="ts">
import {VueCropper} from "vue-cropper";
import 'vue-cropper/dist/index.css'
import {defineProps} from 'vue';
import type {CropperDataType} from "@/components/image/image-cropper/cropperTyoe";

const props = defineProps({
  // 裁剪图片的地址 url 地址, base64, blob
  img: {
    type: String,
    default: ''
  },
  // 裁剪生成图片的质量
  outputSize: {
    type: Number,
    default: 1
  },
  // 裁剪生成图片的格式 jpeg, png, webp
  outputType: {
    type: String,
    default: 'png'
  },
  // 裁剪框的大小信息
  info: {
    type: Boolean,
    default: true
  },
  // 图片是否允许滚轮缩放
  canScale: {
    type: Boolean,
    default: true
  },
  // 是否默认生成截图框
  autoCrop: {
    type: Boolean,
    default: true
  },
  // 默认生成截图框宽度
  autoCropWidth: {
    type: Number,
    default: 200
  },
  // 默认生成截图框高度
  autoCropHeight: {
    type: Number,
    default: 200
  },
  // 固定截图框大小 不允许改变
  fixedBox: {
    type: Boolean,
    default: true
  },
  // 是否开启截图框宽高固定比例
  fixed: {
    type: Boolean,
    default: true
  },
  // 截图框的宽高比例 [ 宽度 , 高度 ]
  fixedNumber: {
    type: Array,
    default: () => [1, 1]
  },
  // 上传图片是否可以移动
  canMove: {
    type: Boolean,
    default: true
  },
  // 截图框能否拖动
  canMoveBox: {
    type: Boolean,
    default: true
  },
  // 上传图片按照原始比例渲染
  original: {
    type: Boolean,
    default: false
  },
  // 截图框是否被限制在图片里面
  centerBox: {
    type: Boolean,
    default: true
  },
  // true 为展示真实输出图片宽高 false 展示看到的截图框宽高
  infoTrue: {
    type: Boolean,
    default: true
  },
  // 是否输出原图比例的截图
  full: {
    type: Boolean,
    default: true
  },
  // 图片根据截图框输出比例倍数
  enlarge: {
    type: String,
    default: '1'
  },
  // 图片默认渲染方式 contain , cover, 100px, 100% auto
  mode: {
    type: String,
    default: 'contain'
  },
  change: {
    type: Object
  }
});

const emit = defineEmits(['update:change'])

/**
 * 变化裁剪框时回调
 * @param data
 */
const handleRealTime = (data: CropperDataType) => {
  emit('update:change',data)
}
</script>
