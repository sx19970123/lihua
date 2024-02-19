<template>
  <div>
    <a-row align="center">
      <a-avatar class="modify" :size="64" :style="{background: avatarColor}" :src="avatarType === 'image' ? avatarImg.url : ''" @click="open = !open">
        <template v-if="avatarType === 'icon'" #icon>
          <component v-if="avatarIcon" :is="avatarIcon"/>
        </template>
        <template v-if="avatarType === 'text'">
          {{avatarText}}
        </template>
      </a-avatar>
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
        <a-input v-if="avatarType === 'text'" v-model:value="avatarText" style="width: 260px;"/>
        <!--        头像上传-->
        <image-cropper v-if="avatarType === 'image'" v-model:change="avatarImg" :img="userStore.avatar" :auto-crop-width="150" :auto-crop-height="150" />
      </a-flex>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import {ref} from "vue";
import ColorSelect from "@/components/color-select/index.vue"
import IconSelect from "@/components/icon-select/index.vue"
import ImageCropper from "@/components/image/image-cropper/index.vue"

import {useUserStore} from "@/stores/modules/user";
import type {CropperDataType} from "@/components/image/image-cropper/cropperTyoe";
const userStore = useUserStore()
const open = ref<boolean>(false)

const avatarType = ref<string>('image')

const avatarColor = ref<string>('#bfbfbf')
const avatarText = ref<string>(userStore.name as string)
const avatarIcon = ref<string>('')
const avatarImg = ref<CropperDataType>({
  div: {height: "", width: ""},
  h: 0,
  html: "",
  img: {height: "", transform: "", width: ""},
  url: "",
  w: 0
})

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
</script>

<style scoped>
.modify:hover:after {
  content: "编辑头像";
  font-size: 12px;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  cursor: pointer;
  border-radius: 50%;
}
.avatar-preview {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;
  overflow: hidden;
}
</style>