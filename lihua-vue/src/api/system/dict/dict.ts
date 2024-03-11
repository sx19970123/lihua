import request from "@/utils/request";
import type { SysDictTypeDTO } from "@/api/system/dict/type/SysDictType"
import {markRaw} from "vue";

/**
 * 列表页查询
 * @param data
 */
export const findPage = (data: SysDictTypeDTO) => {
  return request({
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
  return request({
    url: 'system/dictType/' + id,
    method: 'get'
  })
}

/**
 * 保存数据
 */
export const save = (data: SysDictTypeDTO) => {
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
