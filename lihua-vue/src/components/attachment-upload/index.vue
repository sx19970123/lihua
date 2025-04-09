<template>
  <a-spin v-model:spinning="uploading" :tip="uploadTip">
    <a-upload v-if="model === 'button' || model === 'picture'"
              v-model:file-list="fileList"
              :action="uploadURL"
              :headers="{Authorization: authorization}"
              :data="sysAttachment"
              :list-type="model === 'picture' ? 'picture-card' : 'text'"
              :before-upload="beforeUpload"
              :directory="chunk ? false : directory"
              :multiple="chunk ? false : multiple"
              :max-count="maxCount"
              :isImageUrl="handleShowThumbImage"
              @preview="handlePreview"
              @change="handleChange"
              @remove="handleRemove"
    >
      <!--      picture模式下预览图标变化-->
      <template #previewIcon="data">
        <a-button class="css-dev-only-do-not-override-2qvcno ant-btn ant-btn-text ant-btn-sm ant-upload-list-item-action ant-btn-icon-only" type="link">
          <CloudDownloadOutlined class="anticon anticon-eye"
                                 v-if="!imageExtensions.includes(data.file.name.toLowerCase().split('.').pop()) &&
                                  !videoExtensions.includes(data.file.name.toLowerCase().split('.').pop())"/>
          <EyeOutlined class="anticon anticon-eye" v-else/>
        </a-button>
      </template>
      <!--    按钮上传-->
      <a-button v-if="model === 'button'">
        <component :is="icon ? icon : 'upload-outlined'"></component>
        {{text ? text : '点击上传'}}
      </a-button>
      <!--    图片上传-->
      <div v-if="model === 'picture'">
        <component :is="icon ? icon : 'plus-outlined'"></component>
        <div style="margin-top: 8px">{{text ? text : '点击上传'}}</div>
      </div>
    </a-upload>
    <!--    拖拽上传-->
    <a-upload-dragger v-else
                      v-model:file-list="fileList"
                      :action="uploadURL"
                      :headers="{Authorization: authorization}"
                      :before-upload="beforeUpload"
                      :data="sysAttachment"
                      :directory="chunk ? false : directory"
                      :multiple="chunk ? false : multiple"
                      :max-count="maxCount"
                      @preview="handlePreview"
                      @change="handleChange"
                      @remove="handleRemove"
    >
      <p class="ant-upload-drag-icon">
        <component :is="icon ? icon : 'inbox-outlined'"></component>
      </p>
      <p class="ant-upload-text">{{text ? text : '点击或拖拽上传'}}</p>
      <p class="ant-upload-hint">{{description}}</p>
    </a-upload-dragger>
<!--    图片/视频预览-->
    <a-modal :open="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel" destroyOnClose>
      <a-image style="border-radius: 8px" :preview="{maskClassName: 'attachment-upload-preview-mask'}" :src="previewURL" v-if="previewType === 'image'"/>
      <video style="width: 100%;border-radius: 8px" controls preload="auto" :src="previewURL" v-if="previewType === 'video'"/>
    </a-modal>
  </a-spin>
</template>

<script setup lang="ts">
import {message, Upload, type UploadFile} from "ant-design-vue";
import {onMounted, ref, watch} from "vue";
import {useRoute} from "vue-router";
import token from "@/utils/Token.ts";
import {
  chunksMerge,
  chunksUpload,
  chunksUploadedIndex,
  chunksUploadStart,
  deleteFromBusiness,
  existsAttachmentByMd5,
  fastUpload,
  queryAttachmentInfoByIds
} from "@/api/system/attachment/AttachmentStorage.ts";
import {getDownloadURL} from "@/api/system/attachment/Attachment.ts";
import {ResponseError} from "@/api/global/Type.ts";
import {currentRequests} from "@/utils/Request.ts";
import type {SysAttachment} from "@/api/system/attachment/type/SysAttachment.ts";
import {download} from "@/utils/AttachmentDownload.ts";
const { getToken } = token

