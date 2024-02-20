<template>
  <div>
    <a-row align="center" >
      <sys-avatar class="modify" :size="64" :value="props.modelValue" @click="open = !open" />
    </a-row>

    <a-modal v-model:open="open" title="头像编辑" width="1000px">
      <a-flex vertical align="center" :gap="24">
        <a-avatar :size="150" :style="{background: avatarColor}" v-if="avatarType !== 'image'">
          <template v-if="avatarType === 'icon'" #icon>
            <component v-if="avatarIcon" :is="avatarIcon"/>
          </template>
          <template v-if="avatarType === 'text'">
            <span style="font-size: 60px">
              {{ avatarText }}
            </span>
          </template>
        </a-avatar>
        <div v-else v-html="avatarImg.html" class="avatar-preview"/>
        <a-radio-group v-model:value="avatarType">
          <a-radio value="image">图片</a-radio>
          <a-radio value="icon">图标</a-radio>
          <a-radio value="text">文本</a-radio>
        </a-radio-group>
        <!--        颜色选取-->
        <color-select v-model="avatarColor" v-if="avatarType !== 'image'" :items="avatarBackgroundColor"/>
        <!--        图标选取-->
        <icon-select v-if="avatarType === 'icon'" v-model="avatarIcon"/>
        <!--        文本编辑-->
        <a-input v-if="avatarType === 'text'" v-model:value="avatarText" style="width: 260px;" size="large"/>
        <!--        头像编辑-->
        <image-cropper v-if="avatarType === 'image'"
                       ref="imageCropperRef"
                       v-model:change="avatarImg"
                       :img="avatarPath"
                       wight="900px"
                       height="400px"
                       :auto-crop-width="150"
                       :auto-crop-height="150" />
      </a-flex>
      <template #footer>
        <a-button key="back" @click="open = false">关 闭</a-button>
        <a-button key="submit" type="primary" @click="handleOk">确 认</a-button>
      </template>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import {ref} from "vue";
import ColorSelect from "@/components/color-select/index.vue"
import IconSelect from "@/components/icon-select/index.vue"
import ImageCropper from "@/components/image-cropper/index.vue"
import type {CropperDataType} from "@/components/image-cropper/cropperTyoe";
import SysAvatar from "@/components/avatar/index.vue"
import {saveAvatarFile} from "@/api/system/user/user";
const props = defineProps(['modelValue'])
const init = () => {
  // 控制modal开关
  const open = ref<boolean>(false)
  const modelValue = JSON.parse(props.modelValue)

  // 默认头像类型
  const avatarType = ref<string>(modelValue.type)
  // 默认头像背景颜色
  const avatarColor = ref<string>(modelValue.color)
  // 默认文本头像文本
  const avatarText = ref<string>(modelValue.text)
  // 默认图标头像图标
  const avatarIcon = ref<string>(modelValue.icon)
  // 图片路径
  const avatarPath = ref<string>(modelValue.path)
  // 图片预览返回结果
  const avatarImg = ref<CropperDataType>({
    div: {height: "", width: ""},
    h: 0,
    html: "",
    img: {height: "", transform: "", width: ""},
    url: "",
    w: 0
  })

  // 头像背景颜色定义
  const avatarBackgroundColor = ref<Array<{name: string,color: string}>>([
    {
      name: '薄雾灰',
      color: '#bfbfbf',
    },
    {
      name: '拂晓蓝',
      color: 'rgb(22, 119, 255)',
    },
    {
      name: '薄暮',
      color: 'rgb(245, 34, 45)',
    },
    {
      name: '火山',
      color: 'rgb(250, 84, 28)',
    },
    {
      name: '日暮',
      color: 'rgb(250, 173, 20)',
    },
    {
      name: '明青',
      color: 'rgb(19, 194, 194)',
    },
    {
      name: '极光绿',
      color: 'rgb(82, 196, 26)',
    },
    {
      name: '极客蓝',
      color: 'rgb(47, 84, 235)',
    },
    {
      name: '酱紫',
      color: 'rgb(114, 46, 209)',
    },
  ])

  return {
    open,
    modelValue,
    avatarType,
    avatarColor,
    avatarIcon,
    avatarText,
    avatarImg,
    avatarBackgroundColor
  }
}
const {
  open,
  modelValue,
  avatarType,
  avatarColor,
  avatarIcon,
  avatarText,
  avatarImg,
  avatarBackgroundColor
} = init()

const emit = defineEmits(['update:modelValue'])

// 头像选择ref
const imageCropperRef = ref<InstanceType<typeof ImageCropper>>()

// 处理函数
const handleOk = () => {
  const avatarResp: {
    type: string,
    color: string,
    path: string,
    text: string,
    icon: string
  } = {
    type: '',
    color: '',
    path: '',
    text: '',
    icon: ''
  }

  avatarResp.type = avatarType.value

  switch (avatarType.value) {
    case "image": {
      const cropperInstance = imageCropperRef.value;
      if (cropperInstance) {
        // 获取blob文件
        cropperInstance.getBlob().then(blob => {
          if (blob != null) {
            // 上传到服务器
            saveAvatarFile(blob).then(resp => {
              if (resp.code === 200) {
                avatarResp.path = resp.data
                emit('update:modelValue',JSON.stringify(avatarResp))
                open.value = false
              } else {
                console.error('头像上传异常')
              }
            })
          } else {
            console.error('获取blob数据为null')
          }
        })
      }
      break
    }
    case "icon": {
      avatarResp.icon = avatarIcon.value
      avatarResp.color = avatarColor.value
      emit('update:modelValue',JSON.stringify(avatarResp))
      open.value = false
      break
    }
    case "text": {
      avatarResp.text = avatarText.value
      avatarResp.color = avatarColor.value
      emit('update:modelValue',JSON.stringify(avatarResp))
      open.value = false
      break
    }
  }

}


</script>

<style scoped>
.modify {
  position: relative;
  display: inline-block; /* 确保容器大小适应内容 */
}

.modify::after {
  content: "编辑头像";
  font-size: 12px;
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 64px;
  width: 64px;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  cursor: pointer;
  border-radius: 50%;
  transition: opacity 0.2s ease;
  opacity: 0;
}

.modify:hover::after {
  opacity: 1;
}

.avatar-preview {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;
  overflow: hidden;
}
</style>