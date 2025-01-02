<template>
  <a-flex vertical :gap="16">
    <a-alert :message="'Redis 内存占用：' + useMemory" show-icon/>
    <a-flex :gap="16" wrap="wrap">
      <a-card class="cache-card">
        <a-typography-title :level="5">缓存类型</a-typography-title>
        <div class="scrollbar cache-monitor-max-content-height">
          <selectable-card :data-source="keyTypeList"
                       item-key="keyPrefix"
                       :item-style="{width: '100%'}"
                       :gap="8"
                       @change="handleChangeKeyTypeItem"
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
          </selectable-card>
        </div>
      </a-card>
      <a-card class="cache-card">
        <a-flex justify="space-between">
          <a-typography-title :level="5">缓存键值（{{keys.length}}）</a-typography-title>
          <div>
            <a-tooltip v-if="targetKeyType" :title="'刷新' + targetKeyType.label + '键值列表'">
              <a-button type="link" :loading="loadingKeys" @click="loadKeyList(targetKeyType)">
                <template #icon>
                  <ReloadOutlined />
                </template>
                刷新
              </a-button>
            </a-tooltip>
            <a-tooltip  v-if="targetKeyType" :title="'清空' + targetKeyType.label + '键值列表'">
              <a-button type="link" danger :loading="loadingKeys" @click="removeCacheInfo(targetKeyType.keyPrefix)">
                <template #icon>
                  <DeleteOutlined />
                </template>
                清空
              </a-button>
            </a-tooltip>
          </div>
        </a-flex>
        <div class="scrollbar cache-monitor-max-content-height">
          <selectable-card :data-source="keys"
                       item-key="key"
                       :item-style="{width: '100%'}"
                       :gap="8"
                       @change="handleClickKey"
                      :loading="loadingKeys"
          >
            <template #content="{item, index}">
             <a-flex justify="space-between" align="center">
               <a-typography-text ellipsis>
                 {{item.key}}
               </a-typography-text>
               <a-button danger type="link" @click="(event:MouseEvent) => {event.stopPropagation();removeCacheInfo(item.key)}">
                 <template #icon>
                   <DeleteOutlined />
                 </template>
               </a-button>
             </a-flex>
            </template>
          </selectable-card>
        </div>
      </a-card>
      <a-card class="cache-card">
        <a-flex justify="space-between">
          <a-typography-title :level="5">缓存内容</a-typography-title>
          <div v-if="infoKey">
            <a-tooltip title="刷新缓存内容">
              <a-button size="middle" type="link" :loading="loadingInfo" @click="loadCacheInfo(infoKey)">
                <template #icon>
                  <ReloadOutlined />
                </template>
                刷新
              </a-button>
            </a-tooltip>
            <a-tooltip title="删除缓存内容">
              <a-button type="link" danger :loading="loadingInfo" @click="removeCacheInfo(infoKey)">
                <template #icon>
                  <DeleteOutlined />
                </template>
                删除
              </a-button>
            </a-tooltip>
          </div>
        </a-flex>
        <a-spin :spinning="loadingInfo"  v-if="infoKey">
          <a-descriptions
              :column="1"
              bordered layout="vertical"
              size="small"
              class="scrollbar cache-monitor-max-content-height"
          >
            <a-descriptions-item label="缓存键值">
              {{infoKey}}
            </a-descriptions-item>
            <a-descriptions-item label="剩余有效时间">
              {{info?.expireMinutes ? info?.expireMinutes < 0 ? info?.expireMinutes : + info?.expireMinutes +' 分钟' : ''}}
            </a-descriptions-item>
            <a-descriptions-item label="缓存内容">
              {{info?.value}}
            </a-descriptions-item>
          </a-descriptions>
        </a-spin>

        <selectable-card
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
import SelectableCard from "@/components/selectable-card/index.vue"
import {
  cacheInfo,
  cacheKeyGroups,
  cacheKeys,
  memoryInfo,
  remove
} from "@/api/monitor/cache/Cache.ts";
import {onMounted, ref} from "vue";
import {message} from "ant-design-vue";
import type {CacheMonitor} from "@/api/monitor/cache/type/CacheMonitor.ts";
import {ResponseError} from "@/api/global/Type.ts";
// 内存占用大小
const useMemory = ref<string>('')
// 缓存类型集合
const keyTypeList = ref<CacheMonitor[]>([])
// 缓存键值集合
const keys = ref<{key: string}[]>([])
// 缓存内容
const info = ref<CacheMonitor>()
// 缓存内容对应的键值
const infoKey = ref<string>()
// 选中的缓存类型
const targetKeyType = ref<CacheMonitor>()
// 键值列表加载
const loadingKeys = ref<boolean>(false)
// 缓存内容加载
const loadingInfo = ref<boolean>(false)

// 加载内存占用
const initMemoryInfo = async () => {
  try {
    const resp = await memoryInfo()
    if (resp.code === 200) {
      useMemory.value = resp.data + ' MB'
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  }
}

// 加载缓存类型
const initCacheKeyGroups = async () => {
  try {
    const resp = await cacheKeyGroups()
    if (resp.code === 200) {
      keyTypeList.value = resp.data
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.log(e)
    }
  }
}

// 处理点击缓存类型
const handleChangeKeyTypeItem = async ({item}:{item: CacheMonitor | undefined}) => {
  // 取消选中时清空数据
  if (!item) {
    keys.value = []
    info.value = undefined
    infoKey.value = undefined
    targetKeyType.value = undefined
    return
  }
  await loadKeyList(item)
}

// 加载键值列表
const loadKeyList = async (item: CacheMonitor) => {
  loadingKeys.value = true
  // 查询缓存key
  try {
    const resp = await cacheKeys(item.keyPrefix)
    if (resp.code === 200) {
      info.value = undefined
      infoKey.value = undefined
      keys.value = []
      targetKeyType.value = item
      resp.data.forEach(key => keys.value.push({key: key}))
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  } finally {
    loadingKeys.value = false
  }
}

// 处理点击缓存key
const handleClickKey = async ({item}:{item: {key: string | undefined}}) => {
  if (!item) {
    info.value = undefined
    infoKey.value = undefined
    return
  }
  infoKey.value = item.key
  // 加载缓存内容
  if (item.key) {
    await loadCacheInfo(item.key)
  }
}

// 加载缓存内容
const loadCacheInfo = async (key: string) => {
  loadingInfo.value = true
  try {
    const resp = await cacheInfo(key)
    if (resp.code === 200) {
      info.value = resp.data
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  } finally {
    loadingInfo.value = false
  }
}

// 删除缓存
const removeCacheInfo = async (key: string) => {
  try {
    const resp = await remove(key);
    if (resp.code === 200) {
      message.success(resp.msg)
      if (targetKeyType.value) {
        await loadKeyList(targetKeyType.value)
      }
    } else {
      message.error(resp.msg)
    }
  } catch (e) {
    if (e instanceof ResponseError) {
      message.error(e.msg)
    } else {
      console.error(e)
    }
  }
}


onMounted(() => {
  initMemoryInfo()
  initCacheKeyGroups()
})
</script>
<style scoped>
.cache-card {
  flex: 1;
  min-width: 300px;
}
</style>
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
