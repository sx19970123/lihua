<template>
  <div class="header-content-center">
    <a-row :gutter="8">
      <a-col :xxl="{span: 4}" :xl="{span: 5}" :lg="{span: 6}" :md="{span: 6}" :sm="{span: 6}" :xs="{span: 6}">
        <a-card style="height: 100%" :body-style="{padding: '22px'}">
          <a-menu v-model:selectedKeys="selectedKeys" @click="handleChangeUserMenu" style="border: 0;width: 100%" :inlineCollapsed="collapsed">
            <a-menu-item key="Individuation"> <SkinOutlined /> <span>样式布局</span></a-menu-item>
            <a-menu-item key="Basic"> <UserOutlined /> <span>个人资料</span></a-menu-item>
            <a-menu-item key="Security"> <LockOutlined /> <span>安全设置</span></a-menu-item>
          </a-menu>
        </a-card>
      </a-col>
      <a-col :xxl="{span: 20}" :xl="{span: 19}" :lg="{span: 18}" :md="{span: 18}" :sm="{span: 18}" :xs="{span: 18}">
        <transition :name="themeStore.routeTransition" mode="out-in">
          <component class="scrollbar" :is="activeComponent" style="height: 100%"/>
        </transition>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import Basic from './components/ProfileBasicSetting.vue'
import Individuation from './components/ProfileIndividuation.vue'
import Security from './components/ProfileSecurity.vue'
import {ref, markRaw, onMounted, onUnmounted} from "vue";
import { useThemeStore } from "@/stores/theme";
const collapsed = ref<boolean>(false)
const themeStore = useThemeStore()
// 注册子组件
const allComponents = ref([
  {
    name: 'Individuation',
    com: markRaw(Individuation)
  },
  {
    name: 'Basic',
    com: markRaw(Basic)
  },
  {
    name: 'Security',
    com: markRaw(Security)
  }
])
// 默认选中组件
const activeComponent = ref(markRaw(Individuation))
// 设置回显
const selectedKeys = ref(['Individuation'])
// 点击菜单切换组件
const handleChangeUserMenu = ({key}: {key: string}) => {
  const target = allComponents.value.filter(item => item.name === key)[0]
  activeComponent.value = target.com
}
// 处理屏幕宽度变化
const handleChangeInnerWidth = () => {
  const width = window.innerWidth
  collapsed.value = width <= 992;
}

onMounted(() => {
  handleChangeInnerWidth()
  window.addEventListener('resize', handleChangeInnerWidth)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleChangeInnerWidth)
})
</script>
