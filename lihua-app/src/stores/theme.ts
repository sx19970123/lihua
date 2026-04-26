import { defineStore } from "pinia";
/**
 * 系统主题store
 */
export const useThemeStore = defineStore('theme', {
	state: () => {
		// 当前模式
		const mode: 'auto' | 'dark' | 'light' = uni.getStorageSync("UIStyle") || 'auto'
		return {
			mode
		}
	},
	actions: {
		// 设置主题模式（仅app支持）
		setMode(modeValue?: 'auto' | 'dark' | 'light') {
			// #ifdef APP
			// 没传入具体值使用mode默认值
			if (!modeValue) {
				plus.nativeUI.setUIStyle(this.mode)
				return
			}
			
			// 设置缓存并应用设置
			uni.setStorageSync("UIStyle", modeValue)
			plus.nativeUI.setUIStyle(modeValue)
			// #endif
		}
	}
})
