<template>
  <div :class="props.size"
       :style="{'width':props.width}"
  >
    <a-flex :gap="8">
<!--      图标筛选-->
      <a-segmented v-model:value="segmentedValue" :options="segmentedData"/>
    </a-flex>
<!--    三种类型图标切换-->
    <div :style="{maxHeight: props.maxHeight}" class="scrollbar">
      <a-flex :gap="8" wrap="wrap"  style="margin-top: 30px">
        <div class="icon-group" :class="icon === modelValue ? 'icon-active' : ''" v-if="segmentedValue === '线框'" v-for="icon in outlinedIconList"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div class="text-ellipsis">{{icon}}</div>
          </a-flex>
        </div>
        <div class="icon-group" :class="icon === modelValue ? 'icon-active' : ''" v-if="segmentedValue === '实底'" v-for="icon in filledIconList"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div class="text-ellipsis">{{icon}}</div>
          </a-flex>
        </div>
        <div class="icon-group" :class="icon === modelValue ? 'icon-active' : ''" v-if="segmentedValue === '双色'" v-for="icon in twoToneIconList"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div class="text-ellipsis">{{icon}}</div>
          </a-flex>
        </div>
        <div class="icon-group" :class="icon === modelValue ? 'icon-active' : ''" v-if="segmentedValue === '自定义'" v-for="icon in customIconLIst"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div class="text-ellipsis">{{icon}}</div>
          </a-flex>
        </div>
      </a-flex>
    </div>
  </div>
</template>

<script setup lang="ts">
import {type Component, computed, onMounted, type PropType, reactive, ref} from "vue";
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
const emits = defineEmits(['update:modelValue','click','loadComplete'])

const props = defineProps({
  modelValue: {
    type: String
  },
  size: {
    type: String as PropType<'small' | 'large' | 'default'>,
    default: 'default'
  },
  width: {
    type: String,
    default: '100%'
  },
  maxHeight: {
    type: String,
    default: '350px'
  }
})

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
  emits('click')
}
// 组件初始化完成
onMounted(() => {
  emits('loadComplete')
})
</script>
<style>
/* 小 */
.small {
  .icon-group {
    padding: 4px;
    border-radius: 8px;
    width: 60px;
    height: 32px;
    transition: all 0.2s ease;
  }
  .icon-group:hover {
    padding: 6px;
    border-radius: 8px;
    width: 60px;
    height: 32px;
    cursor: pointer;
    color: #ffffff;
    transition: all 0.2s ease;
  }
  .icon-group:hover .icon-size {
    font-size: 20px;
    transition: all 0.2s ease;
  }
  .icon-size {
    font-size: 18px;
    transition: all 0.2s ease;
  }

  /* 使用自定义css 变量 --colorPrimary 实现颜色跟随主题 */
  .icon-group:hover {
    background: var(--colorPrimary);
  }
  .icon-active {
    background: var(--colorPrimary);
    color: #ffffff;
  }
  .text-ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    text-align: center;
    width: 0;
  }
}


/* 默认 */
.default {
  .icon-group {
    padding: 6px;
    border-radius: 8px;
    width: 110px;
    height: 60px;
    transition: all 0.2s ease;
  }
  .icon-group:hover {
    padding: 6px;
    border-radius: 8px;
    width: 110px;
    height: 60px;
    cursor: pointer;
    color: #ffffff;
    transition: all 0.2s ease;
  }
  .icon-group:hover .icon-size {
    font-size: 26px;
    transition: all 0.2s ease;
  }
  .icon-size {
    font-size: 20px;
    transition: all 0.2s ease;
  }

  /* 使用自定义css 变量 --colorPrimary 实现颜色跟随主题 */
  .icon-group:hover {
    background: var(--colorPrimary);
  }
  .icon-active {
    background: var(--colorPrimary);
    color: #ffffff;
  }
  .text-ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    text-align: center;
    width: 110px;
  }
}


/* 大 */
.large {
  .icon-group {
    padding: 10px;
    border-radius: 8px;
    width: 180px;
    height: 80px;
    transition: all 0.2s ease;
  }
  .icon-group:hover {
    padding: 10px;
    border-radius: 8px;
    width: 180px;
    height: 80px;
    cursor: pointer;
    color: #ffffff;
    transition: all 0.2s ease;
  }
  .icon-group:hover .icon-size {
    font-size: 32px;
    transition: all 0.2s ease;
  }
  .icon-size {
    font-size: 24px;
    transition: all 0.2s ease;
  }

  /* 使用自定义css 变量 --colorPrimary 实现颜色跟随主题 */
  .icon-group:hover {
    background: var(--colorPrimary);
  }
  .icon-active {
    background: var(--colorPrimary);
    color: #ffffff;
  }
  .text-ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    text-align: center;
    width: 180px;
  }
}



</style>
