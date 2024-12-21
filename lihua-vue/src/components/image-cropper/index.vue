<template>
  <div>
    <a-flex vertical :gap="8">
      <div :style="{ height: props.height,width: props.wight}" style="margin: auto;">
        <vue-cropper ref="cropperRef"
                     :img="img"
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
          <!--          文件上传-->
          <a-upload
              :showUploadList="false"
              :max-count="1"
              :customRequest="handleCustomRequest"
              :beforeUpload="handleBeforeUpload"
          >
            <a-popover content="上传图片">
              <a-button type="primary" ghost> <upload-outlined/> 上 传</a-button>
            </a-popover>
          </a-upload>
          <!--          向左旋转-->
          <a-popover content="向左旋转">
            <a-button @click="rotateLeft"><UndoOutlined /></a-button>
          </a-popover>

          <!--          向右旋转-->
          <a-popover content="向右旋转">
            <a-button @click="rotateRight"><RedoOutlined /></a-button>
          </a-popover>
          <!--          放大-->
          <a-popover content="放大">
            <a-button @click="changeScale(1)"><PlusOutlined /></a-button>
          </a-popover>
          <!--          缩小-->
          <a-popover content="缩小">
            <a-button @click="changeScale(-1)"><MinusOutlined /></a-button>
          </a-popover>
          <!--          删除-->
          <a-popover content="删除">
            <a-button @click="deleteImg" danger><DeleteOutlined /></a-button>
          </a-popover>
        </a-flex>
      </div>
    </a-flex>

  </div>

</template>
<script setup lang="ts">
import {VueCropper} from "vue-cropper";
import 'vue-cropper/dist/index.css'
import {defineProps, ref, useTemplateRef} from 'vue';
import type {CropperDataType} from "@/components/image-cropper/CropperType.ts";
import {message} from "ant-design-vue";
import type {UploadRequestOption} from "ant-design-vue/lib/vc-upload/interface";
const cropperRef = useTemplateRef<InstanceType<typeof VueCropper>>("cropperRef")

const props = defineProps({
  // v-modal:img 图片地址
  img: {
    type: String
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
    type: Number,
    default: 1
  },
  // 图片默认渲染方式 contain , cover, 100px, 100% auto
  mode: {
    type: String,
    default: 'contain'
  },
  wight: {
    type: String,
    default: '350px'
  },
  height: {
    type: String,
    default: '350px'
  },
  // v-modal:realTime
  realTime: {
    type: Object
  }
});

/**
 * 上传的图片
 */
const img = ref<string | null>(props.img as string)
/**
 * 双向绑定
 */
const emit = defineEmits(['update:realTime','update:img'])
/**
 * 供父组件获取二进制文件
 */
defineExpose({
  getBlob: () => {
    return new Promise((resolve, reject)=> {
      cropperRef.value.getCropBlob((data: Blob) => {
          resolve(data)
      })
    })
  }
})

/**
 * 变化裁剪框时回调
 * @param data
 */
const handleRealTime = (data: CropperDataType) => {
  emit('update:realTime',data)
}

/**
 * 上传前校验数据格式
 * @param file
 */
const handleBeforeUpload = (file: File) => {
  if (!file.type.startsWith('image')) {
    message.warn("请上传图片类型文件")
    return false
  }
}

/**
 * 上传成功后显示到编辑框
 * @param uploadRequest
 */
const handleCustomRequest = (uploadRequest: UploadRequestOption) => {
  if (uploadRequest) {
    const file = uploadRequest.file
    if (file instanceof Blob) {
      const url = URL.createObjectURL(file)
      img.value = url
      emit('update:img', url)
    } else {
      message.error("头像上传失败")
    }
  }

}

/**
 * 左旋转
 */
const rotateLeft = () => {
  cropperRef.value.rotateLeft();
}

/**
 * 右旋转
 */
const rotateRight = () => {
  cropperRef.value.rotateRight();
}

/**
 * 缩放
 * @param scale
 */
const changeScale = (scale: number) => {
  cropperRef.value.changeScale(scale);
}

/**
 * 删除
 */
const deleteImg = () => {
  img.value = null
  emit('update:img', null)
}
</script>
