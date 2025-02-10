import request from "@/utils/Request.ts";
import type {SysDept, SysDeptVO} from "@/api/system/dept/type/SysDept.ts";
import type {ExcelImportResult} from "@/api/global/Type.ts";
import type {RcFile} from "ant-design-vue/es/vc-upload/interface";

/**
 * 列表查询
 * @param data
 */
export const queryList = (data: SysDept) => {
  return request<Array<SysDeptVO>>({
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
export const queryById = (id: string) => {
  return request<SysDept>({
    url: 'system/dept/' + id,
    method: 'get'
  })
}

/**
 * 修改状态
 * @param id
 * @param status
 */
export const updateStatus = (id: string, status: string) => {
  return request<string>({
    url: 'system/dept/updateStatus/' + id + '/' + status,
    method: 'post'
  })
}

/**
 * 数据删除
 * @param ids
 */
export const deleteData = (ids: Array<string>) => {
  return request({
    url: 'system/dept',
    method: 'delete',
    data: ids
  })
}

/**
 * 单位下拉树（全量数据）
 */
export const getDeptOption = () => {
  return request<Array<SysDept>>({
    url: 'system/dept/option',
    method: 'get',
  })
}

/**
 * 部门导出excel
 * @param data
 */
export const exportExcel = (data: SysDept) => {
  return request<string>({
    url: 'system/dept/export',
    method: 'post',
    data: data
  })
}

/**
 * excel 导入
 * @param file
 */
export const importExcel = (file:  string | Blob | RcFile) => {
  const formData = new FormData()
  formData.append('file', file)

  return request<ExcelImportResult>({
    url: 'system/dept/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}