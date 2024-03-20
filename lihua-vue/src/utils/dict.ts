// 初始化组件中需要的字典数据
import { useDictStore } from "@/stores/modules/dict";
import {getDictDataOption} from "@/api/system/dict/dictData";

export const initDict = (...dictTypeCodes: string[]) => {
  let resDictOption: any = {}
  const dictStore = useDictStore()

  dictTypeCodes.forEach(code => {
    // 从 store 获取 dict option 数据
    const dictOption = dictStore.getDict(code)

    // 判断数据是否存在进行获取/缓存
    if (dictOption && dictOption.length > 0) {
      resDictOption[code] = dictOption
    } else {
      getDictDataOption(code).then(resp => {
        if (resp.code === 200) {
          resDictOption[code] = resp.data
          dictStore.setDict(code,resp.data)
        }
      })
    }
  })

  return resDictOption
}
