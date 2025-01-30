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
              :isImageUrl="handleShowThumbImage"
              @preview="handlePreview"
              @change="handleChange"
              @remove="handleRemove"
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
                      @change="handleChange"
                      @remove="handleRemove"
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
import {getDownloadURL, queryAttachmentInfoByPathList} from "@/api/system/attachment/Attachment.ts";
import {ResponseError} from "@/api/global/Type.ts";
const { getToken } = token
const imageExtensions = ["jpg", "jpeg", "png", "gif", "bmp", "svg", "webp"]
const videoExtensions = ["mp4", "avi", "mkv", "mov", "wmv", "flv", "webm"];
const baseAPI = import.meta.env.VITE_APP_BASE_API
const uploadURL = baseAPI + "/system/attachment/upload/" + useRoute().name?.toString()
const authorization = 'Bearer ' + getToken()

const {model = 'picture', icon, text, uploadType = [], description, maxCount = 10, maxSize = 500, multiple = true, directory = false, modelValue} = defineProps<{
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
  // 双向绑定
  modelValue: string
}>()

const emits = defineEmits(["update:modelValue", "uploadError", "uploadSuccess", "remove"])

const fileList = ref<UploadFile[]>([])

// 初始化双向绑定
const init = async () => {
  const pathList = modelValue
      .split(",")
      .filter(item => item)

  if (pathList && pathList.length > 0) {
    try {
      // 初次加载数据时根据双向绑定内容请求附件信息
      const resp = await queryAttachmentInfoByPathList(pathList)
      if (resp.code === 200) {
        // 组合fileList
        const data = resp.data.map((item, index) => {
          const uploadFile: UploadFile = {
            uid: item.id ? item.id : '',
            name: item.originalName ? item.originalName : '',
            status: "done",
            url: pathList[index],
            thumbUrl: baseAPI + item.path
          }
          return uploadFile;
        })

        // 数据回显
        if (data && data.length > 0) {
          fileList.value = data
        }
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    }
  }
}
init()

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

  // 处理文件上传变化（uploading：上传中 done：上传成功 error：上传失败 removed：已删除）
  const handleChange = ({file, fileList}: {file: UploadFile, fileList: Array<UploadFile>}) => {
    // 文件上传失败
    if (file.status === "error") {
      emits("uploadError", file)
    }

    // 文件上传成功
    if (file.status === "done") {
      emits("uploadSuccess", {file, fileList});
    }

    // 文件删除回调
    if (file.status === "removed") {
      emits("remove", file)
    }

    // 通过fileList获取双向绑定值
    const modelValueList = fileList.filter(item => item.status === "done").map(item => {
      // 有url的直接返回
      if (item.url) {
        return item.url
      }
      // 有response数据获取对应的data。code不为200调用上传失败
      if (item.response) {
        const resp = item.response
        if (resp.code === 200) {
          const url = resp.data
          // 向fileList赋值URL
          fileList.forEach(item => {
            if (item.uid === file.uid) {
              item.url = url
            }
          })
          return url
        } else {
          emits("uploadError", file)
        }
      }
    })

    // 处理双向绑定
    emits("update:modelValue", modelValueList.join(","))
  }

  // 处理文件删除
  const handleRemove = (file: UploadFile) => {
    console.log("file",file)
  }
  return {
    beforeUpload,
    handleChange,
    handleRemove
  }
}

const { beforeUpload, handleChange, handleRemove } = initUpload()

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
  const handlePreview = async (file: UploadFile) => {
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
    // 获取url
    let url = file.thumbUrl
    if (!url?.startsWith(baseAPI)) {
      const resp = await getDownloadURL(file.url as string)
      if (resp.code === 200) {
        url = baseAPI + resp.data
        // 为fileList中thumbUrl赋值
        fileList.value.some(item => {
          if (item.url === file.url) {
            item.thumbUrl = ""
          }
        })
      }
    }
    // 图片视频进行弹窗预览
    if (previewType.value === 'image' || previewType.value === 'video') {
      previewVisible.value = true
      previewTitle.value = file.name
      previewURL.value = url
    } else {
      // 其他类型直接下载
      if (url) {
        download(url, file.name)
      }
    }
  }
  // 关闭预览
  const handleCancel = () => {
    previewVisible.value = false
  }
  // 处理显示缩略图显示
  const handleShowThumbImage = (file: UploadFile) => {
    // 获取文件后缀名
    const extension = file.name.split('.').pop()?.toLowerCase() || ""
    return imageExtensions.includes(extension);
  }
  return {
    previewVisible,
    previewTitle,
    previewURL,
    previewType,
    handlePreview,
    handleCancel,
    handleShowThumbImage
  }
}

const { previewVisible, previewTitle, previewURL, previewType, handlePreview, handleCancel, handleShowThumbImage } = initPreview()
</script>

<style>
.attachment-upload-preview-mask {
  border-radius: 8px;
}
</style>