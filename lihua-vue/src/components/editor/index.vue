<template>
  <div id="editor"/>
</template>

<script setup lang="ts">
import Vditor from "vditor";
import 'vditor/dist/index.css';
import {onMounted, ref, watch} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
import token from "@/utils/Token.ts"
import {download} from "@/utils/FileDownload.ts";
const themeStore = useThemeStore();
const isDarkTheme = themeStore.isDarkTheme
const editor = ref()
const baseURL = import.meta.env.VITE_APP_BASE_API

type LinkToImgType = {
  code: number,
  data: {
    originalURL: string,
    url: string
  },
  msg: string
}

onMounted(() => {
  editor.value =  new Vditor('editor', {
    height: '80vh',                                                         // 高
    width: '100%',                                                          // 宽
    mode: 'wysiwyg',                                                        // 默认模式：所见即所得
    theme: isDarkTheme ? 'dark' : 'classic',                                // 编辑器主题
    icon: 'ant',                                                            // 图标风格
    preview: {
      theme: {current: isDarkTheme ? 'dark' : 'light'},                     // 内容主题
      hljs: {style: isDarkTheme ? 'solarized-dark256' : 'solarized-light'}, // 代码块主题
    },
    toolbarConfig: {
      hide: false,                                                          // 隐藏工具栏
      pin: true                                                             // 固定工具栏
    },
    value: 'hello world',                                                   // 默认值
    counter: {
      enable: true,                                                         // 启用计数器
      type: 'markdown'                                                      // 计数器类型
    },
    link: {
      click(bom: Element) {
        handleClickLink(bom)
      },
    },
    upload: {
      url: baseURL + '/system/file/editor/uploads',                         // 文件上传接口
      headers: {'token': token.getToken()},                                 // 请求头获取token
      fieldName: 'files',                                                   // 文件上传接口参数名
      format(files, responseText) {
        return responseText;
      },
      linkToImgUrl: baseURL + '/system/file/editor/uploadByUrl',            // url图片上传接口
      linkToImgFormat: (responseText: string) => {                          // 处理url图片上传接口返回
        const resp:LinkToImgType = JSON.parse(responseText)
        // 处理链接转换图片
        return handleLinkToImg(resp)
      },
    }
  })
})


// 处理链接转换为图片
const handleLinkToImg = (resp: LinkToImgType): string => {
  if (resp.code === 0) {
    resp.data.url =  baseURL + "/system/file/download/editor?filePath=" + resp.data.url
  } else {
    resp.code = 0
    console.error("图片上传至服务器失败，由原链接显示，错误信息：" + resp.msg)
  }
  return JSON.stringify(resp)
}

const handleClickLink = (linkElement: Element) => {
  if (linkElement instanceof HTMLAnchorElement) {
    const filePath = linkElement.attributes.getNamedItem("href")?.value
    if (filePath) {
      download(baseURL + "/system/file/download/editor?filePath=" + encodeURIComponent(filePath))
    }
  }
}

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

<style scoped>

</style>