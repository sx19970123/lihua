import request from "@/utils/request.ts";
import type {PageResponseType} from "@/api/type.ts";

// 查询与业务流程相绑定的用户列表
export const findAssociatedUserPage = (data: AssociationUserDTO) => {
    return request<PageResponseType<SysUser>>({
        url: '/system/user/association/page',
        method: 'post',
        data: data
    })
}

// 查询还未与业务流程想绑定的用户列表
export const findUnassociatedUserPage = (data: AssociationUserDTO) => {
    return request<PageResponseType<SysUser>>({
        url: '/system/user/unAssociation/page',
        method: 'post',
        data: data
    })
}

// 用户解绑
export const detach = (data: AssociationUserDTO) => {
    return request({
        url: 'system/user/association/detach',
        method: 'delete',
        data: data
    })
}

// 用户绑定
export const associate = (data: AssociationUserDTO) => {
    return request({
        url: 'system/user/association',
        method: 'post',
        data: data
    })
}