import request from "@/utils/Request.ts";
import type {SysSetting} from "@/api/system/setting/type/SysSetting.ts";

/**
 * 保存设置
 */
export const save = (setting :SysSetting) => {
    return request<String>({
        url: "system/setting",
        method: "POST",
        data: setting
    })
}

/**
 * 根据组件名称获取配置
 */
export const getSysSettingByKey = (key: string) => {
    return request<SysSetting>({
        url: "system/setting/" + key,
        method: "GET",
    })
}