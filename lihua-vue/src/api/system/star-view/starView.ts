import request from "@/utils/request";

export const starView = (routerPathKey:string ,affix: string ,star: string) => {
 return request({
     url: '/system/star/view',
     method: 'post',
     data: {
         routerPathKey: routerPathKey,
         affix: affix,
         star: star
     }
 })
}
