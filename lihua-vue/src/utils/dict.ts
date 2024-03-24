// 初始化组件中需要的字典数据
import { useDictStore } from "@/stores/modules/dict";
import {getDictDataOption} from "@/api/system/dict/dictData";
import {type Ref, ref, toRefs} from "vue";

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

export const getLabel = (dictDataOption: Ref<any>, dictDataValue: string) => {
  console.log(dictDataOption)
  const data = getDictDataObject(dictDataOption.value,dictDataValue)
  if (data) {
    return data.label
  }
  console.error("获取字典标签异常")
}

export const getDictDataObject = (option: Array<any>, dictDataValue: string) => {
  return deepGetData(option,dictDataValue)
}

const deepGetData = (arg: Array<any>,dictDataValue: string) : any => {
  for (let i = 0; i < arg.length; i++) {
    const item = arg[i]
    if (item.value === dictDataValue) {
      return item
    } else {
      if (item.children) {
        return deepGetData(item.children,dictDataValue)
      }
    }
  }
  return null
}