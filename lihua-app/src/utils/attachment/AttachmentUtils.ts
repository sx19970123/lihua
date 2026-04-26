const publicBaseURL =  import.meta.env.VITE_APP_PUBLIC_ATTACHMENT_API


type FileInfoType = {
	fileName?: string,
	filePath?: string,
	size?: number,
	md5?: string
}

/**
 * 获取附件详情
 */
export const getFileInfo = async (filePath?: string): Promise<FileInfoType> => {
	if (!filePath) {
		return {}
	}
	// 从路径中截取文件名
	const fileName = filePath.split('/').pop();
	
	return new Promise<FileInfoType>((resolve, reject) => {
		// #ifdef APP-PLUS
			uni.getFileInfo({
				filePath: filePath,
				success: (resp) => {
					resolve({fileName, filePath, size: resp.size, md5: resp.digest})
				},
				fail: (err) => {
					reject(err)
				}
			})
		// #endif
		
		// #ifndef APP-PLUS
			const fileManager = uni.getFileSystemManager()
			fileManager.getFileInfo({
				filePath: filePath,
				success: (resp) => {
					resolve({fileName, filePath, size: resp.size, md5: resp.digest})
				},
				fail: (err) => {
					reject(err)
				}
			})
		// #endif
	})
}

/**
 * url转临时地址
 */
export const getFileTempPath = (url: string): Promise<string> => {
  return new Promise((resolve, reject) => {
    if (!url) {
      reject(new Error('附件输入为空'))
      return
    }

    // 网络地址
    if (/^https?:\/\//i.test(url)) {
      uni.downloadFile({
        url: url,
        success: (resp) => {
          if (resp.tempFilePath) {
            resolve(resp.tempFilePath)
          } else {
            reject(new Error('下载失败'))
          }
        },
        fail: reject
      })
      return
    }

    reject(new Error('路径错误'))
  })
}

/**
 * 拼接公开附件全路径
 * @param fullPath 全路径
 */
export const attachmentUrl = (fullPath: string) => {
    return publicBaseURL + encodeURIComponent(fullPath)
}