import request from "@/utils/Request.ts";
import type {StarViewType} from "@/api/system/view-tab/type/SysViewTab.ts";

export const viewTab = (menuId:string , affix: string , star: string) => {
 return request<StarViewType>({
     url: '/system/viewTab',
     method: 'post',
     data: {
         menuId: menuId,
         affix: affix,
         star: star
     }
 })
}
