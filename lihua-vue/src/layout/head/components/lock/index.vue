<template>
  <div>
    <!--  锁屏按钮  -->
    <a-button type="text" @click="preLock">
      <template #icon>
        <LockOutlined class="icon-default-color"/>
      </template>
    </a-button>

    <!--  锁屏  -->
    <Teleport v-if="openLock" to="body">
      <div class="unselectable">
        <!--  锁屏页面  -->
        <div class="lock lihua-lock-mask background-glass" ref="lockMaskRef" :style="{ transform: `translateY(calc(${startLocation} + ${offsetY}px))`}">
          <div class="content">
            <!-- 日期时间 -->
            <div class="date-time">
              <a-typography-title :level="3"> {{nowDate}} {{nowWeek}}</a-typography-title>
              <a-typography-title class="time"> {{nowTime}} </a-typography-title>
            </div>

            <!-- 用户头像 ｜ 昵称 ｜ 密码框 -->
            <transition name="fade" mode="out-in">
              <a-flex vertical gap="8" class="user" align="center" v-if="status === 'locked'">
                <user-avatar
                    :value="userStore.avatar.value"
                    :type="userStore.avatar.type"
                    :url="userStore.avatar.url"
                    :background-color="userStore.avatar.backgroundColor"
                    :size="60"
                />

                <a-typography-title :level="4">
                  {{userStore.userInfo.nickname}}
                </a-typography-title>

                <a-input class="pwd" status="error" placeholder="请输入锁屏密码" type="password">
                  <template #suffix>
                    <a-button type="text" size="small" shape="circle" html-type="submit">
                      <template #icon>
                        <RightCircleOutlined class="input-prefix-icon-color"/>
                      </template>
                    </a-button>
                  </template>
                </a-input>

                <a-button type="text" size="small" @click="handleLogout">
                  <a-typography-text type="secondary">返回登录</a-typography-text>
                </a-button>
              </a-flex>
            </transition>

            <!-- 提示 -->
            <div class="tips">
              <a-typography type="secondary">
                <div v-if="status === 'reset'">
                  <DoubleRightOutlined style="transform: rotate(90deg);"/> 向下滑动锁定
                </div>
                <div v-if="status === 'close'">
                  松开鼠标，取消锁屏
                </div>
                <div v-if="status === 'lockable'">
                  松开鼠标，锁定屏幕
                </div>
              </a-typography>
            </div>
          </div>
        </div>

        <!--  锁屏下隐藏背景，点击时退出锁屏  -->
        <div class="lock-background" @click="unlock"/>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import {nextTick, onMounted, ref, useTemplateRef} from "vue";
import {hiddenOverflowY, showOverflowY} from "@/utils/Scrollbar.ts";
import UserAvatar from "@/components/user-avatar/index.vue";
import {useUserStore} from "@/stores/user.ts";
import dayjs from "dayjs";
import {useRouter} from "vue-router";

const userStore = useUserStore();
const router = useRouter()
// 启始锁屏位置
const startLocation = '-50vh'

const nowDate = ref<string>()
const nowWeek = ref<string>()
const nowTime = ref<string>()

// 鼠标按下
const mouseDown = ref(false)
// Y轴移动距离
const offsetY = ref(0)
// 是否打开锁屏页面
const openLock = ref<boolean>(false)
// 锁屏元素
const lockMaskRef = useTemplateRef<HTMLDivElement>("lockMaskRef")
// 锁屏状态，关闭锁屏、重置锁屏、可锁屏、已锁屏
const status = ref<'close' | 'reset' | 'lockable' | 'locked'>('reset')

// 开始的坐标
let startY = 0
// 偏移的坐标
let startOffset = 0

// 上次滑动的y值
let lastY = 0

// 点击进入预锁屏状态
const preLock = () => {
  openLock.value = true
  hiddenOverflowY()
  nextTick(() => {
    const element = lockMaskRef.value
    // 播放动画
    const animation = element?.animate(
        [
          { transform: 'translateY(-100vh)' },
          { transform: `translateY(${startLocation})` }
        ],
        { duration: 300, easing: 'ease', fill: 'forwards' }
    )

    animation?.finished.then(() => {
      // 鼠标按下
      element?.addEventListener("mousedown", moveStart)
      // 鼠标抬起
      window.addEventListener("mouseup", moveEnd)
      // 鼠标滑动
      window?.addEventListener("mousemove", moving)

      animation?.cancel()
    })
  })
}

