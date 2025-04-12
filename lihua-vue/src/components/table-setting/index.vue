<template>
  <div class="outermost" :id="tableSettingKey">
    <a-popover v-model:open="visiblePopover" @openChange="changePopover" placement="bottomRight" :arrow="false" trigger="click" :overlayStyle="{'z-index': '2'}">
      <template #content>
        <!--         header-->
        <a-flex class="header unselectable" align="center" :gap="8">
          <a-checkbox class="all-select"
                      v-model:checked="checkboxState.allChecked"
                      :indeterminate="checkboxState.indeterminate"
                      @change="checkAllChange"
          >全选</a-checkbox>
          <a-button type="link" class="reset" @click="reset">重置</a-button>
        </a-flex>
        <a-divider class="divider"/>
        <!--         content-->
        <a-flex vertical :gap="8" class="scrollbar unselectable content" :style="{width: enableWidthSetting? '300px': '200px', maxHeight: '300px'}">
          <!--           拖拽组件-->
          <VueDraggable v-model="tableSettings"
                        :animation="150"
                        handle=".drag"
                        @end="dragEnd">
            <div v-for="(tableSetting, index) in tableSettings">
              <a-flex align="center" :gap="8" :key="index">
                <!--              拖动图标-->
                <HolderOutlined class="drag"/>
                <!--              是否显示-->
                <a-checkbox v-model:checked="tableSetting.display" @change="updateAllCheckedStatus">
                  <div style="width: 60px">
                    <a-tooltip :title="tableSetting.label">
                      <a-typography-text v-model:content="tableSetting.label" ellipsis/>
                    </a-tooltip>
                  </div>
                </a-checkbox>
                <a-flex class="right-content" :gap="8">
                  <!--              宽度控制-->
                  <a-slider class="slider"
                            v-model:value="tableSetting.width"
                            :min="minWidth"
                            :max="maxWidth"
                            v-show="tableSetting.display"
                            v-if="enableWidthSetting"
                            @change="changeSlide(index)"
                  />
                  <!--              左固定-->
                  <a-rate :count="1" v-model:value="tableSetting.leftFixed" @change="(value: number) => changeLeftFixed(value, index)" :style="{color: themeStore.getColorPrimary()}">
                    <template #character>
                      <PushpinOutlined />
                    </template>
                  </a-rate>
                  <!--              右固定-->
                  <a-rate :count="1" v-model:value="tableSetting.rightFixed"  @change="(value: number) => changeRightFixed(value, index)" :style="{color: themeStore.getColorPrimary()}">
                    <template #character>
                      <PushpinOutlined class="right-rate-icon"/>
                    </template>
                  </a-rate>
                </a-flex>
              </a-flex>
            </div>
          </VueDraggable>
        </a-flex>
      </template>
      <a-button>
        <template #icon>
          <SettingOutlined />
        </template>
      </a-button>
    </a-popover>
  </div>
</template>
<script setup lang="ts">
import {nextTick, onMounted, reactive, ref, watch} from "vue";
import type {ColumnGroupType, ColumnsType, ColumnType} from "ant-design-vue/es/table/interface";
import {type DraggableEvent, VueDraggable} from 'vue-draggable-plus'
import {useThemeStore} from "@/stores/theme.ts";
import {useRouter} from "vue-router";
import {debounce} from "@/utils/Debounce.ts";
import {cloneDeep} from "lodash-es"
import {message} from "ant-design-vue";
const themeStore = useThemeStore();
const router = useRouter();
// 控制弹出卡片是否显示
const visiblePopover = ref<Boolean>(false)
// 组件参数
const {modelValue, minWidth = 80, maxWidth = 400, settingKey = ""} = defineProps<{
  // 宽度调节最小值
  minWidth?: number,
  // 宽度调节最大值
  maxWidth?: number,
  // 双向绑定
  modelValue: ColumnsType | ColumnType[],
  // 组件唯一标识，同一vue组件中有多个table时区分使用
  settingKey?: string
}>()
const emit = defineEmits(['update:modelValue']);
// 组件名称
const componentName = router.currentRoute.value.name as string
// 表格设置key
const tableSettingKey = `table-setting-${componentName}-${settingKey}`
// 开启宽度控制
const enableWidthSetting = ref<boolean>(false)

// 创建TableSetting
type TableSettingType = {
  // 标签
  label: string,
  // 是否显示
  display: boolean,
  // 排序
  sort: number,
  // 宽度
  width: number,
  // 默认宽度
  defaultWidth: number,
  // 设置了默认宽度
  setDefaultWidth: boolean,
  // 宽度是否发生了变化
  widthChanged: boolean,
  // 左固定 0 1
  leftFixed: number,
  // 右固定 0 1
  rightFixed: number,
  // key值
  key: string,
  // 存在子节点（多级表头）
  hasChildren: boolean,
}

