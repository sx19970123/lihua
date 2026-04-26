import request from "@/utils/Request";
import type {SysDictDataType} from "@/api/system/dict/type/SysDictDataType";
import type {MapResponseType} from "@/api/global/Type";

export const getDictDataOptionByCodeList = (dictTypeCodeList: string[]) => {
  return request<MapResponseType<string, SysDictDataType>>({
    url: 'app/system/dictData/option',
    method: 'POST',
    data: dictTypeCodeList
  })
}