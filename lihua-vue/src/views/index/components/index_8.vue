<template>
  <card-show :cardKey="componentName as string"
             style="width: 100%"
             :auto-complete="false"
             :is-complete="middleComplete"
             @card-click="handleClick"
             :hover-scale="1.01"
             :expanded-width="600"
             :middle-style="{'background':'#fff','background-size': 'contain','border-radius':' 8px'}"
  >
    <template #overview>
      <a-card :body-style="{height: '263px'}" class="card-background">
        <a-typography-title :level="4" ellipsis>更新日志</a-typography-title>
        <a-flex vertical>
          <a-typography-text ellipsis type="secondary">
            <a-typography-text ellipsis type="secondary">最新版本为：</a-typography-text>
            <a-typography-text ellipsis :style="{color:themeStore.getColorPrimary()}">{{latestVersion.version}}</a-typography-text>
          </a-typography-text>
          <a-typography-title ellipsis :level="5" style="margin-top: 8px">
            {{latestVersion.updateDate}}
          </a-typography-title>
          <div v-for="(item,index) in latestVersion.updateContent">
            <a-typography-text ellipsis v-if="index < 5">
              {{item}}
            </a-typography-text>
          </div>
          <a-typography-text ellipsis v-if="latestVersion.updateContent.length > 5">
            ...
          </a-typography-text>
        </a-flex>
      </a-card>
    </template>
    <template #middle>
      <a-card :body-style="{height: '263px'}" class="card-background child">
        <a-typography-title :level="4" ellipsis>更新日志</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">最新版本为：</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">{{latestVersion.version}}</a-typography-text>
        </a-typography-text>
      </a-card>
    </template>
    <template #detail>
      <a-card class="card-background" id="test" style="height: 600px">
        <a-typography-title :level="4" ellipsis>更新日志</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">最新版本为：</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">{{latestVersion.version}}</a-typography-text>
        </a-typography-text>
        <div class="scrollbar" style="height: 484px;margin-top: 16px">
          <a-timeline>
            <a-timeline-item v-for="item in versionInfo.lihuaUpdateLog">
              <a-typography-title :level="5">
                {{item.version}}
                <a-typography-text type="secondary">{{item.updateDate}}</a-typography-text>
              </a-typography-title>
              <a-flex v-for="content in item.updateContent" vertical>
                <a-typography-text >{{content}}</a-typography-text>
              </a-flex>
            </a-timeline-item>
          </a-timeline>
        </div>
      </a-card>
    </template>
  </card-show>
</template>
<script setup lang="ts">
import CardShow from "@/components/card-show/index.vue";
import {getCurrentInstance, ref} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
import {versionInfo} from "@/views/index/setting.ts";
const latestVersion = versionInfo.lihuaUpdateLog[0]
const themeStore = useThemeStore();
const componentName = getCurrentInstance()?.type.__name
const middleComplete = ref<boolean>(false)
const handleClick = (key:string,show:boolean) => {
  middleComplete.value = true
}
</script>
<style scoped>
.card-background {
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
.child {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-position-y: 10px; /* 增加10像素间距 */
  background-position-x: calc(100% - 10px); /* 保持右对齐 */
  background-repeat: no-repeat;
  background-size: 36px 36px;
}
</style>