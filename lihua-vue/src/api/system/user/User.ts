import request from "@/utils/Request.ts";
import type {ExcelImportResult, PageResponseType} from "@/api/global/Type.ts";
import type {SysUser, SysUserDTO, SysUserVO} from "@/api/system/user/type/SysUser.ts";
import type {RcFile} from "ant-design-vue/es/vc-upload/interface";

// 分页查询列表
export const queryPage = (data: SysUserDTO) => {
    return request<PageResponseType<SysUserVO>>({
        url: "system/user/page",
        method: "post",
        data: data,
    })
}

// 根据id查询详情
export const queryById = (id: string) => {
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

// 重置密码
export const resetPassword = (userId: string,  password: string, passwordRequestKey: string) => {
    return request<string>({
        url: 'system/user/resetPassword',
        method: 'post',
        data: {
            userId: userId,
            password: password,
            passwordRequestKey: passwordRequestKey
        }
    })
}

// 根据部门id获取用户选项
export const getUserOption = (deptId: string) => {
    return request<SysUser[]>({
        url: 'system/user/option/' + deptId,
        method: 'get'
    })
}

// 根据用户id集合获取用户选项
export const getUserOptionByUserIds = (userIds: String[]) => {
    return request<SysUser[]>({
        url: 'system/user/option',
        method: 'post',
        data: userIds
    })
}

// excel导出
export const exportExcel = (data: SysUserDTO) => {
    return request<string>({
        url: 'system/user/export',
        method: "post",
        data: data,
    })
}

// excel 导入
export const importExcel = (file:  string | Blob | RcFile) => {
    const formData = new FormData()
    formData.append('file', file)

    return request<ExcelImportResult>({
        url: 'system/user/import',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}