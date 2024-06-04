
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

