import request from "@/utils/Request.ts";
import type {ExcelImportResult, MapResponseType, PageResponseType} from "@/api/global/Type.ts";
import type {SysPost, SysPostDTO, SysPostVO} from "@/api/system/post/type/SysPost.ts";
import type {RcFile} from "ant-design-vue/es/vc-upload/interface";

/**
 * 分页查询
 * @param data
 */
export const queryPage = (data: SysPostDTO) => {
    return request<PageResponseType<SysPostVO>>({
        url: "system/post/page",
        data: data,
        method: "post",
    })
}

/**
 * 保村岗位信息
 * @param data
 */
export const save = (data: SysPost) => {
    return request<string>({
        url: 'system/post',
        data: data,
        method: 'post',
    })
}

/**
 * 根据 主键查询数据
 * @param id
 */
export const queryById = (id: string) => {
    return request<SysPost>({
        url: 'system/post/' + id,
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
        url: 'system/post/updateStatus/' + id + '/' + status,
        method: 'post'
    })
}

/**
 * 根据id批量删除
 * @param ids
 */
export const deleteData = (ids: Array<String>) => {
    return request({
        url: 'system/post',
        data: ids,
        method: 'delete'
    })
}

/**
 * 根据部门信息获取岗位数据
 * @param deptIds
 */
export const getPostOptionByDeptId = (deptIds: string[]) => {
    return request<MapResponseType<String,SysPost>>({
        url: 'system/post/option',
        method: 'post',
        data: deptIds
    })
}

/**
 * excel导出
 * @param data
 */
export const exportExcel = (data: SysPostDTO) => {
    return request<string>({
        url: 'system/post/export',
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
        url: 'system/post/import',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}