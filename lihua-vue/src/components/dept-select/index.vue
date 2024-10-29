<template>
  <div>
    <a-form>
      <a-form-item>
        <a-input class="form-item-width"
                 @change="handleSearchDept"
                 v-model:value="deptKeyword"
                 placeholder="请输入部门名称"
                 allow-clear
        />
      </a-form-item>
      <a-form-item>
        <a-tree :tree-data="deptTree"
                :selectable="false"
                defaultExpandAll
                :field-names="{children:'children', title:'name', key:'id'}">
          <template #title="{name,code,id}">
            <div @mouseover="handleMouseOver(id)" @mouseleave="handleMouseLeave" @dblclick="handleSetDefaultDept(id)">
              <a-flex :gap="16" align="center">
                <a-flex :gap="8">
                  <a-tooltip :title="defaultDeptId !== id ? '双击设为默认部门' : ''" placement="right">
                    <div v-if="name.indexOf(deptKeyword) > -1">
                      <span>{{ name.substring(0, name.indexOf(deptKeyword)) }}</span>
                      <span :style="{'color':  themeStore.getColorPrimary()}">{{ deptKeyword }}</span>
                      <span>{{ name.substring(name.indexOf(deptKeyword) + deptKeyword.length) }}</span>
                    </div>
                    <span v-else>{{ name }}</span>
                  </a-tooltip>
                  <a-typography-text type="secondary">{{ code }}</a-typography-text>
                </a-flex>
                <span v-if="defaultDeptId === id">
                 <a-tag :color="themeStore.getColorPrimary()" style="font-size: 10px;" :bordered="false">默认</a-tag>
                </span>
<!--                会造成卡片长度抖动-->
                <span v-show="hoverId === id && defaultDeptId !== id" v-if="false">
                  <a-button type="link" size="small" @click="handleSetDefaultDept(id)">设为默认</a-button>
                </span>
              </a-flex>
            </div>
          </template>
        </a-tree>
        <a-empty class="form-item-width" v-if="deptTree.length === 0"/>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">

import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import {ref, watch} from "vue";
import {useUserStore} from "@/stores/user.ts";
import {useThemeStore} from "@/stores/theme.ts";
import {setDefaultDept} from "@/api/system/profile/Profile.ts";
import {message} from "ant-design-vue";
import {cloneDeep} from "lodash-es";
import {ResponseError} from "@/api/global/Type.ts";

const themeStore = useThemeStore();
const userStore = useUserStore();
const deptTree = ref<Array<SysDept>>(userStore.deptTrees)
const defaultDeptId = ref<string | undefined>(userStore.defaultDept.id)
const originDeptTree: Array<SysDept> = userStore.deptTrees
// 部门树检索关键字
const deptKeyword = ref<string>('')

// 鼠标移入的id
const hoverId = ref<string | undefined>()

const emits = defineEmits(['keywordChange','deptSelect'])

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

  try {
    const resp = await setDefaultDept(deptId)
    if (resp.code === 200) {
      // 更新默认部门
      userStore.updateDefaultDept(resp.data)
      emits('deptSelect', resp.data)
      message.success(resp.msg)
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  }
}

// 筛选部门
const handleSearchDept = () => {
  deptTree.value = filterTreeByLabel(originDeptTree, deptKeyword.value)
  emits('keywordChange',deptKeyword.value)
}
// 处理关键词过滤
const filterTreeByLabel = (tree: Array<SysDept>, keyword: string): Array<SysDept> => {
  const cloneTree = cloneDeep(tree);

  const filterNode = (node: SysDept): SysDept | null => {
    if (node.children) {
      node.children = node.children.map(filterNode).filter((child): child is SysDept => child !== null);
    }
    return node.name?.includes(keyword) || (node.children && node.children.length > 0) ? node : null;
  };

  return cloneTree.map(filterNode).filter((node: SysDept) => node !== null);
};

// 监听默认部门变化
watch(() => userStore.defaultDept.id, (value) => {
  defaultDeptId.value = value
})
</script>

<style scoped>
.form-item-width {
  width: 270px;
}
</style>