// 初始化组件
const init = () => {
  // 拿到modelValue，克隆一份原版，获得默认配置
  const defaultSettings: ColumnType[] = cloneDeep(modelValue)
  const tableSettings = ref<TableSettingType[]>([])

  const initTableSettings = () => {
    // 从 localStorage 中获取数据，无数据再通过VModel获取
    const initComplete = initFromLocal()
    if (!initComplete) {
      initFromVModel()
    }
  }

  // 根据VModel初始化赋值，不包含列表宽度
  const initFromVModel = () => {
    tableSettings.value = []
    defaultSettings.forEach((setting, index: number) => {
      const width = typeof setting.width === "string" ? Number.parseInt(setting.width.replace("px", "")) : setting.width
      const tableSetting = {
        label: typeof setting.title === 'string' ? setting.title : "无法识别的title",
        display: true,
        sort: index,
        width: width ? width : 0,
        defaultWidth: width ? width : 0,
        setDefaultWidth: !!width,
        widthChanged: false,
        leftFixed: setting.fixed === true || setting.fixed === 'left' ? 1 : 0,
        rightFixed: setting.fixed === 'right' ? 1 : 0,
        key: typeof setting.key === 'string' ? setting.key : "",
        hasChildren: (setting as ColumnGroupType<any>).children && (setting as ColumnGroupType<any>).children.length > 0
      }
      tableSettings.value.push(tableSetting)

      if (tableSetting.key === "") {
        console.error("title：" + setting.title, "中column key值为空，请检查配置")
      }
    })
  }

  // 从localStorage中取出列表设置值
  const initFromLocal = (): boolean => {
    const localStorageSetting = localStorage.getItem(tableSettingKey)
    if (localStorageSetting) {
      tableSettings.value = JSON.parse(localStorageSetting)
      // 处理双向绑定
      updateModelValue()
      return true;
    }
    return false;
  }

  return {
    defaultSettings,
    tableSettings,
    initTableSettings,
    initFromLocal
  }
}
const {defaultSettings, tableSettings, initTableSettings, initFromLocal} = init()

// 初始化checkbox相关操作
const initCheckbox = () => {
  const checkboxState = reactive<{
    allChecked: boolean,          // 全选
    indeterminate: boolean        // 非全选
  }>({
    indeterminate: false,
    allChecked: true
  })

  // 全选/全不选
  const checkAllChange = () => {
    checkboxState.indeterminate = false
    tableSettings.value.forEach(setting => setting.display = checkboxState.allChecked)
    // 重新计算宽度
    nextTick(() => debounceWidth())
  }

  // 更新全选状态
  const updateAllCheckedStatus = () => {
    const hiddenList = tableSettings.value.filter(setting => !setting.display)
    if (hiddenList.length === tableSettings.value.length) {
      // 隐藏数量与tableSettings总数相等，状态全为false
      checkboxState.indeterminate = false
      checkboxState.allChecked = false
    } else if (hiddenList.length > 0) {
      // 有部分隐藏，全选为false，indeterminate为true
      checkboxState.indeterminate = true
      checkboxState.allChecked = false
    } else {
      // 没有隐藏，全选为true，indeterminate为false
      checkboxState.indeterminate = false
      checkboxState.allChecked = true
    }
    // 重新计算宽度
    nextTick(() => debounceWidth())
  }
  return {
    checkboxState,
    checkAllChange,
    updateAllCheckedStatus
  }
}
const {checkboxState, checkAllChange, updateAllCheckedStatus} = initCheckbox()

// 重置列表
const reset = () => {
  emit("update:modelValue", defaultSettings)
  localStorage.removeItem(tableSettingKey)
  visiblePopover.value = false
  enableWidthSetting.value = false
  message.success("重置完成")
}

// 处理双向绑定
const updateModelValue = () => {
  const vModelColumn:ColumnType[] = []
  tableSettings.value.filter(setting => setting.display).forEach(outSetting => {
    cloneDeep(defaultSettings).forEach((innerSetting: ColumnType) => {
      if (outSetting.key === innerSetting.key) {
        innerSetting.width = outSetting.width !== outSetting.defaultWidth ? outSetting.width + 'px' : innerSetting.width
        innerSetting.fixed = outSetting.rightFixed === 1 ? 'right' : outSetting.leftFixed === 1 ? 'left' : false
        vModelColumn.push(innerSetting)
      }
    })
  })
  // 保存到localStorage
  localStorage.setItem(tableSettingKey, JSON.stringify(tableSettings.value))
  emit("update:modelValue", vModelColumn)
}

