// 初始化组件中需要的字典数据
import { useDictStore } from "@/stores/modules/dict";
import {getDictDataOption} from "@/api/system/dict/dictData";
import {ref, toRefs} from "vue";

export const initDict = (...dictTypeCodes: string[]) => {
  let resDictOption = ref<any>({})
  const dictStore = useDictStore()
  return (() => {
    dictTypeCodes.forEach(code => {
      resDictOption.value[code] = []
      const dictOption = dictStore.getDict(code)
      // 判断数据是否存在进行获取/缓存
      if (dictOption && dictOption.length > 0) {
        resDictOption.value[code] = dictOption
      } else {
        getDictDataOption(code).then(resp => {
          if (resp.code === 200) {
            resDictOption.value[code] = resp.data
            dictStore.setDict(code,resp.data)
          }
        })
      }
    })
    return toRefs(resDictOption.value)
  })()
}
