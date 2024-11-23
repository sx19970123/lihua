// 函数防抖，传入函数及等待时间，返回的函数具有函数防抖
export const debounce = (fun: Function, wait: number | undefined = 300) => {
    let timeout: ReturnType<typeof setTimeout> | null = null;
    return function (this: unknown, ... args: any[]) {
        if (timeout) {
            clearTimeout(timeout);
        }
        timeout = setTimeout(() => {
            fun.apply(this, args)
        }, wait);
    }
}