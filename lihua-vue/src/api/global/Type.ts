
/**
 * 接收接口的返回值
 */
export type ResponseType<T> = {
    code: number;
    msg: string;
    data: T;
}

/**
 * 接收分页接口的返回值
 */
export type PageResponseType<T> = {
    current: number,
    pages: number,
    records: Array<T>,
    size: number,
    total: number
}

/**
 * 接收Map类型返回值
 */
export type MapResponseType<String,V> = {
    [key: string]: V[];
}

/**
 * excel 导入返回结果
 */
export interface ExcelImportResult {
    // 是否全部导入成功
    allSuccess: boolean,
    // 读取到的数量
    readCount: number,
    // 导入成功的数量
    successCount: number,
    // 导入失败的数量
    errorCount: number,
    // 导入失败excel文件路径
    errorExcelPath: string
}

/**
 *  通用的树形结构
 *  对应 ant design 中 a-cascader、a-tree-select、a-tree 组件数据结构
 */
export interface CommonTree {
    // 标签
    label: string,
    // 值
    value: string,
    // 子节点
    children: Array<CommonTree>,
    // id，用于后端构建树形结构
    id: string,
    // 父级id，用于后端构建树形结构
    parentId: string,
    // 类型，可自定义扩展
    type: string
}