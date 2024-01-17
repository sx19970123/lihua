<template>
  <Logo/>
  <a-menu
      :items="menuItem"
      mode="inline"
      theme="dark"
      @select="handleClickMenu"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Logo from "@/layout/sider/components/Logo.vue";
import { getMenuInfo } from "@/api/menu"
import { useRouter } from 'vue-router'
const router = useRouter()

// 加载菜单
const initMenu = () => {
  const menuItem = ref([])
  getMenuInfo().then((resp)=> {
    menuItem.value = resp.data
  })
  return {
    menuItem
  }
}

// 处理点击菜单
const handleClickMenu = ({item}:{item:any}) => {
  if (item.routerPath) {
    router.push(item.routerPath)
  }
}


// 抛出菜单
const { menuItem } = initMenu()
</script>

<style scoped>

</style>
