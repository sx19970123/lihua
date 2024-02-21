<template>
  <div>
    <a-row align="center" style="height: 64px">
      <sys-avatar class="modify"
                  :size="64"
                  :type="props.modelValue.type"
                  :value="props.modelValue.value"
                  :background-color="props.modelValue.backgroundColor"
                  :url="props.modelValue.url"
                  v-if="!open"
                  @click="open = !open"
      />
    </a-row>
<!--    :maskClosable="false" :keyboard="false" :closable="false"-->
    <a-modal v-model:open="open" title="头像编辑" width="1000px" @cancel="close">
      <a-flex vertical align="center" :gap="24">
        <!--        avatarType 不是 image 时使用avatar预览-->
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
        <!--        avatarType 是 image 时使用cropper返回的html预览-->
        <div class="avatar-preview" v-else v-html="avatarImg.html"/>
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
                       v-model="avatarUrl"
                       wight="900px"
                       height="350px"
                       :auto-crop-width="150"
                       :auto-crop-height="150" />
      </a-flex>
      <template #footer>
        <a-button type="default" key="back" @click="close">关 闭</a-button>
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
import {useUserStore} from "@/stores/modules/user";
import { Modal } from 'ant-design-vue';

const userStore = useUserStore()
// 双向绑定值
const props = defineProps(['modelValue'])
// 双向绑定修改方法
const emits = defineEmits(['update:modelValue'])
const init = () => {
  // 控制modal开关
  const open = ref<boolean>(false)
  const modelValue = props.modelValue
  // 默认头像类型
  const avatarType = ref<string>(modelValue.type)
  // 默认头像背景颜色
  const avatarColor = ref<string>(modelValue.backgroundColor)

  // 图片地址
  const avatarUrl = ref<string>(modelValue.url)
  // 图标
  const avatarIcon = ref<string>(modelValue.value)
  // 文本
  const avatarText = ref<string>(modelValue.value)

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
      color: 'rgb(191, 191, 191)',
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
    avatarIcon,
    avatarText,
    avatarUrl
  }
}
const {
  open,
  avatarType,
  avatarColor,
  avatarImg,
  avatarBackgroundColor,
  avatarIcon,
  avatarText,
  avatarUrl
} = init()

// 头像选择ref
const imageCropperRef = ref<InstanceType<typeof ImageCropper>>()

/**
 * 处理确认数据
 */
const handleOk = async () => {
  let updatedData = {};
  try {
    switch (avatarType.value) {
      case "image": {
        const cropperInstance = imageCropperRef.value;
        if (!cropperInstance) {
          throw new Error('未找到裁剪器实例');
        }
        const blob = await cropperInstance.getBlob();
        if (!blob) {
          throw new Error('获取 blob 数据失败');
        }
        avatarUrl.value = URL.createObjectURL(blob as Blob);
        const resp = await uploadAvatar(blob as Blob);
        if (resp.code !== 200) {
          throw new Error('头像上传失败');
        }
        updatedData = {
          url: avatarUrl.value,
          value: resp.data,
          type: avatarType,
          backgroundColor: avatarColor
        };
        break;
      }
      case "icon":
      case "text": {
        updatedData = {
          url: avatarUrl.value,
          value: avatarType.value === 'icon' ? avatarIcon : avatarText,
          type: avatarType,
          backgroundColor: avatarColor
        };
        break;
      }
      default:
        throw new Error('未知的头像类型');
    }
  } catch (error) {
    console.error('处理头像时出错:', error);
  }
  emits('update:modelValue', updatedData);
  open.value = false;
};

/**
 * 关闭modal提示并还原初始头像
 */
const close = () => {
  open.value = true;
  showConfirm()
};
const showConfirm = () => {
  Modal.confirm({
    title: '提 示',
    content: '关闭对话框后配置将不会应用，确认关闭？',
    cancelText: '取 消',
    okText: '关 闭',
    onOk() {
      emits('update:modelValue', userStore.avatar);
      open.value = false;
    }
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