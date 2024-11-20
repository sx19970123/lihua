import {defineStore} from "pinia";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import {querySysSettingByComponentName, insert} from "@/api/system/setting/Setting.ts";
import {message} from "ant-design-vue";
import {ResponseError, type ResponseType} from "@/api/global/Type.ts";

export const useSettingStore = defineStore('setting', {
    state:() =>{
        return {}
    },
    actions: {
        // 保存系统配置
        save(setting: SystemSetting):Promise<ResponseType<String>> {
            return new Promise((resolve, reject) => {
                insert(setting).then(resp => {
                    resolve(resp as ResponseType<String>)
                }).catch((e) => {
                    if (e instanceof ResponseError) {
                        message.error(e.msg)
                    } else {
                        console.error(e)
                    }
                    reject(e)
                })
            })
        },
        // 根据组件名称获取配置信息
        async getSetting<T>(componentName?: string) {
            if (!componentName) {
                return undefined;
            }
            try {
                const resp = await querySysSettingByComponentName(componentName)
                if (resp.code === 200) {
                    return JSON.parse(resp.data.settingJson) as T
                } else {
                    message.error(resp.msg)
                    return undefined
                }
            } catch (e) {
                console.error(e)
                return undefined
            }
        }
    }
})

