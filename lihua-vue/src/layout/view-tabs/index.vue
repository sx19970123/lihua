<template>
  <div :style="{'background': '#fff'}">
    <a-tabs :activeKey="activeKey"
            type="editable-card"
            size="small"
            hide-add
            @edit="handleRemove"
            @change="handleSwitchTab"

    >
      <a-tab-pane v-for="tab in activeTab" :key="tab.routerPathKey" :closable="!tab.affix">
        <!--每个tab的下拉菜单-->
        <template #tab>
          <tab-pane-menu :data="tab"/>
        </template>
        <!--每个tab的关闭按钮-->
        <template #closeIcon>
          <tab-pane-button/>
        </template>
      </a-tab-pane>
      <!-- view-tabs 左侧空白-->
      <template #leftExtra>
        <div style="width: 12px;"></div>
      </template>
      <!--view-tabs 右侧下拉菜单-->
      <template #rightExtra>
        <tab-right-menu/>
      </template>
    </a-tabs>
  </div>
</template>

<script lang="ts" setup>
import TabPaneMenu from "@/layout/view-tabs/components/TabPaneMenu.vue";
import TabPaneButton from "@/layout/view-tabs/components/TabPaneButton.vue";
import TabRightMenu from "@/layout/view-tabs/components/TabRightMenu.vue";
import { computed, reactive, ref, type UnwrapNestedRefs, watch } from "vue";
import { usePermissionStore } from "@/stores/modules/permission";
import { useRoute,useRouter } from "vue-router";
const permission = usePermissionStore()
const route = useRoute()
const router = useRouter()
const allViewTags = computed(() => permission.viewTabs)

/**
 * 初始化
 */
const init = () => {
  // 当前活动的数据
  const activeTab: UnwrapNestedRefs<any[]> = reactive([])
  const activeKey = ref()
  // 初始化固定的数据
  allViewTags.value.forEach(tag => {
    if (tag.affix) {
      activeTab.push(tag)
    }
  })
  return {
    activeTab,
    activeKey
  }
}
const { activeTab, activeKey } = init()

/**
 * 传入key，进行对应标签页反选。新页面新增tab页
 * @param key tab页 key
 */
const handleSelectTab = (key: string) => {
  // 是否在活动tab中存在，不存在调用新增方法
  const includes = activeTab.map(act => act.routerPathKey).includes(key)
  if (!includes) {
    allViewTags.value.forEach(tab => {
      if (tab.routerPathKey === key) {
        activeTab.push(tab)
      }
    })
  }
  activeKey.value = key
}

/**
 * 切换标签，key值存在，则切换到对应的页面。
 * 否则判断是否为固定值，固定值切换到第一个，不是固定值切换到前一个
 * @param key
 */
const handleSwitchTab = (key?: string | number) => {
  if (typeof key === "string") {
    router.push(key)
  } else {
    if (key !== 0) {
      const tab = activeTab[key as number - 1]
      if (!tab.affix) {
        router.push(tab.routerPathKey)
      } else {
        router.push(activeTab[0].routerPathKey)
      }
    }
  }
}

/**
 * 删除标签，根据情况进行路由切换
 * @param key
 */
const handleRemove = (key: string) => {
  const index = activeTab.findIndex(tab => tab.routerPathKey === key);
  if (index !== -1) {
    activeTab.splice(index, 1);
    if (route.path === key) {
      handleSwitchTab(index)
    }
  }
};


// 监听路由变化，切换
watch(() => route.path,(value) => {
  handleSelectTab(value)
})

// 第一次加载标签
handleSelectTab(route.path);
</script>

<style scoped lang="less">

</style>
