<template>
  <card-show cardKey="componentName2"
             style="width: 100%"
             @card-click="handleClick"
             :hover-scale="1.03"
             :expanded-width="600"
             :middle-style="{'background':'#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card style="border: none;" :body-style="{height: '200px'}">
        <a-typography-title :level="4">index_1</a-typography-title>
        <a-typography-title>
          1.0
        </a-typography-title>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar" id="test" style="border: none;height: 600px">

      </a-card>
    </template>
  </card-show>
</template>
<script setup lang="ts">
import CardShow from "@/components/card-show/index.vue";
import {getCurrentInstance, ref} from "vue";
const componentName = getCurrentInstance()?.type.__name
const middleComplete = ref<boolean>(false)
const handleClick = (key:string,show:boolean) => {
  middleComplete.value = true
}
</script>
<style scoped>

</style>