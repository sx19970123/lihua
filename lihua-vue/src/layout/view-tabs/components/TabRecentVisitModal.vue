<template>
  <a-modal v-model:open="viewTabsStore.showRecentModal"
           :footer="null"
           :closable="false"
           width="350px"
           :mask-style="{'backdrop-filter':'blur(3px)'}"
           >
    <template #title>
      <a-flex :gap="6" justify="left" align="center">
        <FieldTimeOutlined /> <span style="font-size: 16px">最近使用</span>
      </a-flex>
      <a-divider style="margin-bottom: 10px;margin-top: 10px" />
    </template>
    <a-list class="scrollbar" :data-source="data" :bordered="false" size="small" :split="false" style="max-height: 550px;">
      <template #renderItem="{ item }">
        <a-list-item class="list-item" @click="handleClickItem(item.path)">
          <a-list-item-meta>
            <template #title>
                <a-button type="text">
                  <component :is="item.icon"/>
                  {{item.label}}
                  {{ handleTime(item.openTime) }}
                </a-button>
            </template>
          </a-list-item-meta>
        </a-list-item>
      </template>
    </a-list >
  </a-modal>
</template>

<script setup lang="ts">
import {ref, watch} from "vue";
import {format, relativeDate} from "@/utils/date";
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
  const recentTabsJson = localStorage.getItem(viewTabsStore.$state.tabCacheKey)
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
    } else if (time.substring(0,10) === relativeDate(new Date(),'yyyy-MM-dd',-1)) {
      return '昨天 ' + time.substring(11)
    } else {
      return time
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

