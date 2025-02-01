export interface SysAttachment {
    /** 主键 */
    id?: string;
    /** 文件存储名 */
    storageName?: string;
    /** 文件原名称 */
    originalName?: string;
    /** 文件扩展名 */
    extensionName?: string;
    /** 文件保存路径 */
    path?: string;
    /** 业务编码（默认文件上传时所在的路由名称） */
    businessCode?: string;
    /** 业务名称（默认文件上传时所在的菜单名称） */
    businessName?: string;
    /** 文件类型 */
    type?: string;
    /** 上传状态 上传成功、上传失败 */
    uploadStatus?: string;
    /** 文件存储位置 如：本地、云存储等 */
    storageLocation?: string;
    /** 上传人id */
    createId?: string;
    /** 上传时间 */
    createTime?: Date;
    /** 删除标识 */
    delFlag?: string;
    /** 上传失败原因 */
    errorMsg?: string;
}