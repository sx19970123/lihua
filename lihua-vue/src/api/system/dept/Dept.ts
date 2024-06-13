import request from "@/utils/Request.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";

/**
 * 列表查询
 * @param data
 */
export const findList = (data: SysDept) => {
  return request<Array<SysDept>>({
    url: 'system/dept/list',
    method: 'post',
    data: data
  })
}

/**
 * 数据保存
 * @param data
 */
export const save = (data: SysDept) => {
  return request<string>({
    url: 'system/dept',
    method: 'post',
    data: data
  })
}

/**
 * 根据 id 查询数据
 * @param id
 */
export const findById = (id: string) => {
  return request<SysDept>({
    url: 'system/dept/' + id,
    method: 'get'
  })
}

/**
 * 数据删除
 * @param ids
 */
export const deleteByIds = (ids: Array<string>) => {
  return request({
    url: 'system/dept',
    method: 'delete',
    data: ids
  })
}

/**
 * 单位下拉树
 */
export const getDeptOption = () => {
  return request<Array<SysDept>>({
    url: 'system/dept/option',
    method: 'get',
  })
}

/**
 * 设置默认部门
 * @param deptId
 */
export const setDefaultDept = (deptId: string) => {
  return request<SysDept>({
    url: 'system/dept/default/' + deptId,
    method: 'post',
  })
}