import request from "@/utils/Request.ts";
import type {SysDictType, SysDictTypeDTO} from "@/api/system/dict/type/SysDictType";
import type {PageResponseType} from "@/api/global/Type.ts";

/**
 * 列表页查询
 * @param data
 */
export const findPage = (data: SysDictTypeDTO) => {
  return request<PageResponseType<SysDictType>>({
    url: 'system/dictType/page',
    method: 'post',
    data: data
  })
}

/**
 * 根据id查询
 * @param id
 */
export const findById = (id: string) => {
  return request<SysDictType>({
    url: 'system/dictType/' + id,
    method: 'get'
  })
}

/**
 * 保存数据
 */
export const save = (data: SysDictType) => {
  return request({
    url: 'system/dictType',
    method: 'post',
    data: data
  })
}

/**
 * 删除数据
 * @param ids
 */
export const deleteData = (ids: Array<string>) => {
  return request({
    url: 'system/dictType',
    method: 'delete',
    data: ids
  })
}
