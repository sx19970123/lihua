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

/**
 * 获取默认密码
 */
export const getDefaultPassword = () => {
    return request<string>({
        url: "system/setting/defaultPassword",
        method: "GET",
    })
}

/**
 * 是否开启验证码
 */
export const enableCaptcha = () => {
    return request<boolean>({
        url: "system/setting/base/enableCaptcha",
        method: "GET",
    })
}

/**
 * 是否开启灰色模式
 */
export const enableGrayMode = () => {
    return request<boolean>({
        url: "system/setting/base/enableGrayMode",
        method: "GET",
    })
}

/**
 * 是否开启自助注册
 */
export const enableSignUp = () => {
    return request<boolean>({
        url: "system/setting/base/enableSignUp",
        method: "GET",
    })
}