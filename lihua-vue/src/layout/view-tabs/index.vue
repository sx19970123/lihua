<template>
  <div :style="{'background': '#fff'}">
    <a-tabs :activeKey="1"
            type="editable-card"
            size="small"
            v-model:active-key="activeKey"
            hide-add
    >
      <a-tab-pane v-for="item in list" :key="item.key">
        <!--每个tab的下拉菜单-->
        <template #tab>
          <tab-pane-menu :data="item"/>
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
import {reactive, ref} from "vue";


const list = reactive([
  {
    label: '首页',
    key: '/index'
  },
  {
    label: '工作台',
    key: '/gzt'
  },
  {
    label: '表',
    key: '/lby/bzlb/lb/b'
  }
])

const activeKey = ref(list[0].key)

</script>

<style scoped lang="less">

</style>
