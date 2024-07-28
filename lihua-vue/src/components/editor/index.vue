<template>
    <!--    编辑器-->
    <div id="editor"/>
</template>

<script setup lang="ts">
import Vditor from "vditor";
import 'vditor/dist/index.css';
import {onMounted, ref, watch} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
import token from "@/utils/Token.ts"
import {message} from "ant-design-vue";
// 暗色模式主题
const themeStore = useThemeStore();
const isDarkTheme = themeStore.isDarkTheme
// 编辑器
const editor = ref()
// 文件上传 url 前缀
const baseURL = import.meta.env.VITE_APP_BASE_API

// 定义prop传参
const props = defineProps<{
  modelValue?: string,
  height?: string,
  width?: string
}>()
// 定义抛出emits函数
const emits = defineEmits(['update:modelValue'])

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
  editor.value =  new Vditor('editor', {
    height: props.height ? props.height : '400px',                          // 高
    width: props.width ? props.width : '100%',                              // 宽
    mode: 'wysiwyg',                                                        // 默认模式：所见即所得
    theme: isDarkTheme ? 'dark' : 'classic',                                // 编辑器主题
    icon: 'ant',                                                            // 图标风格
    cdn:'/vditor/normal',                                                          // 自定义cdn地址，指向项目中public/vditor 下目录
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
      if (props.modelValue) {
        editor.value.setValue(props.modelValue)                             // v-model 赋值
      }
    },
    input(value) {                                                          // 输入后值后的钩子函数
      emits('update:modelValue', value)                               // 双向绑定编辑器内容
    },
    counter: {
      enable: true,                                                         // 启用计数器
      type: 'markdown'                                                      // 计数器类型
    },
    outline: {                                                              // 大纲配置
      enable: false,                                                        // 关闭大纲
      position: 'left'                                                      // 大纲位置
    },
    cache: {                                                                 // 缓存配置
      enable: false                                                          // 关闭缓存
    },
    upload: {
      url: baseURL + '/system/file/editor/uploads',                         // 文件上传接口
      headers: {'Authorization': 'Bearer ' + token.getToken()},                                 // 请求头获取token
      fieldName: 'files',                                                   // 文件上传接口参数名
      format(files, responseText) {                                         // 处理文件上传接口返回
        const resp:UploadType = JSON.parse(responseText)
        return handleUpload(resp);
      },
      error() {                                                             // 文件上传失败处理
          message.error("文件上传异常")
      },
      linkToImgUrl: baseURL + '/system/file/editor/uploadByUrl',            // url图片上传接口
      linkToImgFormat: (responseText: string) => {                          // 处理url图片上传接口返回
        const resp:LinkToImgType = JSON.parse(responseText)
        return handleLinkToImg(resp)
      },
    },
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

// 处理文件上传返回格式
const handleUpload = (resp: UploadType): string => {
  if (resp.code === 0) {
    const succMap = resp.data.succMap
    for (let key in succMap) {
      if (succMap[key]) {
        succMap[key] = baseURL + "/system/file/download/editor?filePath=" + encodeURIComponent(succMap[key])
      }
    }
  }
  return JSON.stringify(resp)
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