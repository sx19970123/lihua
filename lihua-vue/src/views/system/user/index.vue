<template>
 <div>
     <a-card>
       <a-row>
         <a-col :span="4">
           <a-menu v-model:selectedKeys="selectedKeys" @click="handleChangeUserMenu" style="height: 100%">
             <a-menu-item key="Individuation"> 样式布局</a-menu-item>
             <a-menu-item key="Info"> 个人信息</a-menu-item>
             <a-menu-item key="Basic"> 基础设置</a-menu-item>
             <a-menu-item key="Security"> 安全设置</a-menu-item>
           </a-menu>
         </a-col>
         <a-col :span="20">
           <transition :name="themeStore.routeTransition" mode="out-in">
              <component :is="activeComponent" style="margin: 20px"/>
           </transition>
         </a-col>
       </a-row>
     </a-card>
 </div>
</template>

<script setup lang="ts">
import Basic from './components/UserBasic.vue'
import Individuation from './components/UserIndividuation.vue'
import Info from './components/UserInfo.vue'
import Security from './components/UserSecurity.vue'
import {ref,markRaw} from "vue";
import { useThemeStore } from "@/stores/modules/theme";
const themeStore = useThemeStore()
// 注册子组件
const allComponents = ref([
  {
    name: 'Basic',
    com: markRaw(Basic)
  },
  {
    name: 'Individuation',
    com: markRaw(Individuation)
  },
  {
    name: 'Info',
    com: markRaw(Info)
  },
  {
    name: 'Security',
    com: markRaw(Security)
  },
])
// 默认选中组件
const activeComponent = ref(markRaw(Individuation))
// 设置回显
const selectedKeys = ref(['Individuation'])
// 点击菜单切换组件
const handleChangeUserMenu = ({key}: {key: string}) => {
  allComponents.value.forEach(item => {
    if (item.name === key) {
      activeComponent.value = markRaw(item.com)
    }
  })
}
</script>

<style scoped>

</style>