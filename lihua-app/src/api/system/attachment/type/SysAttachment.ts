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
    /** 分片上传id */
    uploadId?: string;
    /** 业务编码（默认文件上传时所在的路由名称） */
    businessCode?: string;
    /** 业务名称（默认文件上传时所在的菜单名称） */
    businessName?: string;
    /** 文件大小 */
    size?: string;
    /** 文件类型 */
    type?: string;
    /** 上传方式 */
    uploadMode?: string;
    /** 上传状态 上传成功、上传失败 */
    status?: string;
    /** 文件存储位置 如：本地、云存储等 */
    storageLocation?: string;
    /** md5 */
    md5?: string;
    /** 上传人id */
    createId?: string;
    /** 上传时间 */
    createTime?: Date;
    /** 删除标识 */
    delFlag?: string;
    /** 上传失败原因 */
    errorMsg?: string;
    /** 原url（通过url上传有该字段） */
    url?: string;
}

export interface SysAttachmentDTO extends SysAttachment {

    /**
     * 创建时间集合
     */
    createTimeList?: Date[];

    /**
     * 当前页数
     */
    pageNum: number;

    /**
     * 每页记录数
     */
    pageSize: number;

}

export interface SysAttachmentVO extends SysAttachment {
    /**
     * 上传用户昵称
     */
    uploadName?: string;
}