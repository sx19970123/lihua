<template>
  <div :class="props.size"
       :style="{'width':props.width}"
  >
    <div class="scrollbar" style="margin: 0 0 2px 0">
      <a-flex :gap="8" :style="{width: showSearch ? '412px' : 'auto'}" style="margin: 6px 0 6px 0">
        <!--      图标筛选-->
        <a-segmented v-model:value="segmentedValue" :options="segmentedData" @change="handleQueryIcons"/>
        <a-input placeholder="筛选图标"
                 v-if="showSearch"
                 style="max-width: 140px"
                 @change="handleQueryIcons"
                 v-model:value="searchKeyword"
                 allow-clear
        />
        <a-button @click="showSearch = !showSearch">
          <template #icon>
            <ZoomOutOutlined v-if="showSearch"/>
            <SearchOutlined v-else/>
          </template>
        </a-button>
      </a-flex>
    </div>
<!--    四种类型图标切换-->
    <div :style="{maxHeight: props.maxHeight}" class="scrollbar">
      <a-flex :gap="8" wrap="wrap">
<!--        线框-->
        <div class="icon-group"
             :class="icon === modelValue ? 'icon-active' : ''"
             v-show="segmentedValue === 'outlined'"
             v-for="(icon, index) in outlinedIconList"
             @click="clickIcon(icon)"
             :data-index="index"
             type="outlined"
             ref="outlinedIconRef"
        >
          <a-flex vertical align="center" v-if="outlinedVisibleList[index]">
            <component class="icon-size" :is="icon"/>
            <div class="text-ellipsis" v-if="props.size !== 'small'">
              <div>
                <span>{{ icon.substring(0, icon.toLowerCase().indexOf(searchKeyword.toLowerCase())) }}</span>
                <span :style="{ color: icon === modelValue ? '#1f1f1f' : themeStore.getColorPrimary() }">{{ icon.substring(icon.toLowerCase().indexOf(searchKeyword.toLowerCase()), icon.toLowerCase().indexOf(searchKeyword.toLowerCase()) + searchKeyword.length) }}</span>
                <span>{{ icon.substring(icon.toLowerCase().indexOf(searchKeyword.toLowerCase()) + searchKeyword.length) }}</span>
              </div>
            </div>
          </a-flex>
        </div>
<!--        实底-->
        <div class="icon-group"
             :class="icon === modelValue ? 'icon-active' : ''"
             v-show="segmentedValue === 'filled'"
             v-for="(icon, index) in filledIconList"
             @click="clickIcon(icon)"
             :data-index="index"
             type="filled"
             ref="filledIconRef"
        >
          <a-flex vertical align="center" v-if="filledVisibleList[index]">
            <component class="icon-size" :is="icon"/>
            <div class="text-ellipsis" v-if="props.size !== 'small'">
              <span>{{ icon.substring(0, icon.toLowerCase().indexOf(searchKeyword.toLowerCase())) }}</span>
              <span :style="{ color: icon === modelValue ? '#1f1f1f' : themeStore.getColorPrimary() }">{{ icon.substring(icon.toLowerCase().indexOf(searchKeyword.toLowerCase()), icon.toLowerCase().indexOf(searchKeyword.toLowerCase()) + searchKeyword.length) }}</span>
              <span>{{ icon.substring(icon.toLowerCase().indexOf(searchKeyword.toLowerCase()) + searchKeyword.length) }}</span>
            </div>
          </a-flex>
        </div>
<!--        拼色-->
        <div class="icon-group"
             :class="icon === modelValue ? 'icon-active' : ''"
             v-show="segmentedValue === 'twoTone'"
             v-for="(icon, index) in twoToneIconList"
             @click="clickIcon(icon)"
             :data-index="index"
             type="twoTone"
             ref="twoToneIconRef"
        >
          <a-flex vertical align="center" v-if="twoToneVisibleList[index]">
            <component class="icon-size" :is="icon"/>
            <div class="text-ellipsis" v-if="props.size !== 'small'">
              <span>{{ icon.substring(0, icon.toLowerCase().indexOf(searchKeyword.toLowerCase())) }}</span>
              <span :style="{ color: icon === modelValue ? '#1f1f1f' : themeStore.getColorPrimary() }">{{ icon.substring(icon.toLowerCase().indexOf(searchKeyword.toLowerCase()), icon.toLowerCase().indexOf(searchKeyword.toLowerCase()) + searchKeyword.length) }}</span>
              <span>{{ icon.substring(icon.toLowerCase().indexOf(searchKeyword.toLowerCase()) + searchKeyword.length) }}</span>
            </div>
          </a-flex>
        </div>
