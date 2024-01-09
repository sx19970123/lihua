<template>
  <div :style="{'background': '#fff','padding-right':'16px','padding-left':'16px'}">
    <a-tabs :activeKey="1"
            type="editable-card"
            size="small"
            hide-add
    >
      <a-tab-pane :key="i" :closable="i !== 1" v-for="i in 30">
        <!--每个tab的下拉菜单-->
        <template #tab>
          <tab-pane-menu/>
        </template>
        <!--每个tab的关闭按钮-->
        <template #closeIcon>
          <tab-pane-button/>
        </template>
      </a-tab-pane>
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
</script>

<style scoped lang="less">
/deep/ .ant-tabs-card.ant-tabs-small >.ant-tabs-nav .ant-tabs-tab {
  padding: 4px 8px;
}
</style>
