<template>
  <view class="content">
	  <sar-radio-group v-model="themeStore.$state.mode" @change="themeStore.setMode">
	    <template #custom="{toggle, value}">
	      <sar-list card>
			<!-- 选项列表 -->
	        <sar-list-item v-for="mode in modeOption" :key="mode.value" :title="mode.label" hover @click="toggle(mode.value)">
	          <template #value>
				<!-- 选中标识 -->
	            <sar-icon v-if="mode.value === value" color="var(--sar-primary)" family="outlined" size="32rpx" name="CheckOutlined"/>
	          </template>
	        </sar-list-item>
	      </sar-list>
	    </template>
	  </sar-radio-group>
  </view>
</template>

<script setup lang="ts">
import {useThemeStore} from "@/stores/theme";
const themeStore = useThemeStore()
const modeOption = [
	{
		"label": "跟随系统",
		"value": "auto"
	},
	{
		"label": "暗色模式",
		"value": "dark"
	},{
		"label": "亮色模式",
		"value": "light"
	}
]
</script>