/**
 * 接收接口的返回值
 */
export type ResponseType<T> = {
    code: number;
    msg: string;
    data: T;
}



