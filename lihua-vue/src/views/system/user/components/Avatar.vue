<template>
  <div>
    <a-row align="center">
      <a-avatar :size="64" :style="{background: avatarColor}" @click="open = !open">
        <template v-if="avatarType === 'icon'">
          <component :is="avatarIcon"/>
        </template>
        <template v-if="avatarType === 'text'">
          {{avatarText}}
        </template>
        <template v-if="avatarType === 'upload'">
          {{avatarText}}
        </template>
      </a-avatar>
    </a-row>
    <a-modal v-model:open="open" title="头像编辑" width="1000px">
      <a-flex vertical align="center" :gap="16">
        <a-avatar :size="64" :style="{background: avatarColor}">
          <template v-if="avatarType === 'icon'">
            <component :is="avatarIcon"/>
          </template>
          <template v-if="avatarType === 'text'">
            {{avatarText}}
          </template>
          <template v-if="avatarType === 'upload'">
            {{avatarText}}
          </template>
        </a-avatar>
        <a-radio-group v-model:value="avatarType">
          <a-radio value="icon">图标</a-radio>
          <a-radio value="text">文本</a-radio>
          <a-radio value="upload">上传</a-radio>
        </a-radio-group>
        <!--        颜色选取-->
        <color-select v-model="avatarColor" v-if="avatarType !== 'upload'" :items="avatarBackgroundColor"/>
<!--        图标选取-->
        <icon-select v-if="avatarType === 'icon'" v-model="avatarIcon"/>
<!--        文本编辑-->
        <a-input v-if="avatarType === 'text'" v-model:value="avatarText" style="width: 300px"/>
<!--        头像上传-->
        <a-upload v-if="avatarType === 'upload'"/>
      </a-flex>
    </a-modal>
  </div>


</template>
<script setup lang="ts">
import {reactive, ref} from "vue";
import ColorSelect from "@/components/color-select/index.vue"
import IconSelect from "@/components/icon-select/index.vue"
import {useUserStore} from "@/stores/modules/user";
const userStore = useUserStore()
const open = ref<boolean>(false)
const data = reactive(['图片', '文本']);
const value = ref(data[0]);

const avatarType = ref<string>('icon')

const avatarColor = ref<string>()
const avatarText = ref<string>(userStore.name as string)
const avatarIcon = ref<string>('')

const avatarBackgroundColor = ref<Array<{name: string,color: string}>>([
  {
    name: '灰色',
    color: '#bfbfbf',
  },
  {
    name: '拂晓蓝',
    color: 'rgb(22, 119, 255)',
  },
  {
    name: '薄暮',
    color: 'rgb(245, 34, 45)',
  },
  {
    name: '火山',
    color: 'rgb(250, 84, 28)',
  },
  {
    name: '日暮',
    color: 'rgb(250, 173, 20)',
  },
  {
    name: '明青',
    color: 'rgb(19, 194, 194)',
  },
  {
    name: '极光绿',
    color: 'rgb(82, 196, 26)',
  },
  {
    name: '极客蓝',
    color: 'rgb(47, 84, 235)',
  },
  {
    name: '酱紫',
    color: 'rgb(114, 46, 209)',
  },
])
</script>
<style scoped>

</style>