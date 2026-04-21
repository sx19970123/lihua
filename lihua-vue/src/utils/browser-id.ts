import FingerprintJS from "@fingerprintjs/fingerprintjs"

// 获取浏览器指纹
export const createBrowserId = async () => {
    const fp = await FingerprintJS.load();
    const resp = await fp.get()
    return resp.visitorId
}