const imageExtensions = ["jpg", "jpeg", "png", "gif", "bmp", "svg", "webp"]
const videoExtensions = ["mp4", "avi", "mkv", "mov", "wmv", "flv", "webm"]
const baseAPI = import.meta.env.VITE_APP_BASE_API
const uploadURL = `${baseAPI}/system/attachment/storage/upload`
const chunk_upload_prefix = "upload-record-"
const authorization = 'Bearer ' + getToken()
const router = useRoute()
let vModelComplete = false

// 参数
const {model = 'button', icon, text, uploadType = [], description, maxCount = 10, maxSize = 10, multiple = true, directory = false, modelValue = "", businessCode, businessName, chunk = false, chunkSize = 20, chunkUploadCount = 3} = defineProps<{
  // 模式：按钮/图片/拖拽
  model?: 'button' | 'picture' | 'dragger',
  // 图标
  icon?: string,
  // 文本描述
  text?: string,
  // 可上传的附件类型
  uploadType?: string[],
  // 详细说明（仅支持拖拽上传）
  description?: string,
  // 最大上传数量
  maxCount?: number,
  // 最大上传大小（mb）
  maxSize?: number,
  // 是否支持多附件上传
  multiple?: boolean,
  // 是否支持附件夹上传
  directory?: boolean,
  // 双向绑定
  modelValue?: string,
  // 业务编码
  businessCode?: string,
  // 业务名称
  businessName?: string,
  // 是否分片上传
  chunk?: boolean,
  // 最大分片片段大小
  chunkSize?: number,
  // 分片上传同时上传数量
  chunkUploadCount?: number,
}>()

// 方法
const emits = defineEmits(["update:modelValue", "uploadError", "uploadSuccess","exceedMaxCount", "remove"])

// 附件对象
const sysAttachment = ref<SysAttachment>({})

// 处理附件对象
const handleSysAttachment = (file: UploadFile, md5: string, uploadMode?: string) => {
  sysAttachment.value = {
    businessCode: businessCode ?? router.name?.toString(),
    businessName: businessName ?? router.meta.label as string,
    uploadMode: uploadMode ?? "0",
    originalName: file.name,
    size: file.size? file.size.toString() : "",
    type: file.type,
    md5: md5
  }
}

