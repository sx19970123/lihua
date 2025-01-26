<template>
  <div>
    <a-upload v-if="model === 'button' || model === 'picture'"
              v-model:file-list="fileList"
              :action="uploadURL"
              :headers="{Authorization: authorization}"
              :list-type="model === 'picture' ? 'picture-card' : 'text'"
              :before-upload="beforeUpload"
              :directory="directory"
              :max-count="maxCount"
              :multiple="multiple"
              @preview="handlePreview"
    >
      <!--    按钮上传-->
      <a-button v-if="model === 'button'">
        <component :is="icon ? icon : 'upload-outlined'"></component>
        {{text ? text : '点击上传'}}
      </a-button>
      <!--    图片上传-->
      <div v-if="model === 'picture'">
        <component :is="icon ? icon : 'plus-outlined'"></component>
        <div style="margin-top: 8px">{{text ? text : '点击上传'}}</div>
      </div>
    </a-upload>
    <!--    拖拽上传-->
    <a-upload-dragger v-else
                      v-model:file-list="fileList"
                      :action="uploadURL"
                      :headers="{Authorization: authorization}"
                      :before-upload="beforeUpload"
                      :directory="directory"
                      :max-count="maxCount"
                      :multiple="multiple"
                      @preview="handlePreview"
    >
      <p class="ant-upload-drag-icon">
        <component :is="icon ? icon : 'inbox-outlined'"></component>
      </p>
      <p class="ant-upload-text">{{text ? text : '点击或拖拽上传'}}</p>
      <p class="ant-upload-hint">{{description}}</p>
    </a-upload-dragger>
<!--    图片/视频预览-->
    <a-modal :open="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel">
      <a-image style="border-radius: 8px" :preview="{maskClassName: 'attachment-upload-preview-mask'}" :src="previewURL" v-if="previewType === 'image'"/>
      <video style="width: 100%;border-radius: 8px" controls :src="previewURL" v-if="previewType === 'video'"/>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import {message, Upload, type UploadFile} from "ant-design-vue";
import {ref} from "vue";
import {download} from "@/utils/FileDownload.ts";
import {useRoute} from "vue-router";
import token from "@/utils/Token.ts";
const { getToken } = token
const imageExtensions = ["jpg", "jpeg", "png", "gif", "bmp", "svg", "webp"]
const videoExtensions = ["mp4", "avi", "mkv", "mov", "wmv", "flv", "webm"];
const uploadURL = import.meta.env.VITE_APP_BASE_API + "/system/attachment/upload/" + useRoute().name?.toString()
const authorization = 'Bearer ' + getToken()

const {model = 'dragger', uploadType = [], maxCount = 10, maxSize = 500, multiple = true, directory = false} = defineProps<{
  // 模式：按钮/图片/拖拽
  model?: 'button' | 'picture' | 'dragger',
  // 图标
  icon?: string,
  // 文本描述
  text?: string,
  // 可上传的文件类型
  uploadType?: string[],
  // 详细说明（仅支持拖拽上传）
  description?: string,
  // 最大上传数量
  maxCount?: number,
  // 最大上传大小（mb）
  maxSize?: number,
  // 是否支持多文件上传
  multiple?: boolean,
  // 是否支持文件夹上传
  directory?: boolean,
}>()

const fileList = ref<UploadFile[]>([{
  uid: '-1',
  name: 'xxx.png',
  status: 'done',
  url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
},{
  uid: '-2',
  name: '824767432559202304.mp4',
  status: 'done',
  url: 'https://www.catarc.ac.cn/member/files/824767432559202304.mp4',
}])

// 初始化文件上传
const initUpload = () => {

  // 文件上传前检验
  const beforeUpload = (file: UploadFile) => {
    // 获取文件数据异常
    if (!file || !file.name || !file.size) {
      message.error("获取文件数据异常")
      return Upload.LIST_IGNORE;
    }

    // 验证文件大小和类型
    if (!checkSize(file.size) || !checkType(file.name)) {
      return Upload.LIST_IGNORE;
    }

    return true;
  };

  // 检查文件大小
  const checkSize = (size: number): boolean => {
    const sizeMB = Math.ceil(size / 1024 / 1024)
    const flag = maxSize >= sizeMB
    if (!flag) {
      message.error("仅允许上传" + maxSize + "MB以内的文件")
    }
    return flag;
  }

  // 检查文件类型
  const checkType = (fileName: string): boolean => {
    const split = fileName.split(".")
    // 文件没有后缀
    if (split.length === 0) {
      message.error("未知的文件类型")
      return false;
    }
    // 可上传的文件类型为空不进行限制
    if (uploadType.length === 0) {
      return true;
    }
    // 判断文件后缀
    if (!uploadType.includes("." + split[split.length - 1])) {
      message.error("仅支持上传 " + uploadType.join(" ") + " 文件");
      return false;
    }
    return true;
  }

  return {
    beforeUpload
  }
}

const { beforeUpload } = initUpload()

// 初始化预览
const initPreview = () => {
  const previewType = ref<'image' | 'video' | 'other'>()
  // 预览model
  const previewVisible = ref<boolean>(false)
  // 预览title
  const previewTitle = ref<string>()
  // 预览url
  const previewURL = ref<string>()
  // 处理预览
  const handlePreview = (file: UploadFile) => {
    if (file.type || file.name) {
      // 获取文件后缀名
      const extension = file.name.split('.').pop()?.toLowerCase() || ""
      // 获取组件返回的文件类型
      const type = file.type?.split("/")[0] || ""
      // 通过组件返回类型和文件后缀联合判断文件类型
      if (type === "image" || imageExtensions.includes(extension)) {
        previewType.value = "image"
      } else if (type === "video" || videoExtensions.includes(extension)) {
        previewType.value = "video"
      } else {
        previewType.value = 'other'
      }
    } else {
      previewType.value = 'other'
    }

    // 图片视频进行弹窗预览
    if (previewType.value === 'image' || previewType.value === 'video') {
      previewVisible.value = true
      previewTitle.value = file.name
      previewURL.value = file.url
    } else {
      // 其他类型直接下载
      if (file.url) {
        download(file.url, file.name)
      }
    }
  }
  // 关闭预览
  const handleCancel = () => {
    previewVisible.value = false
  }
  return {
    previewVisible,
    previewTitle,
    previewURL,
    previewType,
    handlePreview,
    handleCancel
  }
}

const { previewVisible, previewTitle, previewURL, previewType, handlePreview, handleCancel } = initPreview()
</script>

<style>
.attachment-upload-preview-mask {
  border-radius: 8px;
}
</style>