// 初始化处理左右固定和固定时调整顺序相关函数
const initChangeFixedDrag = () => {
  // 查找符合条件的目标索引
  const findTargetIndex = (filterFn: (ts: TableSettingType) => boolean, last: boolean) => {
    const tss = tableSettings.value
    const targetSettings = tss.filter(filterFn)
    if (targetSettings.length > 0) {
      const targetItem = last ? targetSettings[targetSettings.length - 1] : targetSettings[0]
      return tss.findIndex(item => item === targetItem)
    }
    return -1
  }

  // 处理左固定，点击左固定判断前一个元素是否为左固定，不是的话移动到最近的左固定元素后
  const changeLeftFixed = (value: number, index: number) => {
    const tss = tableSettings.value
    const tableSetting = tss[index]

    if (value === 1) {
      // 左固定，取消右固定
      tableSetting.rightFixed = 0
      const prevItem = tss[index - 1]

      if (prevItem && prevItem.leftFixed !== value) {
        const targetIndex = findTargetIndex(ts => ts.leftFixed === value && ts !== tableSetting, true)
        moveTableSettingItemToAfter(index, targetIndex)
      }
    } else {
      // 取消左固定
      const nextItem = tss[index + 1]

      if (nextItem && nextItem.leftFixed !== value) {
        const targetIndex = findTargetIndex(ts => ts.leftFixed === value && ts !== tableSetting, false)
        moveTableSettingItemToBefore(index, targetIndex !== -1 ? targetIndex : tss.length)
      }
    }
  }

  // 处理右固定，点击右固定判断后一个元素是否为右固定，不是的话移动到最近的右固定元素前
  const changeRightFixed = (value: number, index: number) => {
    const tss = tableSettings.value
    const tableSetting = tss[index]

    if (value === 1) {
      // 右固定，取消左固定
      tableSetting.leftFixed = 0
      const nextItem = tss[index + 1]

      if (nextItem && nextItem.rightFixed !== value) {
        const targetIndex = findTargetIndex(ts => ts.rightFixed === value && ts !== tableSetting, false)
        moveTableSettingItemToBefore(index, targetIndex !== -1 ? targetIndex : tss.length)
      }
    } else {
      // 取消右固定
      const prevItem = tss[index - 1]

      if (prevItem && prevItem.rightFixed !== value) {
        const targetIndex = findTargetIndex(ts => ts.rightFixed === value && ts !== tableSetting, true)
        moveTableSettingItemToAfter(index, targetIndex)
      }
    }
  }

  // 将fromIndex位置的元素移动到toIndex的后面
  const moveTableSettingItemToAfter = (fromIndex: number, toIndex: number) => {
    const arr = tableSettings.value
    // 边界检查
    if (fromIndex < -1 || toIndex < -1 || fromIndex >= arr.length || toIndex >= arr.length) return;

    // 移动元素
    const [movedItem] = arr.splice(fromIndex, 1);
    arr.splice(toIndex + 1, 0, movedItem);
  }
  // 将fromIndex位置的元素移动到toIndex的前面
  const moveTableSettingItemToBefore = (fromIndex: number, toIndex: number) => {
    const arr = tableSettings.value
    // 边界检查
    if (fromIndex < 0 || toIndex < 0 || fromIndex >= arr.length + 1 || toIndex >= arr.length + 1) return;

    // 移动元素
    const [movedItem] = arr.splice(fromIndex, 1);
    arr.splice(toIndex -1, 0, movedItem);
  }

  // 拖拽排序结束后进行位置判断
  const dragEnd = (event: DraggableEvent & {newIndex: number, oldIndex: number}) => {
    const tss = tableSettings.value
    // 移动后的坐标
    const newIndex = event.newIndex
    // 被移动的元素
    const dragItem = tss[newIndex]
    // 被移动元素的左右固定
    const leftFixed = dragItem.leftFixed
    const rightFixed = dragItem.rightFixed

    // 元素没有被固定
    if (leftFixed === 0 && rightFixed === 0) {
      // 左固定最后一个元素index
      const leftFixedLastIndex = findTargetIndex(ts => ts.leftFixed === 1, true);
      // 右固定第一个元素index
      const rightFixedFirstIndex = findTargetIndex(ts => ts.rightFixed === 1, false);
      // 元素落在左固定元素中
      if (leftFixedLastIndex != -1 && leftFixedLastIndex > newIndex) {
        moveTableSettingItemToAfter(newIndex, leftFixedLastIndex - 1)
        message.warn("无法移动至固定元素左边")
      }
      // 元素落在右固定元素中
      if (rightFixedFirstIndex != -1 && newIndex > rightFixedFirstIndex) {
        moveTableSettingItemToBefore(newIndex, rightFixedFirstIndex + 1)
        message.warn("无法移动至固定元素右边")
      }
    }

    // 左固定元素跑外面的情况
    if (leftFixed === 1) {
      const leftFixedLastIndex = findTargetIndex(ts => ts.leftFixed === 1 && ts !== dragItem, true);
      if (newIndex - 1 > leftFixedLastIndex) {
        moveTableSettingItemToAfter(newIndex, leftFixedLastIndex)
        message.warn("左固定元素无法移动至右边")
      }
    }

    // 元素被右固定
    if (rightFixed === 1) {
      const rightFixedFirstIndex = findTargetIndex(ts => ts.rightFixed === 1 && ts !== dragItem, false);
      // 只有自己被右固定的情况
      if (rightFixedFirstIndex === -1) {
        moveTableSettingItemToBefore(newIndex, tss.length)
        message.warn("右固定元素无法移动至左边")
      }
      // 右固定元素跑外面的情况
      if (rightFixedFirstIndex - 1 > newIndex) {
        moveTableSettingItemToBefore(newIndex, rightFixedFirstIndex)
        message.warn("右固定元素无法移动至左边")
      }
    }
  }

  return {
    changeLeftFixed,
    changeRightFixed,
    dragEnd
  }
}