<!--        自定义-->
        <div class="icon-group"
             :class="icon === modelValue ? 'icon-active' : ''"
             v-show="segmentedValue === 'custom'"
             v-for="(icon, index) in customIconLIst"
             @click="clickIcon(icon)"
             :data-index="index"
             type="custom"
             ref="customIconRef"
        >
          <a-flex vertical align="center" v-if="customVisibleList[index]">
            <component class="icon-size" :is="icon"/>
            <div class="text-ellipsis" v-if="props.size !== 'small'">
              <span>{{ icon.substring(0, icon.toLowerCase().indexOf(searchKeyword.toLowerCase())) }}</span>
              <span :style="{ color: icon === modelValue ? '#1f1f1f' : themeStore.getColorPrimary() }">{{ icon.substring(icon.toLowerCase().indexOf(searchKeyword.toLowerCase()), icon.toLowerCase().indexOf(searchKeyword.toLowerCase()) + searchKeyword.length) }}</span>
              <span>{{ icon.substring(icon.toLowerCase().indexOf(searchKeyword.toLowerCase()) + searchKeyword.length) }}</span>
            </div>
          </a-flex>
        </div>
<!--        条件筛选无匹配图标空状态-->
        <a-empty v-if="segmentedValue === 'outlined' && outlinedIconList.length == 0" description="无匹配图标" style="margin: auto"/>
        <a-empty v-if="segmentedValue === 'filled' && filledIconList.length == 0" description="无匹配图标" style="margin: auto"/>
        <a-empty v-if="segmentedValue === 'twoTone' && twoToneIconList.length == 0" description="无匹配图标" style="margin: auto"/>
        <a-empty v-if="segmentedValue === 'custom' && customIconLIst.length == 0" description="无匹配图标" style="margin: auto"/>
      </a-flex>
    </div>
  </div>
</template>

<script setup lang="ts">
import {type Component, nextTick, onMounted, onUnmounted, type PropType, reactive, ref, useTemplateRef} from "vue";
import * as Icons from "@ant-design/icons-vue";
import {cloneDeep} from "lodash-es";
import {useThemeStore} from "@/stores/theme.ts";
const themeStore = useThemeStore();
// 读取icon目录下图标(../icon/目录名/组件名.vue)
const modules = import.meta.glob("../icon/*/*.vue")

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
// 显示图标搜索框
const showSearch = ref<boolean>(false)
// 图标搜索关键字
const searchKeyword = ref<string>('')
// 图标类型筛选
const segmentedData = reactive([{
  label: '线框',
  value: 'outlined'
},{
  label: '实底',
  value: 'filled'
},{
  label: '双色',
  value: 'twoTone'
},{
  label: '自定义',
  value: 'custom'
}]);
const segmentedValue = ref(segmentedData[0].value);

// v-modal
const emits = defineEmits(['update:modelValue','click'])

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

const finalOutlinedIconList: string[] = []
const finalFilledIconList: string[] = []
const finalTwoToneIconList: string[] = []
const finalCustomIconLIst: string[] = []

// 初始化三种类型图标集合
for (let iconKey in icons) {
  if (icons[iconKey].name === 'create') {
    break
  }

  if (iconKey.endsWith('Outlined')) {
    outlinedIconList.value.push(iconKey)
    finalOutlinedIconList.push(iconKey)
  }
  if (iconKey.endsWith('Filled')) {
    filledIconList.value.push(iconKey)
    finalFilledIconList.push(iconKey)
  }
  if (iconKey.endsWith('TwoTone')) {
    twoToneIconList.value.push(iconKey)
    finalTwoToneIconList.push(iconKey)
  }
}


