import request from "@/utils/Request.ts";
import type {SysMenu, SysMenuVO} from "@/api/system/menu/type/SysMenu.ts";

/**
 * 列表查询
 * @param data
 */
export const queryList = (data:SysMenu) => {
  return request<Array<SysMenuVO>>({
    url: 'system/menu/list',
    method: 'post',
    data: data
  })
}

/**
 * 根据id查询详情
 * @param id
 */
export const queryById = (id: string) => {
  return request<SysMenu>({
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
    url: 'system/menu/' + data.menuType,
    method: 'post',
    data: data
  })
}

/**
 * 修改状态
 * @param ids
 * @param status
 */
export const updateStatus = (ids: string[], status: string) => {
  return request<string>({
    url: 'system/menu/updateStatus/' + status,
    method: 'post',
    data: ids
  })
}

/**
 * 根据id集合删除元素
 * @param ids
 */
export const deleteData = (ids: Array<string>) => {
  return request({
    url: 'system/menu',
    method: 'delete',
    data: ids
  })
}

/**
 * 获取系统全量菜单树选项数据
 */
export const queryMenuTreeOption = () => {
  return request<Array<SysMenu>>({
    url: 'system/menu/option',
    method: 'get'
  })
}
