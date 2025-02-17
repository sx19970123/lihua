<template>
    <!--    编辑器-->
  <a-spin :spinning="spinning" tip="编辑器准备中...">
    <div id="editor" :style="{height: height, width: width}"/>
  </a-spin>
</template>

<script setup lang="ts">
import Vditor from "vditor";
import 'vditor/dist/index.css';
import {onMounted, ref, watch} from "vue";
import {useThemeStore} from "@/stores/theme.ts";
import token from "@/utils/Token.ts"
import {message} from "ant-design-vue";
import type {SysAttachment} from "@/api/system/attachment/type/SysAttachment.ts";
import {useRoute} from "vue-router";
import type {ResponseType} from "@/api/global/Type.ts";
import {useUserStore} from "@/stores/user.ts";
const router = useRoute()
const userStore = useUserStore()
// 暗色模式主题
const themeStore = useThemeStore();
const isDarkTheme = themeStore.isDarkTheme
// 编辑器
const editor = ref()
// 文件上传 url 前缀
const baseURL = import.meta.env.VITE_APP_BASE_API
const origin = window.location.origin
const spinning = ref<boolean>(true)
// 定义prop传参
const {modelValue, attachmentURLPrefix = "origin", businessCode, businessName, maxSize = 10 * 1024 * 1024, width = '100%', height = '400px'} = defineProps<{
  modelValue: string | undefined,
  maxSize?: number,
  height?: string,
  width?: string,
  // 保存附件前缀
  attachmentURLPrefix?: "baseURL" | "origin"
  // 业务编码
  businessCode?: string,
  // 业务名称
  businessName?: string,
}>()
// 定义抛出emits函数
const emits = defineEmits(['update:modelValue'])
// 附件业务编码
const bCode = businessCode ?? router.name?.toString()
// 附件业务名称
const bName = businessName ?? router.meta.label as string
// 附件上传后保存前缀（/prod-api 或 http://xxx:xx/prod-api）
const httpAttachmentPrefix = attachmentURLPrefix === "baseURL" ? baseURL : origin + baseURL

// 链接转图片返回数据类型
type LinkToImgType = {
  code: number,
  data: {
    originalURL: string,
    url: string
  },
  msg: string
}

// 文件上传返回数据类型
type UploadType = {
  code: number,
  data: {
    errFiles: string[],
    succMap: {
      [key: string]: string
    }
  },
  msg: string
}

