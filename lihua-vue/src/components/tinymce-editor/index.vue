<template>
  <Editor :content="content"
          :init="editorConfig"
          :key="editKey"
          v-model="content"
          licenseKey='gpl'
          tinymceScriptSrc="/tinymce/tinymce.min.js"
  />
</template>

<script setup lang="ts">
import {computed, ref, watch} from 'vue'
import Editor from '@tinymce/tinymce-vue'
import {useThemeStore} from "@/stores/theme.ts";
import {v4 as uuidv4} from "uuid";
import {useRoute} from "vue-router";
import {upload, urlUpload} from "@/api/system/attachment/AttachmentStorage.ts";
import type {SysAttachmentUrl} from "@/api/system/attachment/type/SysAttachmentUrl.ts";
import {message} from "ant-design-vue";
import {ResponseError} from "@/api/global/Type.ts";

const themeStore = useThemeStore();
const router = useRoute()
// 上传默认大小
const defaultSize = 1024 * 1024 * 2
const {attachmentURLPrefix = "origin", businessCode, businessName, imageType = [], mediaType = [], fileType = [], imageMaxSize = defaultSize, mediaMaxSize = defaultSize, fileMaxSize = defaultSize} = defineProps<{
  // 保存附件前缀
  attachmentURLPrefix?: "baseURL" | "origin",
  // 业务编码
  businessCode?: string,
  // 业务名称
  businessName?: string,
  // 图片后缀及最大尺寸
  imageType?: string[],
  imageMaxSize?: number
  // 媒体后缀及最大尺寸
  mediaType?: string[],
  mediaMaxSize?: number
  // 文件后缀及最大尺寸
  fileType?: string[],
  fileMaxSize?: number
}>()

// 附件业务编码
const bCode = businessCode ?? router.name?.toString()
// 附件业务名称
const bName = businessName ?? router.meta.label as string
// 附件上传后保存前缀（/prod-api 或 http://xxx:xx/prod-api）
const url = import.meta.env.VITE_APP_BASE_API + "/system/attachment/storage/download/p/"
const fileDownloadBaseURL = attachmentURLPrefix === "baseURL" ? url : window.location.origin + url
// 切换主题重新加载组件
const editKey = ref<string>(uuidv4())

// 附件上传回调类型
type FilePickerCallback = (url: string, meta?: { title?: string; text?: string; alt?: string }) => void
type FilePickerMeta = { filetype: 'file' | 'image' | 'media' }

// 编辑器配置
const editorConfig = computed(() => ({
  // 语言设置（需从官网下载语言包，下载完成后复制到public/tinymce/langs/下）
  language: 'zh_CN',
  // 隐藏默认logo
  branding: false,
  promotion: false,
  // 亮色｜暗色模式切换
  skin: themeStore.isDarkTheme ? 'oxide-dark' : 'oxide',
  content_css: themeStore.isDarkTheme ? 'dark' : 'default',
  // 免费插件
  plugins: 'link image media table lists code emoticons fullscreen preview searchreplace wordcount',
  // 工具栏配置
  toolbar: [
    'undo redo | bold italic underline strikethrough | forecolor backcolor |',
    'fontfamily fontsize |',
    'alignleft aligncenter alignright alignjustify |',
    'bullist numlist outdent indent |',
    'link image media emoticons |',
    'table |',
    'blockquote code |',
    'removeformat',
    'fullscreen',
    'preview',
    'searchreplace'
  ].join(' '),
  // 附件上传类型，file-链接 image-图片 media-视频
  file_picker_types: 'file image media',
  /**
   * 附件上传，拿到附件后进行处理，处理完成后调用callback
   */
  file_picker_callback: (callback: FilePickerCallback, value: string, meta: FilePickerMeta) => {
    // 创建文件上传框并点击
    const input = document.createElement('input')
    input.type = 'file'
    // 根据 meta.filetype 限制类型
    if (meta.filetype === 'image') {
      input.accept = imageType?.join(",") || 'image/*'
    } else if (meta.filetype === 'media') {
      input.accept = mediaType?.join(",") || 'audio/*,video/*'
    } else {
      input.accept = fileType?.join(",") || '*'
    }

    // 点击调用附件选择器
    input.click()

    // 附件上传
    input.onchange = async () => {
      const resp = await handleUpload(input?.files, meta.filetype)
      if (resp) {
        callback(resp.url, {text: resp.name, alt: resp.name, title: resp.name})
      }
    }
  },
  /**
   * 处理粘贴的文本
   * 过滤img标签拿到url将图片保存到服务器
   */
  paste_postprocess: async (editor: any, args: {node: HTMLElement}) => {
    // 处理提示
    const notif = editor.notificationManager.open({
      text: '正在处理粘贴内容...',
      type: 'info',
      timeout: 0
    })
    // 拿到所有img标签
    const imgs = args.node.querySelectorAll("img")
    let innerHTML = args.node.innerHTML
    let flag = false
    // 遍历标签后进行上传，替换
    for (const img of imgs) {
      const resp = await handleLinkImageUpload(img.src)
      if (resp) {
        // 对 innerHTML 进行替换
        innerHTML = innerHTML.replace(resp.originalURL, resp.url)
        flag = true
      }
    }
    // 拿到替换后的innerHTML为editor进行赋值
    if (flag) {
      editor.setContent(innerHTML)
    }
    // 关闭提示
    notif.close();
  }
}))

