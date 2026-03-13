<template>
  <div>
    <a-row :gutter="8">
      <a-col :xxl="{span: 4}" :xl="{span: 5}" :lg="{span: 6}" :md="{span: 6}" :sm="{span: 6}" :xs="{span: 6}">
        <a-card style="height: 100%" :body-style="{padding: '22px'}">
          <a-menu v-model:selectedKeys="selectedKeys" @click="handleChangeUserMenu" style="border: 0;width: 100%" :inlineCollapsed="themeStore.isSmallWindow">
            <a-menu-item key="Basic"> <UserOutlined /> <span>个人资料</span></a-menu-item>
            <a-menu-item key="Security"> <SafetyCertificateOutlined /> <span>登录密码</span></a-menu-item>
            <a-menu-item key="LockScreen"> <LockOutlined /> <span>锁屏设置</span></a-menu-item>
            <a-menu-divider/>
            <a-menu-item key="Individuation"> <SkinOutlined /> <span>样式布局</span></a-menu-item>
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
import ProfileSecurity from './components/ProfileSecurity.vue'
import ProfileLockScreen from './components/ProfileLockScreen.vue'
import {markRaw, ref} from "vue";
import {useThemeStore} from "@/stores/theme"

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
    com: markRaw(ProfileSecurity)
  },
  {
    name: 'LockScreen',
    com: markRaw(ProfileLockScreen)
  }
])
// 默认选中组件
const activeComponent = ref(markRaw(Basic))
// 设置回显
const selectedKeys = ref(['Basic'])
// 点击菜单切换组件
const handleChangeUserMenu = ({key}: {key: string}) => {
  const target = allComponents.value.filter(item => item.name === key)[0]
  activeComponent.value = target.com
}
</script>
