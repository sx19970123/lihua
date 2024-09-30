<template>
  <a-spin :spinning="loading">
    <a-flex :gap="props.gap" :wrap="props.vertical ? '' : 'wrap'" :vertical="props.vertical" class="scrollbar" :style="{'max-height': props.vertical ? props.maxHeight + 'px' : 'none'}">
      <div :style="props.itemStyle" v-if="props.dataSource && props.dataSource.length > 0" v-for="(item,index) in props.dataSource">
        <div class="select-card"
             @click.stop="handleClickCard(item)"
             :style="item[props.itemKey] && activeCardValueList.includes(item[props.itemKey]) ? bodyStyle : ''">
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
                :color="token.colorPrimary"/>
        </div>
      </div>
      <!--    空状态-->
      <div class="select-card" v-else :style="props.itemStyle">
        <a-empty :description="props.emptyDescription" />
      </div>
    </a-flex>
  </a-spin>

</template>

<script setup lang="ts">
// 接受父组件传递参数
import {reactive, ref, watch, defineProps} from "vue";
import { cloneDeep } from 'lodash-es'
import {theme} from "ant-design-vue";
const {token} = theme.useToken()

// 定义父级传入的配置项
const props = defineProps({
  // 卡片间距
  gap: {
    type: Number,
    default: 16,
  },
  itemStyle: {
    type: Object,
    default: {}
  },
  // 是否垂直排列
  vertical: {
    type: Boolean,
    default: false
  },
  // 最大高度 （仅对垂直排列生效）
  maxHeight: {
    type: Number,
    default: 300
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
  },
  // 空状态描述
  emptyDescription: {
    type: String
  },
  // 加载中
  loading: {
    type: Boolean,
    default: false
  },
  // 定义 v-model
  modelValue: {}
})


// 定义 v-model 双向绑定和抛出的函数
const emit = defineEmits(['update:modelValue','click','change'])

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
    props.multiple ? emit('update:modelValue', cloneDeep(activeCardValueList)) : emit('update:modelValue', null)
    props.multiple ? emit('change', cloneDeep(activeCardValueList)) : emit('change',{})
    return;
  }

  // 处理单选/多选
  if (props.multiple) {
    activeCardValueList.push(keyItem)
    // 多选情况下，返回选中值的集合
    emit('update:modelValue', cloneDeep(activeCardValueList))
  } else {
    clearActiveCardValueList()
    activeCardValueList.push(keyItem)
    // 单选情况下返回选中的值
    emit('update:modelValue', cloneDeep(keyItem))
  }

  // 向父级抛出点击事件
  emit('click',{activeValueList: cloneDeep(activeCardValueList) ,item: cloneDeep(item), props: cloneDeep(props)})
  emit('change',{item: cloneDeep(item)})
}

// 清空选中集合
const clearActiveCardValueList = () => {
  activeCardValueList.length = 0
}

// 处理双向绑定回显
const handleVmodel = () => {
  // 双向绑定modelValue
  const modelValue = props.modelValue;

  if (!modelValue) {
    return;
  }

  const type = Array.isArray(modelValue) ? 'array' : typeof modelValue;
  // 是否多选
  const multiple = props.multiple;

  if (type === 'array') {
    if (!multiple) {
      console.error("错误：双向绑定数据类型不匹配，期望接收单一元素，但实际接收到的是一个数组。");
      return;
    }
    clearActiveCardValueList();
    for (const item of modelValue as Array<any>) {
      activeCardValueList.push(item);
    }

  } else {
    if (multiple) {
      console.error("错误：双向绑定数据类型不匹配，期望接收数组，但实际接收到的是单一元素。");
      return;
    }
    clearActiveCardValueList();
    activeCardValueList.push(modelValue);
  }
}
// 第一次调用
handleVmodel()

// 选中的卡片样式
const bodyStyle = ref<{
  'border': string,
  'box-shadow': string
}>({
  'border': '1px solid ' + token.value.colorPrimary,
  'box-shadow': 'inset 0 0 0 1px ' + token.value.colorPrimary,
})
// 监听主题变化同步卡片样式
watch(() => token.value.colorPrimary, () => {
  bodyStyle.value = {
    'border': '1px solid ' + token.value.colorPrimary,
    'box-shadow': 'inset 0 0 0 1px ' + token.value.colorPrimary,
  }
})

// 监听外部 modelValue 值变化时反应到组件中
watch(() => props.modelValue,() => {
  handleVmodel()
},{deep: true})

// 深度监听 dataSource 变化，当dataSource减少时，删除双向绑定中对应的数据
watch(() => props.dataSource, () => {
  // modelValue 未选中值时不进行操作
  if (!props.modelValue || props.modelValue === 0) {
    // 双向绑定值不存在时，清空已选项
    clearActiveCardValueList()
    return;
  }
  // 双向绑定modelValue
  const type = Array.isArray(props.modelValue) ? 'array' : typeof props.modelValue;
  // 是否多选
  const multiple = props.multiple;
  if (props.dataSource && props.dataSource.length > 0) {
    // 全部可选集合
    const allList = props.dataSource.map(item => item[props.itemKey])
    // 多选情况下
    if (multiple) {
      if (type === 'array') {
        const modelValue = props.modelValue as Array<any>; // 将 props.modelValue 转换为数组
        const isSubset = modelValue.every(item => allList.includes(item));
        if (!isSubset || modelValue.length > allList.length) {
          // 计算 props.modelValue 与 allList 的交集
          const intersection = modelValue.filter(item => allList.includes(item));
          // 清空 props.modelValue 并将交集元素添加回去
          // modelValue.length = 0;
          // modelValue.push(...intersection);
          emit('update:modelValue', cloneDeep(intersection));
        }
      } else {
        console.error("错误：双向绑定数据类型不匹配，期望接收数组，但实际接收到的是单一元素。");
      }
    }
    // 单选情况下
    else {
      if (type === 'array') {
        console.error("错误：双向绑定数据类型不匹配，期望接收单一元素，但实际接收到的是一个数组。");
      } else {
        if (!allList.includes(props.modelValue)) {
          emit('update:modelValue', null)
        }
      }
    }

  } else {
    // dataSource 不存在时直接清空 modelValue
    if (multiple) {
      emit('update:modelValue', [])
    } else {
      emit('update:modelValue', null)
    }
  }

})
</script>

<style>
  [data-theme="light"] {
    .select-card {
      border: 1px solid #d9d9d9;
    }
  }
  [data-theme="dark"] {
    .select-card {
      border: 1px solid #424242;
    }
  }
</style>
<style scoped>
.select-card {
  border-radius: 8px;
  padding: 16px;
  margin-right: 3px;
}
.select-card:hover {
  cursor: pointer;
}
</style>
