import request from "@/utils/request";

/**
 * 列表查询
 * @param data
 */
export const findList = (data:SysMenu) => {
  return request({
    url: 'system/menu/list',
    method: 'post',
    data: data
  })
}

/**
 * 根据id查询详情
 * @param id
 */
export const findById = (id: string) => {
  return request({
    url: 'system/menu/' + id,
    method: 'get'
  })
}

/**
 * 保存方法
 * @param data
 */
export const save = (data:SysMenu) => {
  return request({
    url: 'system/menu',
    method: 'post',
    data: data
  })
}

/**
 * 根据id集合删除元素
 * @param ids
 */
export const deleteByIds = (ids: Array<string>) => {
  return request({
    url: 'system/menu',
    method: 'delete',
    data: ids
  })
}
