import request from "@/utils/Request.ts";

/**
 * 关闭 sse 连接
 */
export const handleClose = () => {
    return request({
        url: '/system/sse/close',
        method: 'post',
    })
}