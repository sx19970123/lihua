<template>
  <a-flex :gap="props.gap" :vertical="props.vertical">
    <div class="select-card" v-for="(item,index) in props.dataSource" @click.stop="handleClickCard(item)" :style="activeCardValueList.includes(item[props.itemKey]) ? bodyStyle : ''">
<!--      具名插槽 content-->
<!--      返回参数 dataSource：传入的option-->
<!--      返回参数 item：option遍历出的元素-->
<!--      返回参数 index：option遍历索引-->
<!--      返回参数 isSelected：是否为当前选中元素-->
<!--      返回参数 color：当前主题颜色-->
      <slot name="content"
            :dataSource="props.dataSource"
            :item="item"
            :index="index"
            :isSelected="activeCardValueList.includes(item[props.itemKey])"
            :color="themeStore.colorPrimary"/>
    </div>
  </a-flex>
</template>

<script setup lang="ts">
// 接受父组件传递参数
import {reactive, ref, watch, defineProps} from "vue";
import {useThemeStore} from "@/stores/modules/theme.ts";
const themeStore = useThemeStore()

// 定义父级传入的配置项
const props = defineProps({
  // 卡片间距
  gap: {
    type: Number,
    default: 16,
  },
  // 是否垂直排列
  vertical: {
    type: Boolean,
    default: false
  },
  // 是否支持多选
  multiple: {
    type: Boolean,
    default: false
  },
  // dataSource 对象中的唯一值
  itemKey: {
    type: String,
    required: true
  },
  // 可选的数据列表
  dataSource: {
    type: Array<any>,
    required: true
  }
})

// 定义 v-model 双向绑定和抛出的函数
const emit = defineEmits(['update:modelValue','click'])

// 选中的元素集合
const activeCardValueList = reactive<Array<any>>([])

// 处理点击选中
const handleClickCard = (item: any): void => {
  const keyItem = item[props.itemKey]

  if (!keyItem) {
    console.error("key 不是 option 集合对象的属性");
    return;
  }
  // 取消选中
  if (activeCardValueList.includes(keyItem)) {
    activeCardValueList.splice(activeCardValueList.indexOf(keyItem),1)
    props.multiple ? emit('update:modelValue', activeCardValueList) : emit('update:modelValue', null)
    return;
  }

  // 处理单选/多选
  if (props.multiple) {
    activeCardValueList.push(keyItem)
    // 多选情况下，返回选中值的集合
    emit('update:modelValue', activeCardValueList)
  } else {
    activeCardValueList.length = 0
    activeCardValueList.push(keyItem)
    // 单选情况下返回选中的值
    emit('update:modelValue', keyItem)
  }

  // 向父级抛出点击事件
  emit('click',{activeValueList: activeCardValueList ,item: item, props: props})
}

// 选中的卡片样式
const bodyStyle = ref<{
  'border': string,
  'box-shadow': string
}>({
  'border': '1px solid ' + themeStore.colorPrimary,
  'box-shadow': 'inset 0 0 0 1px ' + themeStore.colorPrimary,
})
// 监听主题变化同步卡片样式
watch(() => themeStore.colorPrimary, () => {
  bodyStyle.value = {
    'border': '1px solid ' + themeStore.colorPrimary,
    'box-shadow': 'inset 0 0 0 1px ' + themeStore.colorPrimary,
  }
})
</script>

<style scoped>
  .select-card {
    border: 1px solid #d9d9d9;
    border-radius: 8px;
    padding: 16px;
  }
  .select-card:hover {
    cursor: pointer;
  }
</style>