// 初始化自定义图标集合
for (let path in modules) {
  const match = path.match(/\/([^/]+)\.vue$/)
  if (match) {
    customIconLIst.value.push(match[1])
    finalCustomIconLIst.push(match[1])
  }
}

// v-modal 双向绑定
const clickIcon = (icon: string | null) => {
  if (props.modelValue === icon) {
    icon = null
  }
  const cloneIcon = cloneDeep(icon)
  emits('update:modelValue', cloneIcon)
  emits('click', cloneIcon)
}

// 筛选图标，筛选完成后重置懒加载
const handleQueryIcons = () => {
  const keyword = searchKeyword.value
  switch (segmentedValue.value) {
    case 'filled': {
      filledIconList.value = finalFilledIconList.filter(item => item.toLowerCase().indexOf(keyword.toLowerCase()) !== -1)
      nextTick(() => { initLazy(filledIconRefList.value)})
      break
    }
    case 'outlined': {
      outlinedIconList.value = finalOutlinedIconList.filter(item => item.toLowerCase().indexOf(keyword.toLowerCase()) !== -1)
      nextTick(() => { initLazy(outlinedIconRefList.value)})
      break
    }
    case 'twoTone': {
      twoToneIconList.value = finalTwoToneIconList.filter(item => item.toLowerCase().indexOf(keyword.toLowerCase()) !== -1)
      nextTick(() => { initLazy(twoToneIconRefList.value)})
      break
    }
    case 'custom': {
      customIconLIst.value = finalCustomIconLIst.filter(item => item.toLowerCase().indexOf(keyword.toLowerCase()) !== -1)
      nextTick(() => { initLazy(customIconRefList.value)})
      break
    }
  }
}

// 懒加载控制图标显示隐藏
// 实底
const filledVisibleList = ref(Array(filledIconList.value.length).fill(false))
// 线框
const outlinedVisibleList = ref(Array(outlinedIconList.value.length).fill(false))
// 双色
const twoToneVisibleList = ref(Array(twoToneIconList.value.length).fill(false))
// 自定义
const customVisibleList = ref(Array(customIconLIst.value.length).fill(false))

// 获取每种图标类型的dom集合
// 实底
const filledIconRefList = useTemplateRef<HTMLDivElement[]>("filledIconRef")
// 线框
const outlinedIconRefList = useTemplateRef<HTMLDivElement[]>("outlinedIconRef")
// 双色
const twoToneIconRefList = useTemplateRef<HTMLDivElement[]>("twoToneIconRef")
// 自定义
const customIconRefList = useTemplateRef<HTMLDivElement[]>("customIconRef")
// 懒加载观察器
let observer: IntersectionObserver
// 懒加载初始化
const initLazy = (observeDomList: HTMLDivElement[] | null) => {
  // 创建一个视口监视器，监视 icon 中元素是否进入视口
  if (!observer) {
    observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        // 获取dom元素
        const e = entry.target as HTMLDivElement
        // 获取dom索引
        const i = e.dataset.index
        // 获取自定义dom type
        const iconType = e.getAttribute("type")
        // 通过控制VisibleList对应index元素，控制是否显示, entry.isIntersecting为元素是否出现在视口，boolean类型
        switch (iconType) {
          case "filled": {
            filledVisibleList.value.splice(Number(i), 1, entry.isIntersecting);
            break
          }
          case "outlined": {
            outlinedVisibleList.value.splice(Number(i), 1, entry.isIntersecting);
            break
          }
          case "twoTone": {
            twoToneVisibleList.value.splice(Number(i), 1, entry.isIntersecting);
            break
          }
          case "custom": {
            customVisibleList.value.splice(Number(i), 1, entry.isIntersecting);
            break
          }
        }
      })
    })
  }

  // 对传入的dom元素进行观察
  if (observeDomList) {
    observeDomList.forEach(e => {
      observer.observe(e)
    })
  }
}

// 组件初始化完成
onMounted(() => {
  initLazy(outlinedIconRefList.value)
})

// 组件卸载消除所有观察
onUnmounted(() => {
  if (observer) {
    observer.disconnect()
  }
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
