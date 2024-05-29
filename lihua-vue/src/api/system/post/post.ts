import request from "@/utils/request.ts";
import type {MapResponseType, PageResponseType} from "@/api/type.ts";

/**
 * 分页查询
 * @param data
 */
export const findPage = (data: SysPostDTO) => {
    return request<PageResponseType<SysPost>>({
        url: "/system/post/page",
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
        url: '/system/post',
        data: data,
        method: 'post',
    })
}

/**
 * 根据 主键查询数据
 * @param id
 */
export const findById = (id: string) => {
    return request<SysPost>({
        url: '/system/post/' + id,
        method: 'get'
    })
}

/**
 * 根据id批量删除
 * @param ids
 */
export const deleteByIds = (ids: Array<String>) => {
    return request({
        url: '/system/post',
        data: ids,
        method: 'delete'
    })
}

/**
 * 根据部门信息获取岗位数据
 * @param deptIds
 */
export const getPostOptionByDeptId = (deptIds: string[]) => {
    return request<MapResponseType<SysPost>>({
        url: '/system/post/option',
        method: 'post',
        data: deptIds
    })
}