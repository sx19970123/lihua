<template>
  <div>
    <a-input-search style="max-width: 200px;margin-bottom: 16px" placeholder="请输入部门名称"></a-input-search>
    <a-tree :tree-data="deptTree"
            :selectable="false"
            defaultExpandAll
            :field-names="{children:'children', title:'name', key:'id' }">
      <template #title="{name,code,id}">
        <div @mouseover="handleMouseOver(id)" @mouseleave="handleMouseLeave" @dblclick="handleSetDefaultDept(id)">
          <a-flex :gap="16" align="center">
            <a-flex :gap="8">
              <a-typography-text strong>{{name}}</a-typography-text>
              <a-typography-text type="secondary">{{code}}</a-typography-text>
            </a-flex>
            <span v-if="defaultDeptId === id">
              <a-tag :color="themeStore.colorPrimary">默认</a-tag>
            </span>
            <span v-show="hoverId === id && defaultDeptId !== id">
              <a-button type="link" size="small" @click="handleSetDefaultDept(id)">设为默认</a-button>
            </span>
          </a-flex>
        </div>
      </template>
    </a-tree>
  </div>
</template>

<script setup lang="ts">

import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import {ref, watch} from "vue";
import {useUserStore} from "@/stores/modules/user.ts";
import {useThemeStore} from "@/stores/modules/theme.ts";
import {setDefaultDept} from "@/api/system/profile/Profile.ts";
import {message} from "ant-design-vue";
const themeStore = useThemeStore();
const userStore = useUserStore();
const deptTree = ref<Array<SysDept>>(userStore.deptTrees)
const defaultDeptId = ref<string | undefined>(userStore.defaultDept.id)

// 鼠标移入的id
const hoverId = ref<string | undefined>()

// 鼠标移入
const handleMouseOver = (id: string) => {
  hoverId.value = id
}
// 鼠标移出
const handleMouseLeave = () => {
  hoverId.value = undefined
}

// 设置默认部门
const handleSetDefaultDept = async (deptId: string) => {
  if (deptId === defaultDeptId.value) {
    return;
  }

  const resp = await setDefaultDept(deptId)
  if (resp.code === 200) {
    // 更新默认部门
    userStore.updateDefaultDept(resp.data)
    message.success(resp.msg)
  } else {
    message.error(resp.msg)
  }
}

// 监听默认部门变化
watch(() => userStore.defaultDept.id, (value) => {
  defaultDeptId.value = value
})
</script>

<style scoped>

</style>