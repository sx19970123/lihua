<template>
  <div>
    <div id="lihua-tian-captcha" class="tian-captcha"/>
    <Mask :show-mask="showMask"/>
  </div>
</template>

<script setup lang="ts">
import Mask from '@/components/mask/index.vue'
import { useThemeStore } from '@/stores/theme';
import { ref } from "vue";

// 请求url前缀
const baseURL = import.meta.env.VITE_APP_BASE_API
// 当前主题
const themeStore = useThemeStore()
// 是否打开遮罩
const showMask = ref<boolean>(false)

// 验证成功/失败返回对象类型
type ResType = {
  code: number,
  msg: string,
  success: boolean,
  data: {
    id: string
  }
}

// 抛出的方法：校验成功/校验失败/刷新/关闭
const emits = defineEmits(['success', 'fail', 'refresh', 'close'])

// 向外部抛出函数
defineExpose({
  // 显示验证码
  show: () => {
    load()
  },
  // 关闭验证码
  close: () => {
    destroy()
  }
})

// 验证码配置
const captchaConfig = {
  // 请求验证码接口
  requestCaptchaDataUrl: baseURL + "/captcha/get",
  // 验证验证码接口
  validCaptchaUrl: baseURL + "/captcha/check",
  // 绑定div
  bindEl: "#lihua-tian-captcha",
  // 验证成功回调
  validSuccess: (res: ResType, c: any, tac: any) => {
    emits('success', res.data.id)
    showMask.value = false
    tac.destroyWindow()
  },
  // 验证失败的回调函数(可忽略，如果不自定义 validFail 方法时，会使用默认的)
  validFail: (res: ResType, c: any, tac: any) => {
    emits('fail', res)
    // 验证失败后重新拉取验证码
    tac.reloadCaptcha()
  },
  // 刷新按钮回调事件
  btnRefreshFun: (el: any, tac: any) => {
    emits('refresh')
    tac.reloadCaptcha()
  },
  // 关闭按钮回调事件
  btnCloseFun: (el: any, tac: any) => {
    emits('close')
    showMask.value = false
    tac.destroyWindow()
  }
}

// 亮色模式主题
const lightStyle = {
  // 按钮样式
  btnUrl:  "tac/images/btn.png",
  // 背景样式
  bgUrl: null,
  // logo地址
  logoUrl: null,
  // 滑块槽背景颜色
  moveTrackMaskBgColor: "#f7b645",
  // 滑块槽边框颜色
  moveTrackMaskBorderColor: "#ef9c0d"
}
// 暗色模式主题
const darkStyle = {
  // 按钮样式
  btnUrl: "tac/images/btn.png",
  // 背景样式
  bgUrl: "tac/images/dark.png",
  // logo地址
  logoUrl: null,
  // 滑块槽背景颜色
  moveTrackMaskBgColor: "#f7b645",
  // 滑块槽边框颜色
  moveTrackMaskBorderColor: "#ef9c0d"
}

// 加载验证码
const load = () => {
  window.initTAC("tac", captchaConfig, themeStore.isDarkTheme ? darkStyle : lightStyle).then((tac: any) => {
    // 初始化验证码
    tac.init();
    // 设置切换验证码loading
    replaceLoadingElement()
    // 打开遮罩
    showMask.value = true
  }).catch(err => {
    console.error("验证码加载失败", err)
  })
}
// 销毁验证码
const destroy = () => {
  window.initTAC("tac", captchaConfig, themeStore.isDarkTheme ? darkStyle : lightStyle).then((tac: any) => {
    tac.destroyWindow();
    showMask.value = false
  }).catch(err => {
    console.error("验证码销毁失败", err)
  })
}

// 将tianai-captcha-loading的img标签删除替换为div标签，css中定义样式
const replaceLoadingElement = () => {
  const imgElement = document.getElementById("tianai-captcha-loading")
  if (imgElement) {
    const divElement = document.createElement("div");
    divElement.id = imgElement.id;
    if (imgElement.parentNode) {
      imgElement.parentNode.replaceChild(divElement, imgElement);
    }
  }
}
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

