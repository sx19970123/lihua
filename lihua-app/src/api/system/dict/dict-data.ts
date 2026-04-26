import request from "@/utils/request";
import type {SysDictDataType} from "@/api/system/dict/type/sys-dict-data-type";
import type {MapResponseType} from "@/api/global/type";

export const getDictDataOptionByCodeList = (dictTypeCodeList: string[]) => {
  return request<MapResponseType<string, SysDictDataType>>({
    url: 'app/system/dictData/option',
    method: 'POST',
    data: dictTypeCodeList
  })
}