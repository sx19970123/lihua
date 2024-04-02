import request from "@/utils/request";

export const viewTab = (menuId:string , affix: string , star: string) => {
 return request({
     url: '/system/viewTab',
     method: 'post',
     data: {
         menuId: menuId,
         affix: affix,
         star: star
     }
 })
}
