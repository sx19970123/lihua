import { useDictStore } from "@/stores/modules/dict";
import {getDictDataOption} from "@/api/system/dict/dictData";
import { ref, toRefs} from "vue";
import type {SysDictDataType} from "@/api/system/dict/type/SysDictDataType.ts";
import type {ResponseType} from "@/api/type.ts";
// 初始化组件中需要的字典数据
export const initDict = (...dictTypeCodes: string[]) => {
  // 返回 key 为字典编码，value 为字典集合结构
  type ResDictOptionType = {
    [key: string]: SysDictDataType[]
  }
  let resDictOption= ref<ResDictOptionType>({})
  const dictStore= useDictStore()
  return (() => {
    dictTypeCodes.forEach(code => {
      resDictOption.value[code] = []
      const dictOption= dictStore.getDict(code)
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
// 重新从后端拉取对应字典
export const reLoadDict = (code: string) => {
  return new Promise((resolve, reject) => {
    const dictStore= useDictStore()
    getDictDataOption(code).then((resp: ResponseType<Array<SysDictDataType>>) => {
      if (resp.code === 200) {
        dictStore.setDict(code,resp.data)
        resolve(resp.data)
      }
    })
  })
}
