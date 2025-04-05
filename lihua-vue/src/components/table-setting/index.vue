<template>
  <div style="margin-left: auto">
     <a-popover v-model:open="visiblePopover" placement="bottomRight" :arrow="false" trigger="click">
       <template #content>
<!--         header-->
         <a-flex class="header" align="center" :gap="8">
           <a-checkbox style="margin-left: 22px">全选</a-checkbox>
           <a-button type="link" style="margin-left: auto">重置</a-button>
         </a-flex>
         <a-divider style="margin: 4px 0 4px 0"/>
<!--         content-->
         <a-flex vertical :gap="8" style="width: 300px;max-height: 240px;overflow-x: hidden" class="scrollbar">
          <div v-for="(col, index) in columns">
            <a-flex align="center" :gap="8" :key="index">
              <HolderOutlined class="drag"/>
              <a-checkbox/>
              <a-typography-text ellipsis>
                {{col.title}}
              </a-typography-text>
              <a-flex style="margin-left: auto;" :gap="8">
                <a-slider style="width: 100px" :min="300" :max="600"/>
                <a-rate :count="1" :style="{color: themeStore.getColorPrimary()}">
                  <template #character>
                    <PushpinOutlined />
                  </template>
                </a-rate>
                <a-rate :count="1" :style="{color: themeStore.getColorPrimary()}">
                  <template #character>
                    <PushpinOutlined style="transform: scaleX(-1);margin-right: 8px"/>
                  </template>
                </a-rate>
              </a-flex>
            </a-flex>
          </div>
         </a-flex>
       </template>
       <a-button>
         <template #icon>
           <SettingOutlined />
         </template>
       </a-button>
     </a-popover>
  </div>
</template>
<script setup lang="ts">
import {ref} from "vue";
import type {ColumnsType, ColumnType} from "ant-design-vue/es/table/interface";
import {useThemeStore} from "@/stores/theme.ts";
const themeStore = useThemeStore();
// 控制弹出卡片是否显示
const visiblePopover = ref<Boolean>(false)
// 组件参数
const {modelValue} = defineProps<{
  modelValue: ColumnsType | ColumnType[]
}>()


// 表格信息
const columns = ref<ColumnType[]>(modelValue)
console.log(columns.value)

</script>

<style scoped>
.drag:hover {
  color: var(--colorPrimary);
  cursor: grab;
}
.drag:active {
  cursor: grabbing;
}
</style>
