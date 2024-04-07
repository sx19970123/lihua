import { defineStore } from "pinia";


export const useDictStore = defineStore('dict', {
  state: () => {
    const dictMap: Map<string,Array<SysDictDataType>> = new Map();
    return {
      dictMap
    }
  },
  actions: {
    // 获取字典
    getDict(dictTypeCode: string) {
      const map = this.$state.dictMap
      if (map.has(dictTypeCode)) {
        return map.get(dictTypeCode)
      }
      return []
    },
    // 设置字典
    setDict(key: string, value: Array<SysDictDataType>) {
      this.$state.dictMap.set(key,value)
    },
    // 清空字典
    clearDict() {
      this.$state.dictMap.clear()
    }
  }
})
