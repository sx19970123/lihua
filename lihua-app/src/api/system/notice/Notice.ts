import request from "@/utils/Request"
import type {SysNoticeVO, SysNoticeDTO} from "@/api/system/notice/type/SysNotice";
import type { PageResponseType } from "@/api/global/Type";
import type {SysUserNoticeVO} from "@/api/system/notice/type/SysUserNotice";
/**
 * 查询预览
 * @param id
 */
export const preview = (id: string) => {
    return request<SysNoticeVO>({
        url: "app/system/notice/preview/" + id,
        method: "GET"
    })
}

/**
 * 消息通知添加取消 star
 * @param noticeId
 * @param star
 */
export const star = (noticeId: string, star: string) => {
    return request({
        url: 'app/system/notice/star/' + noticeId + '/' + star,
        method: 'POST'
    })
}

/**
 * 消息通知设置已读
 * @param noticeId
 */
export const read = (noticeId: string) => {
    return request<string>({
        url: 'app/system/notice/read/' + noticeId,
        method: 'POST'
    })
}

/**
 * 获取未读消息总数
 */
export const queryUnReadCount = () => {
    return request<number>({
        url: 'app/system/notice/unread/count',
        method: 'GET'
    })
}

/**
 * 查询消息列表
 */
export const userMessageList = (data: SysNoticeDTO) => {
    return request<PageResponseType<SysUserNoticeVO>>({
        url: 'app/system/notice/list',
        method: 'POST',
        data: data
    })
}