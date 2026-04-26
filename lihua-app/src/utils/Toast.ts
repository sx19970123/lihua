/**
 * 显示轻提示
 */
export const toast = (msg: string, duration: number = 1500) => {
	uni.showToast({
		title: msg,
		duration: duration,
		icon: "none",
		position: "bottom"
	})
}