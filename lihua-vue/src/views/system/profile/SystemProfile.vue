<template>
 <div class="header-content-center">
     <a-card>
       <a-row>
         <a-col :lg="{span: 4}" :md="{span: 6}" :xs="{span: 8}">
           <a-menu v-model:selectedKeys="selectedKeys" @click="handleChangeUserMenu" style="height: 100%">
             <a-menu-item key="Basic"> 个人资料</a-menu-item>
             <a-menu-item key="Individuation"> 样式布局</a-menu-item>
             <a-menu-item key="Security"> 安全设置</a-menu-item>
           </a-menu>
         </a-col>
         <a-col :lg="{span: 20}" :md="{span: 18}" :xs="{span: 16}">
           <transition :name="themeStore.routeTransition" mode="out-in">
              <component :is="activeComponent" style="padding: 20px"/>
           </transition>
         </a-col>
       </a-row>
     </a-card>
 </div>
</template>

<script setup lang="ts">
import Basic from './components/ProfileBasicSetting.vue'
import Individuation from './components/ProfileIndividuation.vue'
import Security from './components/ProfileSecurity.vue'
import {ref,markRaw} from "vue";
import { useThemeStore } from "@/stores/theme";
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
    name: 'Security',
    com: markRaw(Security)
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