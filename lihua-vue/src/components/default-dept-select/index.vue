<template>
  <div>
    <a-form>
      <a-form-item>
        <easy-tree-select :tree-data="deptTree"
                          :default-expand-all="true"
                          :multiple="false"
                          :field-names="{children:'children', title:'name', key:'id'}"
                          :body-style="{padding: '16px', backgroundColor: themeStore.isDarkTheme ? '#1f1f1f' : '', borderRadius: '8px'}"
                          :bordered="false"
                          :show-toolbar="false"
                          v-model="userStore.defaultDept.id"
        >
          <template #title="{name, code, id, keyword}">
            <div @mouseover="handleMouseOver(id)" @mouseleave="handleMouseLeave" @click="handleSetDefaultDept(id)">
              <a-space>
                <a-flex :gap="8">
                  <a-tooltip :title="userStore.defaultDept.id !== id ? '点击设为默认部门' : '默认部门'" placement="right" :arrow="false" :getPopupContainer="(triggerNode:Document) => triggerNode.parentNode">
                    <div v-if="name.indexOf(keyword) > -1">
                      <span>{{ name.substring(0, name.indexOf(keyword)) }}</span>
                      <span :style="{'color':  themeStore.getColorPrimary()}">{{ keyword }}</span>
                      <span>{{ name.substring(name.indexOf(keyword) + keyword.length) }}</span>
                    </div>
                    <span v-else>{{ name }}</span>
                  </a-tooltip>
                  <a-typography-text type="secondary" v-if="showDeptCode">{{ code }}</a-typography-text>
                </a-flex>
                <span v-if="userStore.defaultDept.id === id">
                 <a-tag :color="themeStore.getColorPrimary()" :bordered="false" style="margin-bottom: 2px">默认</a-tag>
                </span>
              </a-space>
            </div>
          </template>
        </easy-tree-select>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import {ref} from "vue";
import {useUserStore} from "@/stores/user.ts";
import {useThemeStore} from "@/stores/theme.ts";
import {setDefaultDept} from "@/api/system/profile/Profile.ts";
import {message} from "ant-design-vue";
import {ResponseError} from "@/api/global/Type.ts";
import EasyTreeSelect from "@/components/easy-tree-select/index.vue"
const themeStore = useThemeStore();
const userStore = useUserStore();
const deptTree = ref<Array<SysDept>>(userStore.deptTrees)

// 鼠标移入的id
const hoverId = ref<string | undefined>()

const {showDeptCode = true} = defineProps<{
  showDeptCode?: boolean;
}>()

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
  try {
    const resp = await setDefaultDept(deptId)
    if (resp.code === 200) {
      // 更新默认部门
      userStore.updateDefaultDept(resp.data)
      emits('deptSelect', resp)
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
</script>