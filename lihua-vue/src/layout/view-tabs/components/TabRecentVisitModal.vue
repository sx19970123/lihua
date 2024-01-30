<template>
  <a-modal v-model:open="viewTabsStore.showRecentModal"
           :footer="null"
           :closable="false"
           width="350px"
           :mask-style="{'backdrop-filter':'blur(3px)'}"
           >
    <a-list class="scrollbar" :data-source="data" :bordered="false" size="small" :split="false" style="max-height: 550px">
      <template #renderItem="{ item }">
        <a-list-item class="list-item" @click="handleClickItem(item.path)">
          <a-list-item-meta>
            <template #title>
              <a href="javascript:void(0)">
                <component :is="item.icon"/>
                {{item.label}}
              </a>
            </template>
          </a-list-item-meta>
          <div>
            {{ handleTime(item.openTime) }}
          </div>
        </a-list-item>
      </template>
    </a-list >
  </a-modal>
</template>

<script setup lang="ts">
import {ref, watch} from "vue";
import {format} from "@/utils/date";
import { useViewTabsStore } from "@/stores/modules/viewTabs";
const viewTabsStore = useViewTabsStore()
const data = ref()

/**
 * 监听 view-tabs 变化
 */
watch(() => viewTabsStore.viewTabs, (value) => {
  handleRecentList(value)
},{ deep: true })

/**
 * 处理最近访问数据
 */
const handleRecentList = (viewTabs:Array<any>) => {
  const recentTabsJson = localStorage.getItem('recent-tabs')
  if (recentTabsJson) {
    const recentTabs =  JSON.parse(recentTabsJson)
    // 当前tab页有数据
    if (viewTabs && viewTabs.length > 0) {
      const actPathList = viewTabs.map(tab => tab.routerPathKey)
      data.value = recentTabs.filter((tab: any) => !actPathList.includes(tab.path))
    } else {
      data.value = recentTabs
    }
  }
}

handleRecentList(viewTabsStore.viewTabs)

/**
 * 处理日期，当天隐藏日期
 * @param time
 */
const handleTime = (time: string) => {
  if (time) {
    if (time.substring(0, 10) === format(new Date(),'yyyy-MM-dd')) {
      return time.substring(11)
    }
  }
}

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
  viewTabsStore.closeRecentModal()
}

</script>

<style scoped>
.list-item:hover {
  background-color: #f7f7f7;
  border-radius: 8px;
  cursor: pointer;
}
</style>
