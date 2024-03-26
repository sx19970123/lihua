<template>
  <div>
    <a-flex :gap="8">
<!--      图标筛选-->
      <a-segmented v-model:value="segmentedValue" :options="segmentedData"/>
    </a-flex>
<!--    三种类型图标切换-->
    <div style="max-height: 350px;" class="scrollbar">
      <a-flex :gap="8" wrap="wrap"  style="margin-top: 30px">
        <div class="icon-group" :class="icon === modelValue ? 'icon-active' : ''" v-if="segmentedValue === '线框'" v-for="icon in outlinedIconList"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div>{{icon}}</div>
          </a-flex>
        </div>
        <div class="icon-group" :class="icon === modelValue ? 'icon-active' : ''" v-if="segmentedValue === '实底'" v-for="icon in filledIconList"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div>{{icon}}</div>
          </a-flex>
        </div>
        <div class="icon-group" :class="icon === modelValue ? 'icon-active' : ''" v-if="segmentedValue === '双色'" v-for="icon in twoToneIconList"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div>{{icon}}</div>
          </a-flex>
        </div>
        <div class="icon-group" :class="icon === modelValue ? 'icon-active' : ''" v-if="segmentedValue === '自定义'" v-for="icon in customIconLIst"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div>{{icon}}</div>
          </a-flex>
        </div>
      </a-flex>
    </div>
  </div>
</template>

<script setup lang="ts">
import {type Component, reactive, ref} from "vue";
import * as Icons from "@ant-design/icons-vue";
// 读取icon目录下图标
const modules = import.meta.glob("../icon/**/*.vue")

const icons: Record<string, Component> = Icons
// 三种图标类型集合
// 实底
const filledIconList = ref<string[]>([])
// 线框
const outlinedIconList = ref<string[]>([])
// 双色
const twoToneIconList = ref<string[]>([])
// 自定义
const customIconLIst = ref<string[]>([])


// 图标类型筛选
const segmentedData = reactive(['线框','实底','双色','自定义']);
const segmentedValue = ref(segmentedData[0]);

// v-modal
const emits = defineEmits(['update:modelValue'])
defineProps(['modelValue'])

// 初始化三种类型图标集合
for (let iconKey in icons) {
  if (icons[iconKey].name === 'create') {
    break
  }

  if (iconKey.endsWith('Outlined')) {
    outlinedIconList.value.push(iconKey)
  }
  if (iconKey.endsWith('Filled')) {
    filledIconList.value.push(iconKey)
  }
  if (iconKey.endsWith('TwoTone')) {
    twoToneIconList.value.push(iconKey)
  }
}
// 初始化自定义图标集合
for (let path in modules) {
  const match = path.match(/\/([^/]+)\.vue$/)
  if (match) {
    customIconLIst.value.push(match[1])
  }
}

// v-modal 双向绑定
const clickIcon = (icon: string) => {
  emits('update:modelValue',icon)
}

</script>
<style>
.icon-group {
  padding: 10px;
  border-radius: 8px;
  width: 180px;
  height: 80px;
  transition: all 0.2s ease; /* 添加过渡效果 */
}
.icon-group:hover {
  padding: 10px;
  border-radius: 8px;
  width: 180px;
  height: 80px;
  cursor: pointer;
  color: #ffffff;
  transition: all 0.2s ease; /* 添加过渡效果 */
}
.icon-group:hover .icon-size {
  font-size: 32px;
  transition: all 0.2s ease; /* 添加过渡效果 */
}
.icon-size {
  font-size: 24px;
  transition: all 0.2s ease; /* 添加过渡效果 */
}

/* 使用自定义css 变量 --colorPrimary 实现颜色跟随主题 */
.icon-group:hover {
  background: var(--colorPrimary);
}
.icon-active {
  background: var(--colorPrimary);
  color: #ffffff;
}


</style>
