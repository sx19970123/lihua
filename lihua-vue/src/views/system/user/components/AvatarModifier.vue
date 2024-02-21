<template>
  <div>
    <a-row align="center" >
      <sys-avatar class="modify"
                  :size="64"
                  v-model:type="avatarType"
                  v-model:value="avatarValue"
                  v-model:background-color="avatarColor"
                  v-model:url="url"
                  @click="open = !open"
      />
    </a-row>

    <a-modal v-model:open="open" title="头像编辑" width="1000px">
      <a-flex vertical align="center" :gap="24">
<!--        avatarType 不是 image 时使用avatar预览-->
        <a-avatar :size="150" :style="{background: avatarColor}" v-if="avatarType !== 'image'">
          <template v-if="avatarType === 'icon'" #icon>
            <component v-if="avatarValue" :is="avatarValue"/>
          </template>
          <template v-if="avatarType === 'text'">
            <span style="font-size: 60px">
              {{ avatarValue }}
            </span>
          </template>
        </a-avatar>
        <!--        avatarType 是 image 时使用cropper返回的html预览-->
        <div class="avatar-preview" v-else v-html="avatarImg.html"/>
        <a-radio-group v-model:value="avatarType" @change="avatarValue = ''">
          <a-radio value="image">图片</a-radio>
          <a-radio value="icon">图标</a-radio>
          <a-radio value="text">文本</a-radio>
        </a-radio-group>
        <!--        颜色选取-->
        <color-select v-model="avatarColor" v-if="avatarType !== 'image'" :items="avatarBackgroundColor"/>
        <!--        图标选取-->
        <icon-select v-if="avatarType === 'icon'" v-model="avatarValue"/>
        <!--        文本编辑-->
        <a-input v-if="avatarType === 'text'" v-model:value="avatarValue" style="width: 260px;" size="large"/>
        <!--        头像编辑-->
        <image-cropper v-if="avatarType === 'image'"
                       ref="imageCropperRef"
                       v-model:change="avatarImg"
                       :img="avatarValue"
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
import {uploadAvatar} from "@/api/system/file/file";
// 双向绑定值
const props = defineProps({
  type: {
    type: String,
    default: 'image'
  },
  backgroundColor: {
    type: String,
    default: ''
  },
  value: {
    type: String,
    default: ''
  }
})
// 双向绑定修改方法
const emits = defineEmits(['update:type','update:backgroundColor','update:value'])
const init = () => {
  // 控制modal开关
  const open = ref<boolean>(false)
  // 默认头像类型
  const avatarType = ref<string>(props.type)
  // 默认头像背景颜色
  const avatarColor = ref<string>(props.backgroundColor)
  // 默认文本头像文本
  const avatarValue = ref<string>(props.value)
  // 图片地址
  const url = ref<string>()
  // 图片预览返回结果
  const avatarImg = ref<CropperDataType>({
    div: { height: "", width: "" },
    h: 0,
    html: "",
    img: { height: "", transform: "", width: "" },
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
    avatarType,
    avatarColor,
    avatarImg,
    avatarBackgroundColor,
    avatarValue,
    url
  }
}
const {
  open,
  avatarType,
  avatarColor,
  avatarImg,
  avatarBackgroundColor,
  avatarValue,
  url
} = init()

// 头像选择ref
const imageCropperRef = ref<InstanceType<typeof ImageCropper>>()

// 处理函数
const handleOk = async () => {
  let updatedData = {};
  switch (avatarType.value) {
    case "image": {
      const cropperInstance = imageCropperRef.value;
      if (cropperInstance) {
        const blob = await cropperInstance.getBlob();
        if (blob != null) {
          url.value = URL.createObjectURL(blob)
          const resp = await uploadAvatar(blob);
          if (resp.code === 200) {
            updatedData = {
              value: resp.data,
              type: avatarType,
              backgroundColor: avatarColor
            };
          } else {
            console.error('头像上传异常');
          }
        } else {
          console.error('获取blob数据为null');
        }
      }
      break;
    }
    case "icon":
    case "text": {
      updatedData = {
        value: avatarValue,
        type: avatarType,
        backgroundColor: avatarColor
      };
      break;
    }
  }
  open.value = false;
  Object.entries(updatedData).forEach(([key, value]) => {
    emits(`update:${key}`, value);
  });
};

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