// 附件列表
const fileList = ref<UploadFile[]>([])
// 附件秒传轮询等待变量
const awaitHandleFile = ref<boolean>(false)
// 初始化双向绑定
const initVModel = async (newVal: string, oldValue?: string) => {
  const ids = modelValue.split(",").filter(Boolean)
  if (ids && ids.length > 0) {
    try {
      // 初次加载数据时根据双向绑定内容请求附件信息
      const resp = await queryAttachmentInfoByIds(ids)
      if (resp.code === 200) {
        // 组合fileList
        const data = resp.data.map(item => {
          const id = item.id
          const uploadFile: UploadFile = {
            uid: id ? id : '',
            name: item.originalName ? item.originalName : '',
            status: item.status === 'error' ? "error" : "done",
            url: id,
            thumbUrl: handleThumbUrl(item.path)
          }
          return uploadFile;
        })
        // 数据回显
        if (data && data.length > 0) {
          fileList.value.unshift(...data)
        }
        // 在双向绑定加载前又上传了数据的情况，将新加载的newVal拼接到oldValue之前，再次更新双向绑定
        if (oldValue) {
          emits("update:modelValue", newVal + "," + oldValue)
        }
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    } finally {
      vModelComplete = true
    }
  }
}

// 初始化附件上传
const initUpload = () => {
  // 附件上传前检验，同时进行不同上传逻辑的区分
  const beforeUpload = async (file: UploadFile, currentFileList: UploadFile[]) => {
    // 获取附件数据异常
    if (!file || !file.name || !file.size) {
      message.error("获取附件数据异常")
      return Upload.LIST_IGNORE;
    }

    // 验证附件大小和类型
    if (!checkSize(file.size) || !checkType(file.name)) {
      return Upload.LIST_IGNORE;
    }

    // 控制附件上传最大数
    const index = currentFileList.findIndex(item => item === file)
    if (index >= maxCount - fileList.value.length) {
      emits("exceedMaxCount", file)
      return Upload.LIST_IGNORE;
    }

    if (chunk) {
      // 分片上传
      startChunkUpload(file).then(() => {})
      return false;
    } else {
      // 一般上传
      return await startUpload(file)
    }
  };

  // 检查附件大小
  const checkSize = (size: number): boolean => {
    const sizeMB = Math.ceil(size / 1024 / 1024)
    const flag = maxSize >= sizeMB
    if (!flag) {
      message.error("仅允许上传" + maxSize + "MB以内的附件")
    }
    return flag;
  }

  // 检查附件类型
  const checkType = (fileName: string): boolean => {
    const split = fileName.split(".")
    // 附件没有后缀
    if (split.length === 0) {
      message.error("未知的附件类型")
      return false;
    }
    // 可上传的附件类型为空不进行限制
    if (uploadType.length === 0) {
      return true;
    }
    // 判断附件后缀
    if (!uploadType.includes("." + split[split.length - 1])) {
      message.error("仅支持上传 " + uploadType.join(" ") + " 附件");
      return false;
    }
    return true;
  }

  // 处理更新双向绑定
  const handleModelValue = (file: UploadFile, fileList: Array<UploadFile>) => {
    // 通过fileList获取双向绑定值
    const modelValueList = fileList.filter(item => item.status === "done").map(item => {
      // 有url的直接返回（url内容为附件表id）
      if (item.url) {
        return item.url
      }
      // 有response数据获取对应的data。code不为200调用上传失败
      if (item.response) {
        const resp = item.response
        if (resp.code === 200) {
          const url = resp.data
          // 向fileList赋值URL
          fileList.forEach(item => {
            if (item.uid === file.uid) {
              item.url = url
            }
          })
          return url
        } else {
          // 后端返回非200，标记为上传失败
          fileList.forEach(item => {
            if (item.uid === file.uid) {
              item.status = "error"
            }
          })
          emits("uploadError", file)
        }
      }
    })

    // 处理双向绑定
    emits("update:modelValue", modelValueList.join(","))
  }

  // 处理附件上传变化（uploading：上传中 done：上传成功 error：上传失败 removed：已删除）
  const handleChange = ({file, fileList}: {file: UploadFile, fileList: Array<UploadFile>}) => {
    // 重置轮询等待状态
    awaitHandleFile.value = false

    if (!file.status || file.status === "uploading") {
      return
    }

    // 附件上传失败
    if (file.status === "error") {
      emits("uploadError", file)
    }

    // 附件上传成功
    if (file.status === "done") {
      emits("uploadSuccess", {file, fileList});
    }

    // 附件删除回调
    if (file.status === "removed") {
      emits("remove", file)
    }

    // 处理双向绑定
    handleModelValue(file, fileList)
  }

  // 一般附件上传，返回true由a-upload进行上传，返回false执行附件秒传逻辑
  const startUpload = (file: UploadFile) => {
    return new Promise(async (resolve) => {

      // 1. 获取附件md5
      const md5 = await handleCalculateHash(file) as string
      // 2. 根据md5向后端查询数据库，判断附件是否需要上传
      const resp = await existsAttachmentByMd5(md5, file.name)
      if (resp.code === 200) {
        if (resp.data) {
          awaitHandleFile.value = true
          resolve(false)
          // 附件存在，无需上传，执行附件秒传逻辑
          handleFastUpload(file, md5)
        } else {
          // 构建附件对象
          handleSysAttachment(file, md5)
          resolve(true)
        }
      } else {
        // 服务器返回可控制的错误时，继续上传附件
        handleSysAttachment(file, md5)
        resolve(true)
        console.error(resp.msg, "为业务正常进行，继续上传附件")
      }
    })
  }

  // 处理附件秒传
  const handleFastUpload = (file: UploadFile, md5: string) => {
    // 构建 sysAttachment
    handleSysAttachment(file, md5, "2")
    fastUpload(sysAttachment.value).then((resp) => {
      // 轮询等待awaitHandleFile值变化
      const checkInterval = setInterval(() => {
        if (!awaitHandleFile.value) {
          // 清除轮询
          clearInterval(checkInterval)
          if (resp.code === 200) {
            const id = resp.data
            if (id) {
              fileList.value.some(item => {
                if (item.uid === file.uid) {
                  item.url = id
                  item.status = "done"
                }
              })
              // 处理双向绑定
              handleModelValue(file, fileList.value)
              emits("uploadSuccess", {file, fileList})
              uploading.value = false
            } else {
              handleUploadError(file, resp.msg)
            }
          } else {
            handleUploadError(file, resp.msg)
          }
        }
      }, 100)
    })
  }

  // 处理附件上传失败
  const handleUploadError = (file: UploadFile, errorMsg: string) => {
    fileList.value.forEach(item => {
      if (item.uid === file.uid) {
        item.status = "error"
      }
    })
    uploading.value = false
    emits("uploadError", file, errorMsg)
  }
  return {
    beforeUpload,
    handleChange,
    handleFastUpload,
    handleModelValue,
    handleUploadError
  }
}
const { beforeUpload, handleChange, handleFastUpload, handleModelValue, handleUploadError } = initUpload()

// 初始化分片上传
const initChunkUpload = () => {
  // 上传loading
  const uploading = ref<boolean>(false)
  // 分片上传提示
  const uploadTip = ref<string>()
  // 分片上传记录类型
  type UploadRecordType = {
    uploadId: string,
    status: "in_progress" | "completed",
    uploadedChunkSize: number,
    totalSize: number,
    chunkSize: number,
    attachmentId?: string,
  }

  // 开始进行分片上传
  const startChunkUpload = async (file: UploadFile) => {
    uploading.value = true
    // 1. 获取附件md5值
    const md5 = await handleCalculateHash(file) as string
    // 2. 判断是否进行附件上传
    let allow = await allowUpload(file, md5);
    // 允许上传附件
    if (allow) {
      // 3. 处理分片上传逻辑
      await handleChunkUpload(file, md5)
    } else {
      // 3 不允许分片上传 包含两种情况：1 同一附件有正在执行的上传任务；2 附件已上传完毕
      handleSyncChunkUploadStatus(file, md5)
    }
  }

  // 处理分片上传逻辑
  const handleChunkUpload = async (file: UploadFile, md5: string) => {
    // 获取浏览器缓存中记录的分片上传信息
    const record = localStorage.getItem(chunk_upload_prefix + md5)
    if (!record) { return }
    const recordObj: UploadRecordType = JSON.parse(record)

    // 1. 对附件进行分片
    const chunks = handleChunk(file, chunkSize).map((chunk, index) => ({index: index + 1, chunk}));
    // 2. 获取已上传的分片索引
    const uploadedIndexResp = await chunksUploadedIndex(recordObj.uploadId);
    if (uploadedIndexResp.code !== 200) {
      handleUploadError(file, uploadedIndexResp.msg)
      return
    }
    recordObj.chunkSize = chunks.length
    const uploadedIndexList = uploadedIndexResp.data

    // 3. 获取到需要上传的分片附件，并构建分片对象
    const needUploadChunks = chunks.filter((item) => !uploadedIndexList.includes(item.index)).map((item) => ({
      index: item.index,
      chunk: item.chunk,
      status: "pending", // 状态：等待中
    }))
    // 4. 创建各种计数器
    // 分片上传计数器
    let uploadedChunkNum = 0
    // 已上传大小计数器
    let uploadedChunkSize = uploadedIndexList.length * chunkSize * 1024 * 1024

    // 没有需要上传的情况，直接调用合并
    if (needUploadChunks.length === 0) {
      handleChunksMerge(file, recordObj, md5)
      return
    }

    // 5. 分片上传主体方法
    const uploadChunk = async (i: number) => {
      if (i >= needUploadChunks.length) return;
      // 状态改为进行中
      const {chunk, index} = needUploadChunks[i]
      needUploadChunks[i].status = "in_progress"
      // 调用分片上传接口
      const resp = await chunksUpload(chunk, recordObj.uploadId, md5, index, (bytes: number) => {
        // 上传状态显示，并实时更新上传进度
        uploadedChunkSize = bytes + uploadedChunkSize
        recordObj.uploadedChunkSize = Math.trunc(uploadedChunkSize / 1024 / 1024)
        recordObj.totalSize = Math.trunc(file.size ? file.size / 1024 / 1024 : 0 )
        localStorage.setItem(chunk_upload_prefix + md5, JSON.stringify(recordObj))
        uploadTip.value = `正在上传：${recordObj.uploadedChunkSize}MB / ${recordObj.totalSize}MB（${Math.trunc(recordObj.uploadedChunkSize / recordObj.totalSize * 100)}%）`
      })
      if (resp.code === 200) {
        // 修改状态为已上传
        needUploadChunks[i].status = "completed"
        // 计数器 + 1
        uploadedChunkNum++
        // 所有分片上传完成
        if (uploadedChunkNum == needUploadChunks.length) {
          // 处理分片合并
          handleChunksMerge(file, recordObj, md5)
        } else {
          // 7. 获取下一个等待中状态的数据进行上传
          const nextIndex = needUploadChunks.findIndex(item => item.status === "pending")
          if (nextIndex !== -1) {
            await uploadChunk(nextIndex)
          }
        }
      } else {
        message.error(resp.msg)
      }
    }

    // 6. 初始化chunkUploadCount个上传任务
    const queue: Promise<any>[] = [];
    for (let i = 0; i < Math.min(chunkUploadCount, needUploadChunks.length); i++) {
      queue.push(uploadChunk(i++));
    }

    await Promise.all(queue).catch((e) => {
      if (e instanceof ResponseError) {
        handleUploadError(file, e.msg)
      } else {
        handleUploadError(file, "分片上传失败")
        console.error(e)
      }
    })
  }

  // 同步分片上传状态
  const handleSyncChunkUploadStatus = (file: UploadFile, md5: string) => {
    const record = localStorage.getItem(chunk_upload_prefix + md5)
    if (record) {
      const interval = setInterval(() => {
        const recordObj: UploadRecordType = JSON.parse(record)
        uploadTip.value = `正在上传：${recordObj.uploadedChunkSize}MB / ${recordObj.totalSize}MB（${Math.trunc(recordObj.uploadedChunkSize / recordObj.totalSize * 100)}%）`
        // 检测到上传状态为completed时，执行附件秒传获取数据
        if (recordObj.status === "completed") {
          handleFastUpload(file, md5)
          clearInterval(interval);
        }
      }, 1000)
    } else {
      // 没有本地记录直接调用附件秒传
      handleFastUpload(file, md5)
    }
  }

  // 处理分片
  const handleChunk = (file: UploadFile, size: number): Blob[] => {
    const chunks:Blob[] = []
    if (file.size) {
      size = size * 1024 * 1024
      for (let i = 0; i < file.size; i = size + i) {
        chunks.push((file as any).slice(i, size + i))
      }
    }
    return chunks;
  }

  // 计算附件哈希
  const handleCalculateHash = (file: UploadFile) => {
    const chunks = handleChunk(file, 10)
    return new Promise(resolve => {
      // 通过webWorker后台处理hash计算，防止ui阻塞
      const worker = new Worker(new URL("./HashWorker.ts", import.meta.url), {type: "module"})
      // 接收hash计算完成后的结果
      worker.onmessage = (event) => {
        const resp = event.data
        if (typeof resp === "string") {
          resolve(resp)
          worker.terminate()
        } else {
          uploadTip.value = `正在扫描附件（${resp}%）`
        }
      }
      worker.postMessage(chunks)
    })
  }

  // 是否允许上传
  const allowUpload = async (file: UploadFile, md5: string): Promise<boolean> => {
    return new Promise((resolve, reject) => {
      const record = localStorage.getItem(chunk_upload_prefix + md5)
      let needFetch = false
      if (record) {
        const uploadRecord: UploadRecordType = JSON.parse(record)
        // 缓存对象为上传中状态，检查当前正在进行的请求，post:开头，包含md5是否存在，存在即处于正在上传状态，不存在即为中途断开状态
        if (uploadRecord.status === "in_progress") {
          resolve(![... currentRequests].some(url => url.startsWith("post:") && url.includes("?lh+attachment_md5=" + md5)))
        } else {
          // 已完成上传，从数据库查询对应信息
          needFetch = true
        }
      } else {
        // 记录不存在，从数据库查询对应信息
        needFetch = true
      }

      if (needFetch) {
        // 根据md5 查询数据库数据
        existsAttachmentByMd5(md5, file.name).then(async resp => {
          if (resp.code === 200) {
            // 数据存在，返回false
            if (resp.data) {
              resolve(false)
            } else {
              await _initChunkUploadStorage()
              resolve(true)
            }
          } else {
            await _initChunkUploadStorage()
            resolve(true)
            console.error(resp.msg, "为业务正常进行，继续上传附件")
          }
        }).catch(e => {
          reject(e)
        })
        // 新建localStorage缓存数据
        async function _initChunkUploadStorage() {
          handleSysAttachment(file, md5, "1")
          // 从后端获取updateId
          const resp = await chunksUploadStart(sysAttachment.value)
          if (resp.code === 200) {
            const data = resp.data
            localStorage.setItem(chunk_upload_prefix + md5, JSON.stringify({
              uploadId: data.uploadId,
              attachmentId: data.attachmentId,
              status: "in_progress",
              uploadedChunkSize: 0,
              totalSize: 0,
              chunkSize: 0
            } as UploadRecordType))
          } else {
            message.error(resp.msg)
            handleUploadError(file, resp.msg)
          }
        }
      }
    })
  }

  // 处理附件合并
  const handleChunksMerge = (file: UploadFile, recordObj: UploadRecordType, md5: string) => {
    uploadTip.value = "正在进行数据合并"
    chunksMerge({id: recordObj.attachmentId, originalName: file.name, md5: md5, uploadId:  recordObj.uploadId}, recordObj.chunkSize).then((resp) => {
      if (resp.code === 200) {
        // 上传成功后删除浏览器缓存记录
        localStorage.removeItem(chunk_upload_prefix + md5)
        // fileList重新赋值
        fileList.value.some(item => {
          if (item.uid === file.uid) {
            item.url = resp.data
            item.status = "done"
          }
        })
        // 处理双向绑定
        handleModelValue(file, fileList.value)
        emits("uploadSuccess", {file, fileList})
      } else {
        handleUploadError(file, resp.msg)
      }
    }).catch((e) => {
      if (e instanceof ResponseError) {
        handleUploadError(file, e.msg)
      } else {
        handleUploadError(file, "附件合并失败")
        console.error(e)
      }
    }).finally(() => {
      uploading.value = false
    })
  }
  return {
    uploading,
    uploadTip,
    handleCalculateHash,
    startChunkUpload
  }
}
const { uploading, uploadTip, handleCalculateHash, startChunkUpload } = initChunkUpload()

// 初始化预览
const initPreview = () => {
  const previewType = ref<'image' | 'video' | 'other'>()
  // 预览model
  const previewVisible = ref<boolean>(false)
  // 预览title
  const previewTitle = ref<string>()
  // 预览url
  const previewURL = ref<string>()
  // 处理预览
  const handlePreview = async (file: UploadFile) => {
    if (file.status === "error") {
      message.error("附件异常，无法预览或下载")
      return
    }
    if (file.type || file.name) {
      // 获取附件后缀名
      const extension = file.name.split('.').pop()?.toLowerCase() || ""
      // 获取组件返回的附件类型
      const type = file.type?.split("/")[0] || ""
      // 通过组件返回类型和附件后缀联合判断附件类型
      if (type === "image" || imageExtensions.includes(extension)) {
        previewType.value = "image"
      } else if (type === "video" || videoExtensions.includes(extension)) {
        previewType.value = "video"
      } else {
        previewType.value = 'other'
      }
    } else {
      previewType.value = 'other'
    }
    // 获取url
    let url = file.thumbUrl
    if (!url?.startsWith(baseAPI)) {
      const resp = await getDownloadURL(file.url as string)
      if (resp.code === 200) {
        url = handleThumbUrl(resp.data)
        // 为fileList中thumbUrl赋值
        fileList.value.some(item => {
          if (item.url === file.url) {
            item.thumbUrl = url
          }
        })
      }
    }
    // 图片视频进行弹窗预览
    if (previewType.value === 'image' || previewType.value === 'video') {
      previewVisible.value = true
      previewTitle.value = file.name
      previewURL.value = url
    } else {
      // 其他类型直接下载
      if (url) {
        download(url, file.name)
      }
    }
  }

  // 关闭预览
  const handleCancel = () => {
    previewVisible.value = false
  }

  // 处理显示缩略图显示
  const handleShowThumbImage = (file: UploadFile) => {
    // 获取附件后缀名
    const extension = file.name.split('.').pop()?.toLowerCase() || ""
    return imageExtensions.includes(extension);
  }

  // 处理预览URL
  const handleThumbUrl = (thumbUrl?: string): string => {
    if (!thumbUrl) {
      return "";
    }
    // 由http或baseAPI开头直接返回
    if (thumbUrl.startsWith("http") || thumbUrl?.startsWith(baseAPI)) {
      return thumbUrl;
    }
    // 最后拼接 baseAPI
    return baseAPI + thumbUrl;
  }
  return {
    previewVisible,
    previewTitle,
    previewURL,
    previewType,
    handlePreview,
    handleCancel,
    handleShowThumbImage,
    handleThumbUrl
  }
}
const { previewVisible, previewTitle, previewURL, previewType, handlePreview, handleCancel, handleShowThumbImage, handleThumbUrl } = initPreview()

// 处理附件删除
const handleRemove = async (file: UploadFile) => {
  if (file && file.url) {
    try {
      const resp = await deleteFromBusiness(file.url)
      if (resp.code === 200) {
        emits("remove", {id: file.url, status: "success"})
      } else {
        emits("remove", {id: file.url, status: "error"})
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    }
  }
}

// 监听双向绑定
watch(() => modelValue, (newVal, oldValue) => {
  // 已执行过双向绑定回显直接返回
  if (vModelComplete) {
    return;
  }

  // 1. 求newVal, oldValue集合间的交集，交集为空，表示为双向绑定触发的watch，执行initVModel()
  const intersection = newVal?.split(",").filter(nv => oldValue.split(",")?.includes(nv))
  // 2. 双向绑定数据加载前上传的集合
  const newUpload = fileList.value.filter(fl => fl.url && newVal.includes(fl.url))

  // 符合 1、2 条件并 newVal 存在，即为双向绑定加载的数据，执行initVModel
  if (intersection.length === 0 && newUpload.length === 0 && newVal) {
    initVModel(newVal, oldValue)
  }
})

onMounted(() => {
  if (modelValue) {
    initVModel(modelValue)
  }
})
</script>

<style>
.attachment-upload-preview-mask {
  border-radius: 8px;
}
</style>