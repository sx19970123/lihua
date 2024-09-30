<template>
  <a-flex vertical :gap="16">
    <a-alert :message="'Redis 内存占用：' + useMemory" show-icon/>
    <a-flex :gap="16">
      <a-card style="flex: 1">
        <a-typography-title :level="5">缓存类型</a-typography-title>
        <div class="scrollbar cache-monitor-max-content-height">
          <card-select :data-source="keyGroups"
                       item-key="keyPrefix"
                       :item-style="{width: '100%'}"
                       :gap="8"
                       @click="handleClickKeyGroupItem"
          >
            <template #content="{item, index}">
              <a-flex justify="space-between">
                <a-typography-text ellipsis>
                  {{item.keyPrefix}}
                </a-typography-text>
                <a-typography-text ellipsis>
                  {{item.label}}
                </a-typography-text>
              </a-flex>
            </template>
          </card-select>
        </div>
      </a-card>
      <a-card style="flex: 1; overflow: hidden">
        <a-typography-title :level="5">缓存key</a-typography-title>
        <div class="scrollbar cache-monitor-max-content-height">
          <card-select :data-source="keys"
                       item-key="key"
                       :item-style="{width: '100%'}"
                       :gap="8"
                       @click="handleClickKey"
          >
            <template #content="{item, index}">
              <a-typography-text ellipsis>
                {{item.key}}
              </a-typography-text>
            </template>
          </card-select>
        </div>
      </a-card>
      <a-card style="flex: 1">
        <a-typography-title :level="5">缓存内容</a-typography-title>
        <a-descriptions
            v-if="infoKey"
            :column="1"
            bordered layout="vertical"
            size="small"
            class="scrollbar cache-monitor-max-content-height">
          <a-descriptions-item label="缓存key">
           {{infoKey}}
          </a-descriptions-item>
          <a-descriptions-item label="剩余有效时间">
            {{info?.expireMinutes + ' 分钟'}}
          </a-descriptions-item>
          <a-descriptions-item label="缓存内容">
            {{info?.value}}
          </a-descriptions-item>
        </a-descriptions>
        <card-select
            :data-source="[]"
            :item-style="{width: '100%'}"
            item-key="key"
            emptyDescription="请选择缓存key"
            v-else
        />
      </a-card>
    </a-flex>
  </a-flex>
</template>

<script setup lang="ts">
import CardSelect from "@/components/card-select/index.vue"
import {cacheInfo, cacheKeyGroups, cacheKeys, memoryInfo} from "@/api/monitor/cache/Cache.ts";
import {onMounted, ref} from "vue";
import {message} from "ant-design-vue";
import type {CacheMonitor} from "@/api/monitor/cache/type/CacheMonitor.ts";

const useMemory = ref<string>('')
const keyGroups = ref<CacheMonitor[]>([])
const keys = ref<{key: string}[]>([])
const info = ref<CacheMonitor>()
const infoKey = ref<string>()

// 加载内存占用
const initMemoryInfo = async () => {
  const resp = await memoryInfo()
  if (resp.code === 200) {
    useMemory.value = resp.data + ' MB'
  } else {
    message.error(resp.msg)
  }
}

// 加载缓存类型
const initCacheKeyGroups = async () => {
  const resp = await cacheKeyGroups()
  if (resp.code === 200) {
    keyGroups.value = resp.data
  } else {
    message.error(resp.msg)
  }
}

// 处理点击缓存类型
const handleClickKeyGroupItem = async ({item}:{item: CacheMonitor}) => {
  const resp = await cacheKeys(item.keyPrefix)
  if (resp.code === 200) {
    info.value = undefined
    infoKey.value = undefined
    keys.value = []
    resp.data.forEach(key => keys.value.push({key: key}))
  } else {
    message.error(resp.msg)
  }
}

// 处理点击缓存key
const handleClickKey = async ({item}:{item: {key: string}}) => {
  infoKey.value = item.key
  const resp = await cacheInfo(item.key)
  if (resp.code === 200) {
    info.value = resp.data
  } else {
    message.error(resp.msg)
  }
}

onMounted(() => {
  initMemoryInfo()
  initCacheKeyGroups()
})
</script>

<style>
/* 根据是否开启多任务栏，设定不同的content高度 */
[view-tabs=show][show-hide-layout=show] .cache-monitor-max-content-height {
  max-height: calc(100vh - (48px + 54px + 16px + 156px));
}
[view-tabs=hide][show-hide-layout=show] .cache-monitor-max-content-height {
  max-height: calc(100vh - (48px + 16px + 156px));
}
[view-tabs=show][show-hide-layout=hide] .cache-monitor-max-content-height {
  max-height: calc(100vh - (54px + 16px + 156px));
}
</style>
