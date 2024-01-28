<template>
  <a-dropdown>
    <button class="ant-tabs-nav-more">
      <MoreOutlined />
    </button>
    <template #overlay>
      <a-menu>
        <a-menu-item @click="showModal">
          最近的使用
        </a-menu-item>
        <a-menu-item>
          关闭所有标签
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
  <a-modal v-model:open="open" title="Basic Modal" :mask-style="{'backdrop-filter':'blur(10px)'}">
    <p>Some contents...</p>
    <p>Some contents...</p>
    <p>Some contents...</p>
  </a-modal>
</template>
<script setup lang="ts">
import {ref} from "vue";

const open = ref<boolean>(false);
const showModal = () => {
  open.value = true;
};

</script>
<style>
.ant-tabs-nav-more {
  margin-left: 2px !important;
  cursor: pointer;
  border-radius: 6px
}
.ant-tabs-nav-more:hover {
  margin-left: 2px !important;
  background: #f7f7f7 !important;
  cursor: pointer;
  border-radius: 6px
}
</style>