onMounted(() => {
  editor.value = new Vditor('editor', {
    height: height,                                                         // 高
    width: width,                                                           // 宽
    mode: 'wysiwyg',                                                        // 默认模式：所见即所得
    theme: isDarkTheme ? 'dark' : 'classic',                                // 编辑器主题
    icon: 'ant',                                                            // 图标风格
    cdn: '/vditor',                                                          // 自定义cdn地址，指向项目中public/vditor 下目录
    preview: {
      theme: {current: isDarkTheme ? 'dark' : 'light'},                     // 内容主题
      hljs: {style: isDarkTheme ? 'solarized-dark256' : 'solarized-light'}, // 代码块主题
    },
    fullscreen: {
      index: 1100                                                           // 全屏层级
    },
    toolbarConfig: {
      hide: false,                                                          // 隐藏工具栏
      pin: true                                                             // 固定工具栏
    },
    after() {                                                               // 编辑器初始化后的钩子函数
      spinning.value = false                                                // 关闭加载loading
      if (modelValue) {
        editor.value.setValue(modelValue)                             // v-model 赋值
      }
    },
    input(value: string) {                                                   // 输入后值后的钩子函数
      emits('update:modelValue', value)                                // 双向绑定编辑器内容
    },
    focus(value: string) {                                                    // 聚焦
      emits('update:modelValue', value)
    },
    blur(value: string) {                                                     // 失焦
      emits('update:modelValue', value)
    },
    counter: {
      enable: true,                                                         // 启用计数器
      type: 'markdown'                                                      // 计数器类型
    },
    outline: {                                                              // 大纲配置
      enable: false,                                                        // 关闭大纲
      position: 'left'                                                      // 大纲位置
    },
    cache: {                                                                // 缓存配置
      enable: false                                                         // 关闭缓存
    },
    upload: {
      url: baseURL + '/system/attachment/storage/multiple/upload',                  // 文件上传接口
      headers: {'Authorization': 'Bearer ' + token.getToken()},             // 请求头获取token
      fieldName: 'files',                                                   // 文件上传接口参数名
      max: maxSize,                                                         // 文件上传最大值
      extraData: {                                                          // 文件上传额外参数，在file()方法中进行重新赋值
        sysAttachmentJsonList: ""
      },
      file(files: File[]): File[] | Promise<File[]> {                       // 根据file构建上传参数
        const sysAttachmentList: SysAttachment[] = []
        files.filter(file => file.size <= maxSize).forEach(file => {
          sysAttachmentList.push({
            businessCode: bCode,
            businessName: bName,
            originalName: file.name,
            uploadMode: "0",
            size: file.size.toString(),
            type: file.type
          })
        })
        // 对上传参数进行赋值
        let extraData = editor.value.vditor.options.upload?.extraData
        if (extraData) {
          extraData.sysAttachmentJsonList = JSON.stringify(sysAttachmentList)
        }
        return files;
      },
      format(fileList, responseText) {                                         // 处理文件上传接口返回
        const resp: ResponseType<string[]> = JSON.parse(responseText)
        const updateData: UploadType = { code: 0, data: { errFiles: [], succMap: {} }, msg: "" }
        // 过滤file.size到新集合
        const targetFiles: File[] = []
        for (let i = 0; i < fileList.length; i++) {
          const file = fileList[i]
          if (file.size <= maxSize) {
            targetFiles.push(file)
          } else {
            if (file.name) {
              updateData.data.errFiles.push(file.name)
            }
          }
        }
        // 通过新集合构建succMap对象
        if (resp.code === 200) {
          // 构建succMap对象
          for (let i = 0; i < targetFiles.length; i++) {
            updateData.data.succMap[targetFiles[i].name] = httpAttachmentPrefix + "/system/attachment/storage/download/p/" + resp.data[i]
          }
        } else if (resp.code === 401) {
          userStore.authenticationFailure(resp.msg)
        } else {
          message.error(resp.msg)
        }
        return JSON.stringify(updateData);
      },
      error() {                                                                                                         // 文件上传失败处理
        message.error("附件上传异常")
      },
      linkToImgUrl: baseURL + `/system/attachment/storage/url/upload/${bCode}/${bName}`,                                // url图片上传接口
      linkToImgFormat: (responseText: string) => {                                                                      // 处理url图片上传接口返回
        const resp: ResponseType<{originalURL: string, id: string}> = JSON.parse(responseText)
        const linkToImg: LinkToImgType = { code: 0, data: { originalURL: "", url: "" }, msg: "" }
        if (resp.code === 200) {
          linkToImg.data.url = httpAttachmentPrefix + "/system/attachment/storage/download/p/" + resp.data.id
          linkToImg.data.originalURL = resp.data.originalURL
        } else if (resp.code === 401) {
          userStore.authenticationFailure(resp.msg)
        } else {
          linkToImg.data.url = resp.msg
          linkToImg.data.originalURL = resp.msg
        }
        return JSON.stringify(linkToImg)
      },
    },
  })
})

// 切换亮暗主题
watch(() => themeStore.isDarkTheme,(value) => {
  const editorValue = editor.value
  if (editor.value) {
    // 编译器主题
    editorValue.setTheme(
        value ? 'dark' : 'classic',
        value ? 'dark' : 'light',
        value ? 'solarized-dark256' : 'solarized-light'
    )
  }
})
</script>

<style>
.vditor-img {
  z-index: 1100;
}
</style>