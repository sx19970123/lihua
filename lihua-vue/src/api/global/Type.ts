
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