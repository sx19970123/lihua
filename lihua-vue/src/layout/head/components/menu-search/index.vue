<template>
  <div>
    <div class="header-right-item" @click="open = true" v-show="!open">
      <a-input placeholder="搜索" readonly class="title-search-input">
        <template #prefix>
          <SearchOutlined />
        </template>
        <template #suffix>
          <a-tag style="margin-right: 4px">⌘</a-tag>
          <a-tag style="margin-right: 0">k</a-tag>
        </template>
      </a-input>
    </div>
    <a-modal v-model:open="open" :closable="false">
      <a-typography-text strong>我的收藏</a-typography-text>
      <selectable-card
          :card-style="{marginTop: '4px', marginBottom: '4px'}"
          class="menu-content"
          v-model="pathKey"
          :gap="4"
          :data-source="[{index: 1},{index: 2},{index: 3},{index: 4},{index: 5},{index: 6},{index: 7},{index: 8}]"
          item-key="index"
          >
        <template #content="{item}">
          {{item.index}}
        </template>
      </selectable-card>
      <a-typography-text strong>最近打开</a-typography-text>
      <selectable-card
          :card-style="{marginTop: '4px', marginBottom: '4px'}"
          v-model="pathKey"
          :gap="4"
          :data-source="[{index: 1},{index: 2},{index: 3},{index: 4},{index: 5}]"
          item-key="index"
          >
        <template #content="{item}">
          {{item.index}}
        </template>
      </selectable-card>
      <a-typography-text strong>所有菜单</a-typography-text>
      <selectable-card
          :card-style="{marginTop: '4px', marginBottom: '4px'}"
          class="menu-content"
          v-model="pathKey"
          :gap="4"
          :data-source="[{index: 1},{index: 2},{index: 3},{index: 4},{index: 5},{index: 6},{index: 7},{index: 8},{index: 9},{index: 10}]"
          item-key="index"
          vertical>
        <template #content="{item}">
          {{item.index}}
        </template>
      </selectable-card>
      <!--      头部搜索栏-->
      <template #title>
        <a-input placeholder="搜索菜单" size="large" allow-clear ref="menuSearchInputRef">
          <template #prefix>
            <SearchOutlined />
          </template>
        </a-input>
      </template>
      <!--      底部操作提示-->
      <template #footer>
        <a-flex gap="16">
          <div>
            <a-tag class="bottom-tag-tips"><EnterOutlined /></a-tag> 进入
          </div>
          <div>
            <a-tag class="bottom-tag-tips"><SwapOutlined style="transform: rotate(90deg)"/></a-tag> 切换
          </div>
          <div>
            <a-tag class="bottom-tag-tips">ESC</a-tag> 关闭
          </div>
        </a-flex>
      </template>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import {nextTick, onBeforeUnmount, onMounted, ref, useTemplateRef, watch, watchEffect} from "vue";
import SelectableCard from "@/components/selectable-card/index.vue";

// modal开关
const open = ref<boolean>(false)
// 菜单path
const pathKey = ref<number>(1)
// 搜索框ref
const menuSearchInputRef = useTemplateRef<HTMLInputElement>('menuSearchInputRef')

// 监听键盘按下事件
const handleKeydown = (e: KeyboardEvent) => {

  // 快捷方式，开关modal
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
    e.preventDefault()
    open.value = !open.value
  }

  // 上按键，选择菜单
  if (open.value && e.key === 'ArrowDown') {
    e.preventDefault()
    pathKey.value++
  }

  // 下按键，选择菜单
  if (open.value && e.key === 'ArrowUp') {
    e.preventDefault()
    pathKey.value--
  }

  // 回车键，进入菜单
  if (open.value && e.key === 'Enter') {

  }
}

watchEffect(() => {
  // 打开modal时input自动聚焦
  if (open.value) {
    nextTick(() => {
      menuSearchInputRef.value?.focus()
    })
  }
})

onMounted(() => {
  window.addEventListener('keydown', handleKeydown)
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', handleKeydown)
})
</script>

<style scoped>
.title-search-input {
  width: 130px;
  margin-right: 8px;
}
.title-search-input:hover {
  cursor: pointer !important;
}
.bottom-tag-tips {
  margin-right: 2px;
}
:deep(.title-search-input input) {
  cursor: pointer;
}
</style>
