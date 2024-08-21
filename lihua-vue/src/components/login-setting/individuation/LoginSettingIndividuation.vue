<template>
  <login-setting-base-component title="个性化"
                                description="您可以自定义系统主题颜色、导航类型、布局大小等个性化配置"
                                icon="SkinOutlined"
                                skip-msg="可在个人中心 - 样式布局进行设置"
                                @next="handleNext"
                                @skip="handleSkip"
                                @back="emits('back')"
  >
    <template #content>
      <a-form>
        <a-form-item>
            <color-select :dataSource="colorList"
                          v-model:color="themeStore.colorPrimary"
                          @click="themeStore.changeColorPrimary"/>
        </a-form-item>
        <a-form-item>
          <nav-select v-model="themeStore.layoutType" @click="themeStore.changeLayoutType" @change="handleChangeLayoutType"/>
        </a-form-item>
      </a-form>
    </template>
  </login-setting-base-component>
</template>

<script setup lang="ts">

import LoginSettingBaseComponent from "@/components/login-setting/LoginSettingBaseComponent.vue";
import {ref, type Ref} from "vue";
import ColorSelect from "@/components/color-select/index.vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
import settings from "@/settings.ts";
import NavSelect from "@/components/nav-type-select/index.vue";
import {usePermissionStore} from "@/stores/modules/permission.ts";
const permissionStore = usePermissionStore()
const themeStore = useThemeStore()
// 主题颜色
const colorList = ref<Array<{name: string,color: string}>>(settings.colorOptions)
// 向外抛出函数
const emits = defineEmits(['back', 'skip', 'next'])
const handleNext = (loading:Ref<boolean>) => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    emits('next', loading.value)
  }, 2000)
}

const handleSkip = (loading:Ref<boolean>) => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    emits('skip', loading.value)
  }, 1000)
}
// 处理修改菜单分组模式
const handleChangeSiderGroup = () => {
  permissionStore.reloadMenu()
}

const handleChangeLayoutType = (key: string) => {
  if (key === "header-content") {
    themeStore.$state.siderGroup = false
    handleChangeSiderGroup()
  }
}
</script>

<style scoped>

</style>