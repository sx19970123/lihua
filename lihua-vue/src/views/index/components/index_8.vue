<template>
  <expandable-card
             style="width: 100%"
             :hover-scale="1.01"
             :expanded-width="600"
             :expanded-height="610"
             @beforeCardClose="resetContent"
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
      <a-card class="card-background">
        <a-typography-title :level="4" ellipsis>更新日志</a-typography-title>
        <a-typography-text ellipsis type="secondary">
          <a-typography-text type="secondary">最新版本为：</a-typography-text>
          <a-typography-text :style="{color:themeStore.getColorPrimary()}">{{latestVersion.version}}</a-typography-text>
        </a-typography-text>
        <div class="scrollbar" id="lihua-index-8-content" style="height: 484px;margin-top: 16px">
          <a-timeline>
            <div v-for="(item, index) in versionInfo.lihuaUpdateLog">
              <a-timeline-item v-if="index <= showIndex">
                <a-typography-title :level="5">
                  {{item.version}}
                  <a-typography-text type="secondary">{{item.updateDate}}</a-typography-text>
                </a-typography-title>
                <a-alert v-if="item.title" strong :message="item.title" style="margin-bottom: 8px;margin-right: 8px">{{item.title}}</a-alert>
                <a-flex v-for="content in item.updateContent" vertical>
                  <a-typography-text>{{content}}</a-typography-text>
                </a-flex>
              </a-timeline-item>
            </div>
            <a-flex>
              <a-button type="link"
                        style="margin: auto"
                        :disabled="versionInfo.lihuaUpdateLog.length === showIndex" @click="handleShowMore">
                <template #icon v-if="versionInfo.lihuaUpdateLog.length !== showIndex">
                  <DoubleRightOutlined style="rotate: 90deg" />
                </template>
                {{versionInfo.lihuaUpdateLog.length === showIndex ? '已显示全部' : '显示更多' }}
              </a-button>
            </a-flex>
          </a-timeline>
        </div>
      </a-card>
    </template>
  </expandable-card>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";
import {ref} from "vue";
import {useThemeStore} from "@/stores/theme.ts";
import {versionInfo} from "@/views/index/setting.ts";
const latestVersion = versionInfo.lihuaUpdateLog[0]
const themeStore = useThemeStore();
const showIndex = ref<number>(4)
// 显示更多
const handleShowMore = () => {
  if (versionInfo.lihuaUpdateLog.length - showIndex.value >= 5) {
    showIndex.value = showIndex.value + 5
  } else {
    showIndex.value = versionInfo.lihuaUpdateLog.length
  }
}
// 重置内容
const resetContent = () => {
  const e = document.getElementById('lihua-index-8-content');
  if (e) {
    e.scrollTop = 0
    showIndex.value = 4
  }
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
