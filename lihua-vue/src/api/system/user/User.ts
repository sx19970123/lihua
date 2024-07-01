import request from "@/utils/Request.ts";
import type {PageResponseType} from "@/api/global/Type.ts";
import type {SysUser, SysUserDTO, SysUserVO} from "@/api/system/user/type/SysUser.ts";

// 分页查询列表
export const findPage = (data: SysUserDTO) => {
    return request<PageResponseType<SysUserVO>>({
        url: "system/user/page",
        method: "post",
        data: data,
    })
}

// 根据id查询详情
export const findById = (id: string) => {
    return request<SysUserDTO>({
        url: "system/user/" + id,
        method: "get"
    })
}

// 保存用户信息
export const save = (data: SysUser) => {
    return request({
        url: 'system/user',
        method: 'post',
        data: data
    })
}

// 修改用户状态
export const updateStatus = (id: string, status: string) => {
    return request<string>({
        url: 'system/user/updateStatus/' + id + '/' + status,
        method: 'post'
    })
}

// 根据id集合删除用户信息
export const deleteByIds = (ids: string[]) => {
    return request({
        url: 'system/user',
        method: 'delete',
        data: ids
    })
}

// excel导出
export const exportExcel = (data: SysUserDTO) => {
    return request({
        url: 'system/user/export',
        method: "post",
        data: data,
    })
}

// excel 导入
export const importExcel = (file: Blob) => {
    const formData = new FormData()
    formData.append('file', file)

    return request({
        url: 'system/user/importExcel',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}