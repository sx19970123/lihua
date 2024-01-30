<template>
  <a-modal v-model:open="viewTabsStore.showStarModal"
           :footer="null"
           :closable="false"
           width="350px"
           :mask-style="{'backdrop-filter':'blur(3px)'}"
           >
    <a-list class="scrollbar" :data-source="data" :bordered="false" size="small" :split="false" style="max-height: 550px">
      <template #renderItem="{ item }">
        <a-list-item class="list-item" @click="handleClickItem(item.routerPathKey)">
          <a-list-item-meta>
            <template #title>
              <a href="javascript:void(0)">
                <component :is="item.icon"/>
                {{item.label}}
              </a>
            </template>
          </a-list-item-meta>
        </a-list-item>
      </template>
    </a-list >
  </a-modal>
</template>

<script setup lang="ts">
import {ref, watch} from "vue";
import { useViewTabsStore } from "@/stores/modules/viewTabs";
const viewTabsStore = useViewTabsStore()
const data = ref()

/**
 * 监听模态框开关变化。打开模态框重新载入数据
 */
watch(() => viewTabsStore.showStarModal, (value) => {
  if (value) {
    handleRecentList()
  }
},{deep: true})

/**
 * 数据载入
 */
const handleRecentList = () => {
  data.value = viewTabsStore.totalViewTabs.filter(tab => tab.star)
}

/**
 * 初始化
 */
handleRecentList()


/**
 * 注册抛出的函数
 */
const emits = defineEmits(['routeSkip'])

/**
 * 调用父组件进行路由切换
 * @param key
 */
const handleClickItem = (key: string) => {
  emits('routeSkip', key)
  viewTabsStore.closeStarModal()
}

</script>

<style scoped>
.list-item:hover {
  background-color: #f7f7f7;
  border-radius: 8px;
  cursor: pointer;
}
</style>
