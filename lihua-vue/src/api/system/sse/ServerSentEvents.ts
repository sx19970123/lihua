import request from "@/utils/Request.ts";

/**
 * 关闭 sse 连接
 */
export const handleClose = (clientKey: string) => {
    return request({
        url: '/system/sse/close/' + clientKey,
        method: 'post',
    })
}