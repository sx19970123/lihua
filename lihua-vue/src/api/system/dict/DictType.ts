import request from "@/utils/Request.ts";
import type {SysDictType, SysDictTypeDTO, SysDictTypeVO} from "@/api/system/dict/type/SysDictType";
import type {PageResponseType} from "@/api/global/Type.ts";

/**
 * 列表页查询
 * @param data
 */
export const queryPage = (data: SysDictTypeDTO) => {
  return request<PageResponseType<SysDictTypeVO>>({
    url: 'system/dictType/page',
    method: 'post',
    data: data
  })
}

/**
 * 根据id查询
 * @param id
 */
export const queryById = (id: string) => {
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
 * 修改状态
 * @param id
 * @param status
 */
export const updateStatus = (id: string, status: string) => {
  return request<string>({
    url: 'system/dictType/updateStatus/' + id + '/' + status,
    method: 'post'
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

/**
 * 刷新缓存
 */
export const reloadCache = () => {
  return request({
    url: 'system/dictType/reload/cache',
    method: 'post'
  })
}