<style lang="less">
/* 验证码外部容器样式 */
#tianai-captcha-parent {
  box-shadow: var(--lihua-light-box-shadow) !important;
  border-radius: 8px !important;
  width: 332px !important;
  height: 326px !important;
  padding: 16px !important;
}
/* 背景图片样式 */
#tianai-captcha-parent #tianai-captcha-bg-img {
  border-radius: 8px !important;
}
/* 滑动提示文字样式 */
#tianai-captcha-slider-move-track-font {
  color: rgba(0, 0, 0, 0.88) !important;
  font-size: 16px !important;
  font-weight: 500 !important;
}
/* 点击提示文字样式 */
#tianai-captcha.tianai-captcha-word-click .click-tip #tianai-captcha-click-track-font {
  color: rgba(0, 0, 0, 0.88) !important;
  font-size: 20px !important;
  font-weight: 500 !important;
}

/* 滑块滑过样式 */
#tianai-captcha-parent #tianai-captcha-slider-move-track-mask {
  height: 34px !important;
  border-radius: 4px 0 0 4px !important;
}
/* 底部操作栏样式 */
#tianai-captcha-parent .slider-bottom {
  padding-top: 8px !important;
}
/* 验证成功样式 */
#tianai-captcha .content .tianai-captcha-tips.tianai-captcha-tips-success {
  background-color: #52c41a !important;
}

/* 点选样式 */
.click-confirm-btn {
  margin-top: 5px !important;
}

/* 验证码切换过渡动画 */
#tianai-captcha-loading {
  width: 120px;
  height: 30px;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  -webkit-mask: linear-gradient(90deg, #000 70%, rgba(0, 0, 0, 0) 0) 0 / 20%;
  background: linear-gradient(#f7b645 0%, #f7b645 0%) 0 0 / 0 no-repeat rgba(221, 221, 221, .42);
  animation: cartoon 1s infinite steps(6);
}
/* 定义过渡动画 */
@keyframes cartoon {
  60% {
    background-size: 120%;
  }
}
/* 验证失败样式 */
#tianai-captcha .content .tianai-captcha-tips.tianai-captcha-tips-error {
  background-color: #ff4d4f !important;
}
/* 拼接类型验证统一圆角 */
#tianai-captcha.tianai-captcha-concat .tianai-captcha-slider-concat-bg-img {
  border-radius: 5px;
}
#tianai-captcha.tianai-captcha-concat .tianai-captcha-slider-concat-img-div {
  border-radius: 5px 5px 0 0;
}
[data-theme = 'dark'] {
  /* 验证码外部容器样式 */
  #tianai-captcha-parent {
    box-shadow: var(--lihua-dark-box-shadow) !important;
  }
  /* 滑动提示文字样式 */
  #tianai-captcha-slider-move-track-font {
    color: rgba(255, 255, 255, 0.85) !important;
  }
  /* 点击提示文字样式 */
  #tianai-captcha.tianai-captcha-word-click .click-tip #tianai-captcha-click-track-font {
    color: rgba(255, 255, 255, 0.85) !important;
  }
  /* 验证成功样式 */
  #tianai-captcha .content .tianai-captcha-tips.tianai-captcha-tips-success {
    background-color: #306317 !important;
  }
  /* 验证失败样式 */
  #tianai-captcha .content .tianai-captcha-tips.tianai-captcha-tips-error {
    background-color: #7e2e2f !important;
  }
  /* 滑块轨道样式*/
  #tianai-captcha.tianai-captcha-slider .slider-move-track{
    background: #333333 !important;
    border: 1px solid #333333 !important;
  }
  /* 滑块轨道光标样式*/
  #tianai-captcha-parent .slider-move-shadow {
    background: rgba(104, 104, 104, 0.1) !important;
    box-shadow: 1px 1px 1px rgba(104, 104, 104, 0.1) !important;
  }
  /* 点选图标 */
  #tianai-captcha.tianai-captcha-word-click .content #bg-img-click-mask .click-span {
    background-color: #15417e !important;
    border: 2px solid #15325b !important;
    color: rgba(255, 255, 255, 0.65) !important;
  }
  /* 验证后文本提示样式 */
  #tianai-captcha .content .tianai-captcha-tips {
    color: rgba(255, 255, 255, 0.65) !important;
  }
  /* 验证码切换过渡动画 */
  #tianai-captcha-loading {
    background: linear-gradient(#b58d2a 0%, #b58d2a 0%) 0 0 / 0 no-repeat rgba(221, 221, 221, .42);
  }
  /* 滑块区域文本提示 */
  .slider-move-track::after {
    color: rgba(255, 255, 255, 0.45);
  }
}
/* 滑块区域文本提示 */
.slider-move-track::after {
  content: "向右滑动完成验证";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
}
</style>
