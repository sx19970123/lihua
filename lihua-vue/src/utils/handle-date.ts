import dayjs from "dayjs";

/**
 * 传入时间字符串：2024-06-02 14:59 处理为今天/昨天/前天
 * @param time
 */
export const handleTime = (time: string) => {
    if (time) {
        if (time.substring(0, 10) === dayjs().format('YYYY-MM-DD')) {
            return '今天 ' + time.substring(11)
        } else if (time.substring(0,10) === dayjs().subtract(1,'day').format('YYYY-MM-DD')) {
            return '昨天 ' + time.substring(11)
        } else if (time.substring(0,10) === dayjs().subtract(2,'day').format('YYYY-MM-DD')) {
            return '前天 ' + time.substring(11)
        } else {
            return time
        }
    }
}