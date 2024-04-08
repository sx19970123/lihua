<template>
  <div>
    <a-button @click="open">loading</a-button>
    <template v-for="item in 't1,t1-1,t1-1-1'.split(',')">
      <dict-tag :dict-data-option="tree_test" :dict-data-value="item" root-tree-node-prefix=""/>
    </template>
  </div>
</template>




<script setup lang="ts">
import spin from "@/components/spin";
import { LoadingOutlined } from '@ant-design/icons-vue';
import {h, onMounted} from "vue";
import dictTag from "@/components/dict-tag/index.vue"
import {initDict} from "@/utils/dict";

const {tree_test} = initDict("tree_test")
const open = () => {
  spin.service({
    size: 'large',
    indicator: h(LoadingOutlined,{
      style: {
        fontSize: '48px'
      }
    }),
    tip: '加载中'
  })
  setTimeout(() => {
    spin.service().close()
  },1000)
}


</script>

<style scoped>

</style>
