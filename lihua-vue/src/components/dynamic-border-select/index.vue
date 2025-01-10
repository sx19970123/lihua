<template>
  <div>
    <a-select v-model:value="value"
              :bordered="isGetFocus || showBordered"
              :field-names="props.fieldNames"
              :options="props.options"
              size="large"
              @focus="handleFocus"
              @blur="handleBlur"
              @mouseenter="handleMouseOver"
              @mouseleave="handleMouseOut"
              @change="handleValueChange"
    >
      <template #suffixIcon><EditOutlined class="input-prefix-icon-color" style="font-size: 16px;"/></template>
    </a-select>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
const props = defineProps({
  // 双向绑定
  modelValue: {
    type: String,
    required: false
  },
  // 下拉值
  options: {
    type: Array,
    required: true
  },
  // 下拉字段属性绑定
  fieldNames: {
    type: Object,
    required: false
  }
})
// 抛出方法
const emits = defineEmits(['update:modelValue','submit']);
// 暴露函数
defineExpose({
  // 提交成功，设置为失焦并关闭loading
  success: () => {
    isGetFocus.value = false
    loading.value = false
  },
  // 失败关闭loading
  error: () => {
    loading.value = false
  }
})
// 双向绑定值
const value = ref<string|undefined>(props.modelValue)
// 提交保存loading
const loading = ref<boolean>(false)
// 处理双向绑定
const handleValueChange = () => {
  emits('update:modelValue', value.value)
  emits('submit', value.value)
}

// 显示边框
const showBordered = ref<boolean>(false)
// 是否获取焦点
const isGetFocus = ref<boolean>(false);
// 聚焦
const handleFocus = () => {
  isGetFocus.value = true
}
// 失焦
const handleBlur = () => {
  isGetFocus.value = false
}
// 鼠标移入
const handleMouseOver = () => {
  showBordered.value = true
}
// 鼠标移出
const handleMouseOut = () => {
  showBordered.value = false
}

</script>
