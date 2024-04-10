import request from "@/utils/request";

// 分页查询列表
export const findPage = (data: SysRoleDTO) => {
  return request({
    url: 'system/role/page',
    method: 'post',
    data: data,
  })
}

// 根据id查询数据
export const findById = (id: string) => {
  return request({
    url: 'system/role/' + id,
    method: 'get'
  })
}

// 保存数据
export const save = (data: SysRole) => {
  return request({
    url: 'system/role',
    method: 'post',
    data: data
  })
}

// 根据id集合删除数据
export const deleteByIds = (ids: Array<string>) => {
  return request({
    url: 'system/role',
    method: 'delete',
    data: ids
  })
}
