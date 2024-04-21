import request from "@/utils/request.ts";

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
export const deptOption = () => {
  return request({
    url: 'system/dept/option',
    method: 'get',
  })
}