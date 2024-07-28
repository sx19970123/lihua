import request from "@/utils/Request.ts"
import type {SysNotice, SysNoticeDTO, SysNoticeVO} from "@/api/system/noice/type/SysNotice.ts";
import type {MapResponseType, PageResponseType} from "@/api/global/Type.ts";
import type {SysUser} from "@/api/system/user/type/SysUser.ts";

/**
 * 分页查询
 * @param data
 */
export const findPage = (data: SysNoticeDTO) => {
    return request<PageResponseType<SysNotice>>({
        url: "/system/notice/page",
        method: "post",
        data: data
    })
}

/**
 * 根据id查询
 */
export const findById = (id: string) => {
    return request<SysNoticeVO>({
        url: "/system/notice/" + id,
        method: "get"
    })
}

/**
 * 保存数据
 * @param data
 */
export const save = (data: SysNoticeVO) => {
    return request<string>({
        url: "/system/notice",
        method: "post",
        data: data
    })
}

/**
 * 获取已读未读信息
 * @param id
 */
export const findReadInfo = (id: string) => {
    return request<MapResponseType<String,SysUser[]>>({
        url: "/system/notice/readInfo/" + id,
        method: "get"
    })
}

/**
 * 通知发布
 */
export const release = (id: string) => {
    return request<string>({
        url: '/system/notice/release/' + id,
        method: 'post'
    })
}

/**
 * 通知撤回
 */
export const revoke = (id: string) => {
    return request<string>({
        url: '/system/notice/revoke/' + id,
        method: 'post'
    })
}

/**
 * 根据ids删除
 * @param ids
 */
export const deleteByIds = (ids: string[]) => {
    return request({
        url: '/system/notice',
        method: 'delete',
        data: ids
    })
}