// 双向绑定
const content = ref<string>('')

/**
 * 处理链接图片上传
 * @param url
 */
const handleLinkImageUpload = async (url?: string): Promise<SysAttachmentUrl | false> => {
  // 业务编码｜业务名称不存在，则返回原url
  if (!bCode || !bName || !url) {
    return false
  }
  try {
    // url上传
    const resp = await urlUpload(url, bCode, bName)
    if (resp.code === 200) {
      const data = resp.data
      // 组合url
      data.url = fileDownloadBaseURL + data.id
      return data
    } else {
      message.error(resp.msg)
      return false
    }
  } catch (error) {
    if (error instanceof ResponseError) {
      message.error(error.msg)
    } else {
      console.error(error)
    }
    return false
  }
}

/**
 * 处理附件上传
 */
const handleUpload = async (files: FileList | null, type: "file" | "image" | "media") => {
  // 附件类型
  const fileTypes: string[] = []
  // 附件大小
  let maxSize: number
  if (type === 'image') {
    fileTypes.push(...imageType)
    maxSize = imageMaxSize
  } else if (type === 'media') {
    fileTypes.push(...mediaType)
    maxSize = mediaMaxSize
  } else {
    fileTypes.push(...fileType)
    maxSize = fileMaxSize
  }
  // 拿到附件
  const file = files?.item(0)

  // 附件不存在
  if (!file) {
    message.error("附件不存在")
    return false
  }

  // 附件类型不匹配
  const filter = fileTypes.filter(type => file.name.endsWith(type))
  if (filter.length === 0 && fileTypes.length > 0) {
    message.error("附件类型不匹配")
    return false
  }

  // 附件过大
  if (file.size > maxSize) {
    message.error("超过文件大小限制")
    return false
  }

  // 业务参数不存在
  if (!bCode || !bName) {
    message.error("业务参数不存在")
    return false
  }

  try {
    // 进行附件上传
    const resp = await upload(file, bCode, bName)
    // 上传成功
    if (resp.code === 200) {
      return {
        url: fileDownloadBaseURL + resp.data,
        name: file.name,
      }
    } else {
      message.error(resp.msg)
      return false
    }
  } catch (error) {
    if (error instanceof ResponseError) {
      message.error(error.msg)
    } else {
      console.error(error)
    }
    return false
  }
}

// 切换暗色模式后重新载入编辑器
watch(() => themeStore.isDarkTheme, () => {
  editKey.value = uuidv4()
})
</script>
<style>
:deep(.ant-message) {
  z-index: 999999 !important;
}
</style>