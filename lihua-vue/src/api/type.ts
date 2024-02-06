/**
 * 接收接口的返回值
 */
interface ResponseType<T> {
    code: number;
    msg: string;
    data?: T;
}

export {
    ResponseType
}

