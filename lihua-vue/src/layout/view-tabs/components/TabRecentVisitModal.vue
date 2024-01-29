<template>
  <a-modal v-model:open="props.show"
           :footer="null"
           :closable="false"
           @cancel="close"
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

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  activeTabs: {
    type:Array<any>,
    default: []
  }
})

const data = ref()
const activeTab = ref(props.activeTabs)

/**
 * 监听弹窗打开获取 list 数据
 */
watch(()=> props.show , (value)=> {
  if (value) {
    handleRecentList()
  }
})

/**
 * 监听 view-tabs 变化
 */
watch(() => props.activeTabs, (value) => {
  activeTab.value = value
},{
  deep: true
})

/**
 * 处理最近访问数据
 */
const handleRecentList = () => {
  const recentTabsJson = localStorage.getItem('recent-tabs')
  if (recentTabsJson) {
    const recentTabs =  JSON.parse(recentTabsJson)
    // 当前tab页有数据
    if (activeTab && activeTab.value.length > 0) {
      const actPathList = activeTab.value.map(tab => tab.routerPathKey)
      data.value = recentTabs.filter((tab: any) => !actPathList.includes(tab.path))
    } else {
      data.value = recentTabs
    }
  }
}

const handleTime = (time: string) => {
  if (time) {
    if (time.substring(0, 10) === format(new Date(),'yyyy-MM-dd')) {
      return  time.substring(11)
    }
  }
}

/**
 * 注册抛出的函数
 */
const emits = defineEmits(['closeRecentModal','switchTab'])

/**
 * 调用父组件关闭菜单
 */
const close = () => {
  emits("closeRecentModal")
}
/**
 * 调用父组件进行路由切换
 * @param path
 */
const handleClickItem = (path) => {
  emits('switchTab', path)
  emits("closeRecentModal")
}
</script>



<style scoped>


.list-item:hover {
  background-color: #f7f7f7;
  border-radius: 8px;
  cursor: pointer;
}
</style>
