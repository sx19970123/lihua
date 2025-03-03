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
    <a-modal v-model:open="open" title="头像编辑" width="1000px" @cancel="close">
      <a-flex vertical align="center" :gap="24">
        <!--        avatarType 不是 image 时使用avatar预览-->
        <a-avatar :size="150"
                  :style="{background: avatarColor === avatarBackgroundColor[0].color ? themeStore.getColorPrimary(): avatarColor}"
                  v-if="avatarType !== 'image'">
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
        <color-select v-model:color="avatarColor"
                      v-if="avatarType !== 'image'"
                      :dataSource="avatarBackgroundColor"
        />
        <!--        图标选取-->
        <icon-select v-if="avatarType === 'icon'" v-model="avatarIcon" :size="iconSize"/>
        <!--        文本编辑-->
        <a-input v-if="avatarType === 'text'" v-model:value="avatarText" style="max-width: 260px;" size="large" placeholder="请输入头像文本"/>
        <!--        头像编辑-->
        <image-cropper v-if="avatarType === 'image'"
                       ref="imageCropperRef"
                       v-model:realTime="avatarImg"
                       v-model:img="avatarUrl"
                       :wight="imageCropperWight + 'px'"
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
import {onMounted, onUnmounted, ref, useTemplateRef} from "vue";
import ColorSelect from "@/components/color-select/index.vue"
import IconSelect from "@/components/icon-select/index.vue"
import ImageCropper from "@/components/image-cropper/index.vue"
import type {CropperDataType} from "@/components/image-cropper/CropperType.ts";
import SysAvatar from "@/components/user-avatar/index.vue"
import {useUserStore} from "@/stores/user";
import {message, Modal} from 'ant-design-vue';
import settings from "@/settings";
import type {AvatarType} from "@/api/system/profile/type/SysProfile.ts";
import { cloneDeep } from 'lodash-es'
import {useThemeStore} from "@/stores/theme.ts";
import {ResponseError} from "@/api/global/Type.ts";
import {upload} from "@/api/system/attachment/AttachmentStorage.ts";
import {v4 as uuidv4} from "uuid";

const themeStore = useThemeStore()
const userStore = useUserStore()
// 双向绑定值
const props = defineProps(['modelValue'])
// 双向绑定修改方法
const emits = defineEmits(['update:modelValue','change'])
const init = () => {
  // 控制modal开关
  const open = ref<boolean>(false)
  const modelValue = props.modelValue
  // 默认头像类型
  const avatarType = ref<string>(modelValue.type)
  // 默认头像背景颜色
  const avatarColor = ref<string>(modelValue.backgroundColor)
  // 图标选择尺寸
  const iconSize = ref<'small' | 'large' | 'default'>('large')
  // 图片裁剪宽度
  const imageCropperWight = ref<number>(0)

  // 图片地址
  const avatarUrl = ref<string>(modelValue.url)
  // 图标
  const avatarIcon = ref<string>(modelValue.type === 'icon' ? modelValue.value : '')
  // 文本
  const avatarText = ref<string>(modelValue.type === 'text' ? modelValue.value : '')

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
  const avatarBackgroundColor = ref<Array<{name: string,color: string}>>(cloneDeep(settings.colorOptions))

  // 颜色集合第一个添加为跟随系统颜色
  avatarBackgroundColor.value.unshift({
    name: '跟随系统',
    color: 'conic-gradient(from 45deg, ' + settings.colorOptions.map(item => item.color).join(",") + ')'
  })

  // 处理窗口宽度 1050 / 680 / 492 划分图标选择器尺寸
  const handleWindowWith = () => {
    const width = window.innerWidth

    if (width > 1050) {
      iconSize.value = 'large'
    } else if (width < 1050 && width > 680) {
      iconSize.value = 'default'
    } else {
      iconSize.value = 'small'
    }

    // 视口宽度 - dialog 外边距 - dialog 内边距
    if (width <= 1050) {
      imageCropperWight.value = width - 48 - 32
    } else {
      imageCropperWight.value = 954
    }

  }

  handleWindowWith()

  return {
    open,
    avatarType,
    avatarColor,
    avatarImg,
    avatarBackgroundColor,
    avatarIcon,
    avatarText,
    avatarUrl,
    iconSize,
    imageCropperWight,
    handleWindowWith
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
  avatarUrl,
  iconSize,
  imageCropperWight,
  handleWindowWith
} = init()

// 头像选择ref
const imageCropperRef = useTemplateRef<InstanceType<typeof ImageCropper>>("imageCropperRef")

/**
 * 处理确认数据
 */
const handleOk = async () => {
  let updatedData: AvatarType = {
    type :'',
    backgroundColor :'',
    value :''
  };
  try {
    switch (avatarType.value) {
      case "image": {
        const cropperInstance = imageCropperRef.value;
        if (!cropperInstance) {
          throw new Error('未找到裁剪器实例');
        }
        if (!avatarUrl.value) {
          throw new Error('请上传头像');
        }
        const blob = await cropperInstance.getBlob() as Blob;

        if (!blob) {
          throw new Error('获取 blob 数据失败');
        }

        const resp = await upload(new File([blob],uuidv4() + ".png", { type: "image/png" }), "UserAvatar", "用户头像");
        if (resp.code !== 200) {
          throw new Error('头像上传失败');
        }
        updatedData = {
          url: URL.createObjectURL(blob),
          value: resp.data,
          type: avatarType.value,
          backgroundColor: avatarColor.value
        };
        break;
      }
      case "icon":
      case "text": {
        updatedData = {
          value: avatarType.value === 'icon' ? avatarIcon.value : avatarText.value,
          type: avatarType.value,
          backgroundColor: avatarColor.value
        };
        break;
      }
      default:
        throw new Error('未知的头像类型');
    }

    if (updatedData.value) {
      // 双向绑定
      emits('update:modelValue', updatedData);
      // 删除临时url
      const cloneData = cloneDeep(updatedData);
      delete cloneData.url;
      // 触发change事件
      emits('change', JSON.stringify(cloneData));
      open.value = false;
    } else {
      if (avatarType.value === 'image') {
        message.warn("请上传头像")
      } else if (avatarType.value === 'text') {
        message.warn("请编辑文本")
      } else if (avatarType.value === 'icon') {
        message.warn("请选择图标")
      } else {
        message.warn("请将头像编辑完整")
      }

    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      message.error("处理头像异常-" + e)
      console.error('处理头像时出错:', e);
    }
  }
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

// 组件创建完成后获取抽屉展开宽度
onMounted(() => {
  window.addEventListener('resize', handleWindowWith)
})
// 组件销毁后删除监听
onUnmounted(() => {
  window.removeEventListener('resize', handleWindowWith)
})

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
