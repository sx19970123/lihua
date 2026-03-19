const publicBaseURL = import.meta.env.VITE_APP_PUBLIC_ATTACHMENT_API

/**
 * 拼接公开附件全路径
 * @param fullPath 全路径
 */
export const attachmentUrl = (fullPath: string) => {
    return publicBaseURL + encodeURIComponent(fullPath)
}

/**
 * 网络路径转为浏览器临时路径
 */
export const getTemporaryPath = async (url: string) => {
    const response = await fetch(url);
    const blob = await response.blob();
    return URL.createObjectURL(blob);
}