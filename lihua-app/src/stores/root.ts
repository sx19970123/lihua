import { defineStore } from "pinia";
/**
 * 获取根节点实例
 */
export const useRootRefStore = defineStore('rootRef', {
	state: () => {
		const rootRef: any = undefined
		return {
			rootRef
		}
	},
	actions: {
		getRootRef() {
			const pagesStack = getCurrentPages()
			if (pagesStack.length > 0) {
			  this.rootRef = pagesStack[pagesStack.length - 1].$vm.$refs.uniKuRoot
			  return this.rootRef
			}
		}
	}
})
