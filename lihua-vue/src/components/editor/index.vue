<template>
  <div id="editor"/>
</template>

<script setup lang="ts">
import Vditor from "vditor";
import 'vditor/dist/index.css';
import {onMounted, ref, watch} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
import token from "@/utils/Token.ts"
const themeStore = useThemeStore();
const isDarkTheme = themeStore.isDarkTheme
const editor = ref()

onMounted(() => {
  editor.value =  new Vditor('editor', {
    height: '80vh',                                                         // 高
    width: '100%',                                                          // 宽
    theme: isDarkTheme ? 'dark' : 'classic',                                // 编辑器主题
    preview: {
      theme: {current: isDarkTheme ? 'dark' : 'light'},                     // 内容主题
      hljs: {style: isDarkTheme ? 'solarized-dark256' : 'solarized-light'}, // 代码块主题
    },
    value: 'hello world',                                                   // 默认值
    counter: {
      enable: true,                                                         // 启用计数器
      type: 'markdown'                                                      // 计数器类型
    },
    upload: {
      url: import.meta.env.VITE_APP_BASE_API + '/system/file/uploads',
      headers: {'token': token.getToken()},
      fieldName: 'files'
    }
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

<style scoped>

</style>