const {changeLeftFixed, changeRightFixed, dragEnd} = initChangeFixedDrag()


type ColumnWidthType = {
  // 表格标题
  text: string,
  // 表格宽度
  width: number
}

// 初始化列宽度集合，通过dom节点获取表格对应宽度
const initColumnWidth = () => {
  // 不可调节宽度情况下直接返回
  if (tableSettings.value.filter(ts => ts.hasChildren).length > 0) {
    return;
  }
  // 列表宽度集合
  const columnWidthList: ColumnWidthType[] = []
  // 通过id获取当前组件元素
  const element = document.getElementById(tableSettingKey)
  if (element) {
    // 获取祖先节点.ant-table
    const target = element.closest(".ant-table")
    if (target) {
      // 获取匹配到的第一个tr标签
      const tr = target.querySelector("tr")
      if (tr) {
        // 找到所有tr标签
        const allTh = tr.querySelectorAll("th")
        allTh.forEach(el => {
          // 元素宽度
          const width = el.offsetWidth
          // 元素文本
          const text = el.textContent
          if (text) {
            columnWidthList.push({ text: text, width:width })
          }
        })
      }
    }
  }
  // 当dom元素中的列数量与tableSettings长度相同时，允许控制列宽度
  const targetTableSettings = tableSettings.value.filter(item => item.display)
  enableWidthSetting.value = targetTableSettings.length === columnWidthList.length
  // columnWidthList
  if (enableWidthSetting.value) {
    for (let i = 0; i < columnWidthList.length; i++) {
      const cw = columnWidthList[i]     // 列宽-label集合
      const ts = targetTableSettings[i] // 操作的主要配置
      // 同一索引判断名称是否相同
      if (cw.text === ts.label) {
        // 首次加载或未调整过宽度，在窗口大小变化时，为width、defaultWidth赋初值
        if (!ts.setDefaultWidth && !ts.widthChanged){
          ts.width = cw.width
          ts.defaultWidth = cw.width
        }
      } else {
        console.error("dom中获取名称与prop中不同")
        enableWidthSetting.value = false
        return
      }
    }
  }
}

// 处理防抖
const debounceWidth = debounce(initColumnWidth)

// visible显示时才进行加载，关闭时销毁监听
const changePopover = (visible: boolean) => {
  // enableWidthSetting 为false时加载一次初始化宽度
  if (!enableWidthSetting.value) {
    initTableSettings()
  }
  if (visible) {
    initColumnWidth()
    window.addEventListener("resize", debounceWidth)
  } else {
    window.removeEventListener("resize", debounceWidth)
  }
}

// 拖动宽度条
const changeSlide = (index: number) => {
  // 拖动宽度条标记宽度已改变
  const target = tableSettings.value[index]
  if (!target.widthChanged && target.width != maxWidth) {
    target.widthChanged = true
  }
  debounceWidth()
}

// 组件加载完成后先从localStorage读取表头数据
onMounted(() => {
  initFromLocal()
})

// 监听tableSettings的变化
watch(() => tableSettings.value, (newVal, oldValue) => {
  // oldValue值变化时才会触发双向绑定改变表格配置
  if (oldValue.length > 0) {
    updateModelValue()
  }
},{deep: true})
</script>

<style scoped>
.outermost {
  margin-left: auto
}
.all-select {
  margin-left: 22px
}
.reset {
  margin-left: auto
}
.divider {
  margin: 4px 0 4px 0
}
.content {
  overflow-x: hidden;
  padding-bottom: 2px;
}
.right-content {
  margin-left: auto;
}
.slider {
  width: 100px;
  margin: auto
}
.drag:hover {
  color: var(--colorPrimary);
  cursor: grab;
}
.drag:active {
  cursor: grabbing;
}
.right-rate-icon {
  transform: scaleX(-1);
  margin-right: 8px
}
</style>
