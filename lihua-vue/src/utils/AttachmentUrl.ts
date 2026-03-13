const publicBaseURL = import.meta.env.VITE_APP_PUBLIC_ATTACHMENT_API

/**
 * 拼接公开附件全路径
 * @param fullPath 全路径
 */
export const attachmentUrl = (fullPath: string) => {
    return publicBaseURL + encodeURIComponent(fullPath)
}