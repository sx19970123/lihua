import request from "@/utils/request";
/**
 * 列表查询
 */
export const findList = (data: SysDictDataType) => {
  return request({
    url: 'system/dictData/list',
    method: 'post',
    data: data
  })
}

/**
 * 保存数据
 */
export const save = (data: SysDictDataType) => {
  return request({
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
