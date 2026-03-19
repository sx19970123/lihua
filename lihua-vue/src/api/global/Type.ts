/**
 * 接收接口的返回值
 */
export interface ResponseType<T> {
    code: number
    msg: string
    data: T
}

/**
 * 接收分页接口的返回值
 */
export interface PageResponseType<T> {
    current: number
    pages: number
    records: Array<T>
    size: number
    total: number
}

/**
 * 接收Map类型返回值
 */
export interface MapResponseType<String,V> {
    [key: string]: V[]
}

/**
 * 基础模态框属性维护
 */
export interface BaseModalActiveType {
    // 模态框开关
    open: boolean
    // 点击保存按钮加载
    saveLoading: boolean
    // 模态框标题
    title: string
}

/**
 *  请求异常时返回值
 *  catch中无法对 error 进行有效的类型判断，
 *  定义 ResponseError 类可直接使用 instanceof 进行错误类型判断
 */
export class ResponseError implements ResponseErrorType {
    constructor(code: number, msg: string) {
        this.code = code
        this.msg = msg
    }
    code: number;
    msg: string;
}

/**
 * 描述响应错误类型
 */
export interface ResponseErrorType {
    code: number,
    msg: string
}