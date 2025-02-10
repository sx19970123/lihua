const baseURL = import.meta.env.VITE_APP_BASE_API
const origin = window.location.origin

/**
 * 通用下载，根据传入参数不同自动调用不同的下载函数
 * @param data 链接 | 路径 | id
 * @param fileName 附件名称
 */
export const download = (data: string, fileName?: string) => {
    if (data.startsWith(baseURL) || data.startsWith(origin)) {
        // 传入的是链接
        handleDownload(data, fileName)
    } else if (data.includes("\\")) {
        // 传入的是路径
        downloadExport(data, fileName)
    } else {
        // 传入的是id
        downloadPublic(data, fileName)
    }
}

// 根据附件id下载
export const downloadPublic = (id: string, fileName?: string) =>  {
    handleDownload(import.meta.env.VITE_APP_BASE_API + `/system/attachment/download/p/${id}?fileName=${fileName?encodeURIComponent(fileName):''}`, fileName)
}

// 附件导出下载
export const downloadExport = (path: string, fileName?: string) => {
    handleDownload(import.meta.env.VITE_APP_BASE_API + `/system/attachment/download/e?path=${encodeURIComponent(path)}&fileName=${fileName?encodeURIComponent(fileName):''}`, fileName);
}

// 通过url下载
export const handleDownload = (url: string, fileName?: string) => {
    const linkElement = document.createElement('a');
    linkElement.href = url;
    if (fileName) {
        linkElement.download = fileName;
    }
    document.body.appendChild(linkElement);
    linkElement.click();
    document.body.removeChild(linkElement);
}