<template>
  <Logo/>
  <a-menu
      :items="items"
      mode="inline"
      theme="dark"
      @click="onTitleClick"
  />
</template>

<script setup lang="ts">
import {h, reactive, ref} from "vue";
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  PieChartOutlined,
  MailOutlined,
  DesktopOutlined,
  InboxOutlined,
  AppstoreOutlined,
} from '@ant-design/icons-vue';
import Logo from "@/layout/sider/components/Logo.vue";

const onTitleClick = ({ key, domEvent }) => {
}
const expandIcon = () => {
  return h(AppstoreOutlined)
}

const items = reactive([
  {
    key: '1',
    icon: () => h(PieChartOutlined),
    label: 'Option 1',
    title: 'Option 1',
    danger: true, // 错误的红色样式
    disabled: true // 禁用
  },
  {
    key: 'sub1',
    icon: () => h(MailOutlined),
    label: 'Navigation One',
    title: 'Navigation One',
    // type: 'group', // type 指定为group时，不为折叠菜单，label作为组名
    // children 下面是对 SubMenuType 的控制
    children: [
      {
        key: '5',
        label: 'Option 5',
        title: 'Option 5',
        disabled: false,
        icon: ()=> h(DesktopOutlined),
        children: [
          {
            key: '15',
            label: 'Option 5',
            title: 'Option 5',
            disabled: false,
            icon: ()=> h(DesktopOutlined)
          },
          {
            type: 'divider' // 分割选 只能在 SubMenuType 中使用，并且暗色主题看不出来
          },
          {
            key: '61',
            label: 'Option 6',
            title: 'Option 6',
          },
          {
            key: '17',
            label: 'Option 7',
            title: 'Option 7',
          },
          {
            key: '18',
            label: 'Option 8',
            title: 'Option 8',
          },
        ],
      },
      {
        type: 'divider' // 分割选 只能在 SubMenuType 中使用，并且暗色主题看不出来
      },
      {
        key: '6',
        label: 'Option 6',
        title: 'Option 6',
      },
      {
        key: '7',
        label: 'Option 7',
        title: 'Option 7',
      },
      {
        key: '8',
        label: 'Option 8',
        title: 'Option 8',
      },
    ],
  }
]);


</script>

<style scoped>

</style>
