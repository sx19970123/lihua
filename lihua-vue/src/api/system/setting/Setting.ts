import request from "@/utils/Request.ts";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";

/**
 * 插入新设置
 * @param setting
 */
export const insert = (setting :SystemSetting) => {
    return request<String>({
        url: "system/setting",
        method: "POST",
        data: setting
    })
}

/**
 * 根据组件名称获取配置
 */
export const querySysSettingByComponentName = (componentName: string) => {
    return request<SystemSetting>({
        url: "system/setting/" + componentName,
        method: "GET",
    })
}