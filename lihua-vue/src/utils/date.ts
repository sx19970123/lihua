export const format = (data:Date,fmt:string): string => {
    return data.format(fmt);
}

/**
 * 获取相对于date的时间日期
 * @param date 某一日期
 * @param fmt 日期格式
 * @param offset 偏移量，+1就是一天后；-1就是前一天
 */
export const relativeDate = (date: Date, fmt: string, offset: number): string => {
    const previousDay =  new Date(date)
    previousDay.setDate(date.getDate() + offset)
    return previousDay.format(fmt)
}

// Date 原型添加format 方法
Date.prototype.format = function(fmt:string) {
    let o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}
