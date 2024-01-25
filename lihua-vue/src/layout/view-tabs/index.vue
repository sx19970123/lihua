<template>
  <div :style="{'background': '#fff'}">
    <a-tabs :activeKey="activeKey"
            type="editable-card"
            size="small"
            hide-add
            @edit="handleEdit"
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

<script setup lang="ts">
import TabPaneMenu from "@/layout/view-tabs/components/TabPaneMenu.vue";
import TabPaneButton from "@/layout/view-tabs/components/TabPaneButton.vue";
import TabRightMenu from "@/layout/view-tabs/components/TabRightMenu.vue";
import { computed, reactive, ref, type UnwrapNestedRefs, watch } from "vue";
import { usePermissionStore } from "@/stores/modules/permission";
import { useRoute } from "vue-router";
const permission = usePermissionStore()
const route = useRoute()
const allViewTags = computed(() => permission.viewTabs)

// 当前活动的数据
const activeTab: UnwrapNestedRefs<any[]> = reactive([])

// 初始化固定的数据
allViewTags.value.forEach(tag => {
  if (tag.affix) {
    activeTab.push(tag)
  }
})

const activeKey = ref()

// 监听路由变化，切换
watch(() => route.path,(value) => {
  // 是否在活动tab中存在，不存在调用新增方法
  const includes = activeTab.map(act => act.routerPathKey).includes(value)
  if (!includes) {
    allViewTags.value.forEach(tab => {
      if (tab.routerPathKey === value) {
        addActiveTab(tab)
      }
    })
  }
  activeKey.value = value
})

// 新增到tabs
const addActiveTab = (tab: UnwrapNestedRefs<any>) => {
  activeTab.push(tab)
}

const deleteActiveTab = (tab: UnwrapNestedRefs<any>) => {
  const index = activeTab.indexOf(tab);
  if (index !== -1) {
    // 使用 splice 方法删除元素，并将新的数组赋值给 activeTab
    activeTab.splice(index, 1);
  }
};

const handleEdit = (key: string) => {
  const tabToDelete = activeTab.find(tab => tab.routerPathKey === key);

  if (tabToDelete) {
    deleteActiveTab(tabToDelete);
  }
};



</script>

<style scoped lang="less">

</style>
