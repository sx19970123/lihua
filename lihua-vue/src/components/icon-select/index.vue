<template>
  <div>
    <a-flex :gap="8">
      <a-segmented v-model:value="segmentedValue" :options="segmentedData"/>
    </a-flex>
    <div style="max-height: 400px;" class="scrollbar">
      <a-flex :gap="8" wrap="wrap"  style="margin-top: 30px">
        <div class="icon-group" v-if="segmentedValue === '线框'" v-for="icon in outlinedIconList"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div>{{icon}}</div>
          </a-flex>
        </div>
        <div class="icon-group" v-if="segmentedValue === '实底'" v-for="icon in filledIconList"  @click="clickIcon(icon)">
          <a-flex vertical align="center">
            <component class="icon-size" :is="icon"/>
            <div>{{icon}}</div>
          </a-flex>
        </div>
        <div class="icon-group" v-if="segmentedValue === '双色'" v-for="icon in twoToneIconList"  @click="clickIcon(icon)">
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
const filledIconList = ref<string[]>([])
const outlinedIconList = ref<string[]>([])
const twoToneIconList = ref<string[]>([])

const segmentedData = reactive(['线框','实底','双色']);
const segmentedValue = ref(segmentedData[0]);

const emits = defineEmits(['update:modelValue'])


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
}

[data-color = 'rgb(245, 34, 45)'] {
  .icon-group:hover {
    background: rgb(245, 34, 45);
  }
}

[data-color = 'rgb(250, 84, 28)'] {
  .icon-group:hover {
    background: rgb(250, 84, 28);
  }
}

[data-color = 'rgb(250, 173, 20)'] {
  .icon-group:hover {
    background: rgb(250, 173, 20);
  }
}

[data-color = 'rgb(19, 194, 194)'] {
  .icon-group:hover {
    background: rgb(19, 194, 194);
  }
}

[data-color = 'rgb(82, 196, 26)'] {
  .icon-group:hover {
    background: rgb(82, 196, 26);
  }
}

[data-color = 'rgb(47, 84, 235)'] {
  .icon-group:hover {
    background: rgb(47, 84, 235);
  }
}

[data-color = 'rgb(114, 46, 209)'] {
  .icon-group:hover {
    background: rgb(114, 46, 209);
  }
}

</style>