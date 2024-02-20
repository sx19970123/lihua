<template>
  <div>
    <a-flex :gap="8">
<!--      图标筛选-->
      <a-segmented v-model:value="segmentedValue" :options="segmentedData"/>
    </a-flex>
<!--    三种类型图标切换-->
    <div style="max-height: 400px;" class="scrollbar">
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
      </a-flex>
    </div>
  </div>
</template>

<script setup lang="ts">
import {type Component, reactive, ref} from "vue";
import * as Icons from "@ant-design/icons-vue";
const icons: Record<string, Component> = Icons
// 三种图标类型集合
// 实底
const filledIconList = ref<string[]>([])
// 线框
const outlinedIconList = ref<string[]>([])
// 双色
const twoToneIconList = ref<string[]>([])

// 图标类型筛选
const segmentedData = reactive(['线框','实底','双色']);
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

// v-modal 双向绑定
const clickIcon = (icon: string) => {
  emits('update:modelValue',icon)
}

</script>
<style>
.icon-group {
  padding: 10px;
  border-radius: 6px;
  width: 180px;
  height: 80px;
  transition: all 0.2s ease; /* 添加过渡效果 */
}
.icon-group:hover {
  padding: 10px;
  border-radius: 6px;
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



[data-color = 'rgb(22, 119, 255)'] {
  .icon-group:hover {
    background: rgb(22, 119, 255);
  }
  .icon-active {
    background: rgb(22, 119, 255);
    color: #ffffff;
  }
}

[data-color = 'rgb(245, 34, 45)'] {
  .icon-group:hover {
    background: rgb(245, 34, 45);
  }
  .icon-active {
    background: rgb(245, 34, 45);
    color: #ffffff;
  }
}

[data-color = 'rgb(250, 84, 28)'] {
  .icon-group:hover {
    background: rgb(250, 84, 28);
  }
  .icon-active {
    background: rgb(250, 84, 28);
    color: #ffffff;
  }
}

[data-color = 'rgb(250, 173, 20)'] {
  .icon-group:hover {
    background: rgb(250, 173, 20);
  }
  .icon-active {
    background: rgb(250, 173, 20);
    color: #ffffff;
  }
}

[data-color = 'rgb(19, 194, 194)'] {
  .icon-group:hover {
    background: rgb(19, 194, 194);
  }
  .icon-active {
    background: rgb(19, 194, 194);
    color: #ffffff;
  }
}

[data-color = 'rgb(82, 196, 26)'] {
  .icon-group:hover {
    background: rgb(82, 196, 26);
  }
  .icon-active {
    background: rgb(82, 196, 26);
    color: #ffffff;
  }
}

[data-color = 'rgb(47, 84, 235)'] {
  .icon-group:hover {
    background: rgb(47, 84, 235);
  }
  .icon-active {
    background: rgb(47, 84, 235);
    color: #ffffff;
  }
}

[data-color = 'rgb(114, 46, 209)'] {
  .icon-group:hover {
    background: rgb(114, 46, 209);
  }
  .icon-active {
    background: rgb(114, 46, 209);
    color: #ffffff;
  }
}

</style>