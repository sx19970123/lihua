<template>
  <card-show :cardKey="componentName as string"
             style="width: 100%"
             :auto-complete="false"
             :is-complete="middleComplete"
             @card-click="handleClick"
             :hover-scale="1.03"
             :expanded-width="600"
             :middle-style="{'background':'#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card :body-style="{height: '124px'}">
        <a-flex :gap="16"  align="center" style="margin-bottom: 10px">
          <img src="../static/icons8-java-logo-48.png">
          <a-typography-title :level="4" style="margin: 0" ellipsis>请作者喝一杯咖啡</a-typography-title>
        </a-flex>
        <a-typography-text type="secondary" ellipsis>如果这个项目对你有帮助，不妨支持一下作者吧~</a-typography-text>
      </a-card>
    </template>
    <template #middle>
      <a-card class="child">
        <a-flex :gap="16" align="center" style="margin-bottom: 10px">
          <img src="../static/icons8-java-logo-48.png">
          <a-typography-title :level="4" style="margin: 0" ellipsis>请作者喝一杯咖啡</a-typography-title>
        </a-flex>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar" id="test" style="height: 600px">
        <a-flex :gap="16" align="center">
          <img src="../static/icons8-java-logo-48.png">
          <a-typography-title :level="4" style="margin: 0" ellipsis>请作者喝一杯咖啡</a-typography-title>
        </a-flex>
        <a-typography-text type="secondary" ellipsis>如果这个项目对你有帮助，不妨支持一下作者吧~</a-typography-text>
        <a-flex :gap="16" class="dark-overlay" justify="space-around" style="margin-top:24px;">
          <img src="../static/wx.jpg" style="width: 250px;border-radius: 8px;margin-top: 30px">
          <img src="../static/zfb.jpg" style="width: 250px;border-radius: 8px">
        </a-flex>
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
.child {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>