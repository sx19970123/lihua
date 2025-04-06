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
          <VueDraggable v-model="tableSettings" :animation="150" handle=".drag" fallbackClass="drag" ghostClass="drag" dragClass="drag" chosenClass="drag">
            <div v-for="(tableSetting, index) in tableSettings">
              <a-flex align="center" :gap="8" :key="index">
                <!--              拖动图标-->
                <HolderOutlined class="drag"/>
                <!--              是否显示-->
                <a-checkbox v-model:checked="tableSetting.display">
                  <a-typography-text v-model:content="tableSetting.label" ellipsis/>
                </a-checkbox>
                <a-flex class="right-content" :gap="8">
                  <!--              宽度控制-->
                  <a-slider class="slider"
                            v-model:value="tableSetting.width"
                            :marks="{[tableSetting.defaultWidth]: {label: tableSetting.defaultWidth + 'px',style:{fontSize: '12px'}}}"
                            :min="tableSetting.defaultWidth - 100"
                            :max="tableSetting.defaultWidth + 200"
                            v-if="enableWidthSetting"
                            @change="changeSlide"
                  />
                  <!--              左固定-->
                  <a-rate :count="1" v-model:value="tableSetting.leftFixed" :style="{color: themeStore.getColorPrimary()}">
                    <template #character>
                      <PushpinOutlined />
                    </template>
                  </a-rate>
                  <!--              右固定-->
                  <a-rate :count="1" v-model:value="tableSetting.rightFixed" :style="{color: themeStore.getColorPrimary()}">
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
import {onMounted, onUnmounted, reactive, ref, toRef, watch} from "vue";
import type {ColumnsType, ColumnType} from "ant-design-vue/es/table/interface";
import {VueDraggable} from 'vue-draggable-plus'
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
const {modelValue} = defineProps<{
  modelValue: ColumnsType | ColumnType[]
}>()
const emit = defineEmits(['update:modelValue']);
// 组件名称
const componentName = router.currentRoute.value.name as string
// 表格设置key
const tableSettingKey = "table-setting-" + componentName
// 开启宽度控制
const enableWidthSetting = ref<boolean>(false)


// 初始化组件
const init = () => {
  // 拿到modelValue，克隆一份原版，获得默认配置
  const defaultSettings: ColumnType[] = cloneDeep(modelValue)
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
    // 左固定 0 1
    leftFixed: number,
    // 右固定 0 1
    rightFixed: number,
    // key值
    key: string
  }
  const tableSettings = ref<TableSettingType[]>([])

  // 初始化赋值，不包含列表宽度
  const initTableSettings = () => {
    tableSettings.value = []
    defaultSettings.forEach((setting: ColumnType, index: number) => {
      const tableSetting = {
        label: typeof setting.title === 'string' ? setting.title : "无法识别的title",
        display: true,
        sort: index,
        width: 0,
        defaultWidth: 0,
        leftFixed: setting.fixed === true || setting.fixed === 'left' ? 1 : 0,
        rightFixed: setting.fixed === 'right' ? 1 : 0,
        key: typeof setting.key === 'string' ? setting.key : "",
      }
      tableSettings.value.push(tableSetting)

      if (tableSetting.key === "") {
        console.error("title：" + setting.title, "中column key值为空，请检查配置")
      }
    })
  }


  return {
    defaultSettings,
    tableSettings,
    initTableSettings
  }
}
const {defaultSettings, tableSettings, initTableSettings} = init()

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
  initTableSettings()
  // 浏览器绘制一帧后重新计算列宽度
  requestAnimationFrame(() =>  initColumnWidth())
  visiblePopover.value = false
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

  emit("update:modelValue", vModelColumn)
}

type ColumnWidthType = {
  // 表格标题
  text: string,
  // 表格宽度
  width: number
}

// 初始化列宽度集合，通过dom节点获取表格对应宽度
const initColumnWidth = () => {
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
  enableWidthSetting.value = tableSettings.value.length === columnWidthList.length
  // columnWidthList
  if (enableWidthSetting.value) {
    for (let i = 0; i < columnWidthList.length; i++) {
      const ds = defaultSettings[i]     // 最原始的table配置
      const cw = columnWidthList[i]     // 列宽-label集合
      const ts = tableSettings.value[i] // 操作的主要配置
      // 同一索引判断名称是否相同
      if (cw.text === ts.label) {
        // 首次加载或未调整过宽度，在窗口大小变化时，为width、defaultWidth赋初值
        if ((ts.width === 0 && ts.defaultWidth === 0) || ts.width === ts.defaultWidth) {
          ts.width = cw.width
          // 默认赋初值的情况下将初值赋值给tableSettings对应属性
          if (ds.width) {
            const width = typeof ds.width === "string" ? Number.parseInt(ds.width.replace("px", "")) : ds.width
            ts.width = width
            ts.defaultWidth = width
          } else {
            // 没有赋初值时触发表格宽度自动计算时修改tableSettings默认值
            ts.defaultWidth = cw.width
          }
        }
      } else {
        console.error("dom中获取名称与prop中不同")
        enableWidthSetting.value = false
        return
      }
    }
  }
}

// visible显示时才进行加载，关闭时销毁监听
const changePopover = (visible: boolean) => {
  // enableWidthSetting 为false时加载一次初始化宽度
  if (!enableWidthSetting.value) {
    initTableSettings()
    initColumnWidth()
  }
  if (visible) {
    window.addEventListener("resize", debounceWidth)
  } else {
    window.removeEventListener("resize", debounceWidth)
  }
}

// 拖动宽度条
const changeSlide = () => {
  debounceWidth()
}

// 处理防抖
const debounceWidth = debounce(initColumnWidth, 100)

// 监听tableSettings的变化
watch(() => tableSettings.value, () => {
  // 更新全选状态
  updateAllCheckedStatus()
  // 执行双向绑定
  updateModelValue()
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
