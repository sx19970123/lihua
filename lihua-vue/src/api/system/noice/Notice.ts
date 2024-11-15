import request from "@/utils/Request.ts"
import type {SysNotice, SysNoticeDTO, SysNoticeVO} from "@/api/system/noice/type/SysNotice.ts";
import type {MapResponseType, PageResponseType} from "@/api/global/Type.ts";
import type {SysUser} from "@/api/system/user/type/SysUser.ts";
import type {SysUserNoticeVO} from "@/api/system/noice/type/SysUserNotice.ts";

/**
 * 分页查询
 * @param data
 */
export const queryPage = (data: SysNoticeDTO) => {
    return request<PageResponseType<SysNotice>>({
        url: "/system/notice/page",
        method: "post",
        data: data
    })
}

/**
 * 根据id查询
 */
export const queryById = (id: string) => {
    return request<SysNoticeVO>({
        url: "/system/notice/" + id,
        method: "get"
    })
}

/**
 * 查询预览
 * @param id
 */
export const preview = (id: string) => {
    return request<SysNoticeVO>({
        url: "/system/notice/preview/" + id,
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
export const queryReadInfo = (id: string) => {
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

/**
 * 用户查询消息通知
 * @param userId
 * @param data
 */
export const queryListByUserId = (userId: string, data: SysNoticeDTO) => {
    return request<PageResponseType<SysUserNoticeVO>>({
        url: 'system/notice/list/' + userId,
        method: 'post',
        data: data
    })
}

/**
 * 消息通知添加取消 star
 * @param noticeId
 * @param star
 */
export const star = (noticeId: string, star: string) => {
    return request({
        url: '/system/notice/star/' + noticeId + '/' + star,
        method: 'post'
    })
}

/**
 * 消息通知设置已读
 * @param noticeId
 */
export const read = (noticeId: string) => {
    return request({
        url: '/system/notice/read/' + noticeId,
        method: 'post'
    })
}

/**
 * 获取未读消息总数
 */
export const queryUnReadCount = () => {
    return request<number>({
        url: '/system/notice/unread/count',
        method: 'get'
    })
}
