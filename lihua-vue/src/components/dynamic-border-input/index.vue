<template>
  <div>
    <a-input class="input"
             size="large"
             v-model:value="value"
             :style="{'border-color': showBordered || isGetFocus ? themeStore.getColorPrimary() : 'rgba(0,0,0,0)'}"
             :readonly="loading"
             :id="'dynamic-border-input-' + uuid"
             @mouseover="handleMouseOver"
             @mouseout="handleMouseOut"
             @change="handleValueChange"

    >
      <template #suffix>
        <a-button :icon="h(CheckOutlined)"
                  :loading="loading"
                  type="text"
                  size="small"
                  :style="{color: themeStore.getColorPrimary()}" v-if="isGetFocus"
                  :id="uuid"
        />
        <EditOutlined class="input-prefix-icon-color" v-else/>
      </template>
    </a-input>
  </div>
</template>

<script setup lang="ts">
import {h, nextTick, onMounted, onUnmounted, ref} from "vue";
import {useThemeStore} from "@/stores/theme.ts";
import {CheckOutlined} from "@ant-design/icons-vue";
import {v4 as uuidv4} from "uuid";
const themeStore = useThemeStore()
// 双向绑定
const props = defineProps({
  modelValue: {
    type: String,
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
    if (element.value) {
      element.value.blur()
    }
  },
  // 失败关闭loading
  error: () => {
    loading.value = false
  }
})
// uuid 组件唯一值
const uuid = uuidv4()
// 初始的双向绑定值
const finalValue = ref<string|undefined>(props.modelValue)
// 双向绑定值
const value = ref<string|undefined>(props.modelValue)
// 提交保存loading
const loading = ref<boolean>(false)
const element = ref<HTMLElement | null>(null)

// 处理双向绑定
const handleValueChange = () => {
  emits('update:modelValue', value)
}
// 点击提交按钮
const handleSubmit = () => {
  loading.value = true
  // 点击提交按钮让组件一直处于聚焦状态
  if (element.value) {
    element.value.focus()
    nextTick(() => handleFocus())
  }
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
const handleBlur = (event: FocusEvent) => {
  const element = event.relatedTarget as HTMLElement
  // 点击提交按钮
  if (element && element.id === uuid) {
    handleSubmit()
  }
  // 点击了外部
  else {
    if (!loading.value) {
      value.value = finalValue.value
      handleValueChange()
    }
  }
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

onMounted(() => {
  // 获取组件元素，添加聚焦失焦事件
  element.value = document.getElementById("dynamic-border-input-" + uuid)
  if (element.value) {
    // 聚焦
    element.value.addEventListener("focusin", () => handleFocus())
    // 失焦
    element.value.addEventListener("focusout", (event) => handleBlur(event))
  }
})

onUnmounted(() => {
  // 移除聚焦失焦事件
  if (element.value) {
    // 聚焦
    element.value.removeEventListener("focusin", () => handleFocus())
    // 失焦
    element.value.removeEventListener("focusout", (event) => handleBlur(event))
  }
})
</script>

<style scoped>
.input {
  max-width: 370px;
  text-align: right;
}
</style>