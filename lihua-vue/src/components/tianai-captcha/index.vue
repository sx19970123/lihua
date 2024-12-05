<template>
  <div>
    <div id="lihua-tian-captcha" class="tian-captcha"/>
    <Mask :show-mask="showMask"/>
  </div>
</template>

<script setup lang="ts">
import Mask from '@/components/mask/index.vue'
import {ref} from "vue";

const baseURL =  import.meta.env.VITE_APP_BASE_API

const showMask = ref<boolean>(true)

type ResType = {
  code: number,
  msg: string,
  success: boolean,
  data: {
    id: string
  }
}

const captchaConfig = {
  // 请求验证码接口
  requestCaptchaDataUrl: baseURL + "/captcha/get",
  // 验证验证码接口
  validCaptchaUrl: baseURL + "/captcha/check",
  // 绑定div
  bindEl: "#lihua-tian-captcha",
  // 验证成功回调
  validSuccess: (res: ResType, c: any, tac: any) => {
    showMask.value = false
    tac.destroyWindow()
  },
  // 验证失败的回调函数(可忽略，如果不自定义 validFail 方法时，会使用默认的)
  validFail: (res: ResType, c: any, tac: any) => {
    // 验证失败后重新拉取验证码
    tac.reloadCaptcha()
  },
  // 刷新按钮回调事件
  btnRefreshFun: (el: any, tac: any) => {
    tac.reloadCaptcha()
  },
  // 关闭按钮回调事件
  btnCloseFun: (el: any, tac: any) => {
    tac.destroyWindow()
  }
}

const style = {
  // 按钮样式
  btnUrl: "https://minio.tianai.cloud/public/captcha-btn/btn3.png",
  // 背景样式
  bgUrl: "https://minio.tianai.cloud/public/captcha-btn/btn3-bg.jpg",
  // logo地址
  logoUrl: "https://minio.tianai.cloud/public/static/captcha/images/logo.png",
  // 滑块槽背景颜色
  moveTrackMaskBgColor: "#f7b645",
  // 滑块槽边框颜色
  moveTrackMaskBorderColor: "#ef9c0d"
}

// 初始化验证码
window.initTAC("tac", captchaConfig, style).then((tac: any) => {
  tac.init();
})
</script>

<style scoped>
.tian-captcha {
  position: fixed;
  top: 50%;
  left: 50%;
  z-index: 1001;
  transform: translate(-50%, -50%);
}
</style>
