import {createI18n} from "vue-i18n";
import cn from "./cn"
import en from "./en"

// 初始化 i18n
let i18n: any = createI18n({
  legacy: false,
  globalInjection: true,
  locale: localStorage.getItem('language') || 'cn',
  messages: {
    cn,en
  }
})

// 切换语言
export function changeLanguage(language: string) {
  localStorage.setItem("language",language)
  i18n.locale = language
  location.reload()
}

// ts中调用翻译
export function t(code: string) {
  return i18n.global.t(code)
}

export default i18n