// 从当前位置回到预锁屏状态
const resetPreLock = () => {
  const element = lockMaskRef.value
  // 播放关闭动画
  const animation = element?.animate(
      [
        { transform: `translateY(calc(${startLocation} + ${offsetY}px))` },
        { transform: `translateY(${startLocation})` }
      ],
      { duration: 300, easing: 'ease', fill: 'forwards' }
  )

  // 动画结束后执行清除操作
  animation?.finished.finally(() => {
    // 初始化数据
    clear()
    animation?.cancel()
  })
}

// 点击空白解除锁屏
const unlock = () => {
  const element = lockMaskRef.value
  // 播放关闭动画
  const animation = element?.animate(
      [
        { transform: `translateY(calc(${startLocation} + ${offsetY}px))` },
        { transform: 'translateY(-100vh)' }
      ],
      { duration: 300, easing: 'ease', fill: 'forwards' }
  )

  // 动画结束后执行清除操作
  animation?.finished.finally(() => {
    // 清除监听
    element?.removeEventListener("mousedown", moveStart)
    window.removeEventListener("mouseup", moveEnd)
    window?.removeEventListener("mousemove", moving)

    clear()
    showOverflowY()
    animation?.cancel()
    openLock.value = false
  })
}

// 锁屏
const lock = () => {
  const element = lockMaskRef.value
  // 播放关闭动画
  const animate = element?.animate(
      [
        { transform: `translateY(calc(${startLocation} + ${offsetY}px))` },
        { transform: 'translateY(0)' }
      ],
      { duration: 300, easing: 'ease', fill: 'forwards' }
  )

  animate?.finished.finally(() => {
    status.value = 'locked'
  })
}

// 提交解锁
const submitPassword = () => {

}

// 退出登录
const handleLogout = async () => {
  await userStore.handleLogout()
  clear()
  await router.push('/login')
}

// 清理数据
const clear = () => {
  startY = 0
  startOffset = 0
  offsetY.value = 0
  status.value = 'reset'
}

// 开始拖动
const moveStart = (e: MouseEvent) => {
  mouseDown.value = true
  startY = e.clientY
  startOffset = offsetY.value
}

// 结束拖动
const moveEnd = () => {
  mouseDown.value = false

  if (status.value === 'close') {
    unlock()
  } else if (status.value === 'reset') {
    resetPreLock()
  } else {
    lock()
  }
}

// 拖动中
const moving = (e: MouseEvent) => {
  if (!mouseDown.value || status.value === 'locked') return

  // 获取锁屏元素底部位置
  const element = lockMaskRef.value
  const bottom = element?.getBoundingClientRect().bottom

  // 触底后不能继续滑动
  // 通过阈值更新状态
  if (bottom) {
    const value = bottom / window.innerHeight
    if (value < 0.3) {
      status.value = 'close'
    } else if (value < 0.7) {
      status.value = 'reset'
    } else {
      status.value = 'lockable'
    }

    const clientY = e.clientY
    // 拉到底部并且还向下拉时，不可下拉
    if (lastY - clientY < 0 && value > 1) {
      return;
    }
    // 赋值
    const moveY = clientY - startY
    offsetY.value = startOffset + moveY
    lastY = clientY
  }
}

onMounted(() => {

  // 获取当前时间
  setInterval(() => {
    const now = dayjs()
    nowDate.value = now.format('MM月DD日')
    nowWeek.value = now.format('dddd')
    nowTime.value = now.format('HH:mm')
  }, 1000)
})
</script>

<style scoped>
.lock-background {
  height: 100vh;
  width: 100vw;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 999998;
  background: var(--lihua-alpha-level-0);
}

.lock {
  height: 100vh;
  width: 100vw;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 999999;
  background: var(--lihua-alpha-level-3);
}

.lihua-lock-mask {
  will-change: backdrop-filter;
  backdrop-filter: var(--lihua-backdrop-filter-sm);
  -webkit-backdrop-filter: var(--lihua-backdrop-filter-sm);
}

.content {
  text-align: center
}

.date-time {
  margin-top: 64px;
}

.time {
  font-size: 100px;
  margin-top: 0 !important;
}

.user {
  position: absolute;
  bottom: 64px;
  width: 100vw
}

.pwd {
  width: 200px;
  border-radius: 50px
}

.tips {
  position: absolute;
  bottom: var(--lihua-space-base);
  width: 100vw
}
</style>