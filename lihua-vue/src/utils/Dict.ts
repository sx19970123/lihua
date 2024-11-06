import { useDictStore } from "@/stores/dict";
import {getDictDataOption, getDictDataOptionByCodeList} from "@/api/system/dict/DictData.ts";
import { ref, toRefs} from "vue";
import type {SysDictDataType} from "@/api/system/dict/type/SysDictDataType.ts";
import {ResponseError, type ResponseType} from "@/api/global/Type.ts";
import {message} from "ant-design-vue";
// 初始化组件中需要的字典数据
export const initDict = (...dictTypeCodes: string[]) => {
  // 返回 key 为字典编码，value 为字典集合结构
  type ResDictOptionType = {
    [key: string]: SysDictDataType[]
  }
  let resDictOption= ref<ResDictOptionType>({})
  const dictStore= useDictStore()
  return (() => {
    // store中不存在等待去数据库中查询的code集合
    const dictCodeList: string[] = []
    dictTypeCodes.forEach(code => {
      resDictOption.value[code] = []
      const dictOption= dictStore.getDict(code)
      // 判断数据是否存在进行获取/缓存
      if (dictOption && dictOption.length > 0) {
        resDictOption.value[code] = dictOption
      } else {
        dictCodeList.push(code)
      }
    })
    // 拿到收集到的字典编码集合查询字典选项
    if (dictCodeList.length > 0) {
      getDictDataOptionByCodeList(dictCodeList).then(resp => {
        if (resp.code === 200) {
          const data = resp.data
          dictCodeList.forEach(code => {
            const dictOption = data[code]
            if (dictOption) {
              resDictOption.value[code] = dictOption
              dictStore.setDict(code,dictOption)
            }
          })
        } else {
          message.error(resp.msg)
        }
      }).catch(e => {
        if (e instanceof ResponseError) {
          message.error(e.msg)
        } else {
          console.error(e)
        }
      })
    }

    return toRefs(resDictOption.value)
  })()
}

// 根据 option 集合 和 value 获取字典 label
export const getDictLabel = (option: SysDictDataType[], value?: string) => {
  const filter = option.filter(dict => dict.value === value)
  if (filter.length > 0) {
    return filter[0].label
  }
  return value
}

// 重新从后端拉取对应字典
export const reLoadDict = (code: string) => {
  return new Promise((resolve, reject) => {
    const dictStore= useDictStore()
    getDictDataOption(code).then((resp: ResponseType<Array<SysDictDataType>>) => {
      if (resp.code === 200) {
        dictStore.setDict(code,resp.data)
        resolve(resp.data)
      } else {
        reject(new ResponseError(resp.code, resp.msg))
      }
    }).catch(e => {
      reject(e)
    })
  })
}
