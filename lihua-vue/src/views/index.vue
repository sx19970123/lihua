<template>
  <a-button @click="open" v-hasRole="[123]">loading</a-button>
</template>




<script setup lang="ts">
import spin from "@/components/spin";
import { LoadingOutlined } from '@ant-design/icons-vue';
import {h} from "vue";
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
