<template>
  <expandable-card
             style="width: 100%"
             :hover-scale="1.03"
             :expanded-width="600"
             :expanded-height="610"
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
      <a-card class="child" :body-style="{height: '124px'}">
        <a-flex :gap="16"  align="center" style="margin-bottom: 10px">
          <img src="../static/icons8-java-logo-48.png">
          <a-typography-title :level="4" style="margin: 0" ellipsis>请作者喝一杯咖啡</a-typography-title>
        </a-flex>
        <a-typography-text type="secondary" ellipsis>如果这个项目对你有帮助，不妨支持一下作者吧~</a-typography-text>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar" id="test">
        <a-flex :gap="16"  align="center" style="margin-bottom: 10px">
          <img src="../static/icons8-java-logo-48.png">
          <a-typography-title :level="4" style="margin: 0" ellipsis>请作者喝一杯咖啡</a-typography-title>
        </a-flex>
        <a-typography-text type="secondary" ellipsis>如果这个项目对你有帮助，不妨支持一下作者吧~</a-typography-text>
        <a-typography-text type="secondary" ellipsis>非常感谢大家的捐赠与支持，大家的捐赠将用于演示环境服务器的各项支出</a-typography-text>
        <a-flex :gap="16" class="dark-overlay" justify="space-around" style="margin-top:24px;">
          <img src="../static/wx.jpg" style="width: 250px;border-radius: 8px;margin-top: 30px">
          <img src="../static/zfb.jpg" style="width: 250px;border-radius: 8px">
        </a-flex>
      </a-card>
    </template>
  </expandable-card>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";
import { ref} from "vue";
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
