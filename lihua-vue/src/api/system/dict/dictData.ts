import request from "@/utils/request";
/**
 * 列表查询
 */
export const findList = (data: SysDictDataType) => {
  return request<Array<SysDictDataType>>({
    url: 'system/dictData/list',
    method: 'post',
    data: data
  })
}

/**
 * 保存数据
 */
export const save = (data: SysDictDataType) => {
  return request<string>({
    url: 'system/dictData',
    method: 'post',
    data: data
  })
}

/**
 * 删除数据
 */
export const deleteData = (ids: Array<string>) => {
  return request({
    url: 'system/dictData',
    method: 'delete',
    data: ids
  })

}

/**
 * 根据字典类型编码获取字典数据
 * @param dictTypeCode
 */
export const getDictDataOption = (dictTypeCode: string) => {
  return request<Array<SysDictDataType>>({
    url: 'system/dictData/option/' + dictTypeCode,
    method: 'get'
  })
}
