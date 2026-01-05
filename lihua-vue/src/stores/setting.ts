import {defineStore} from "pinia";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import {insert, querySysSettingByComponentName} from "@/api/system/setting/Setting.ts";
import {message} from "ant-design-vue";
import {ResponseError, type ResponseType} from "@/api/global/Type.ts";

export const useSettingStore = defineStore('setting', {
    state:() => {
        // 配置集合
        const map = new Map();
        return {
            map
        }
    },
    actions: {
        // 保存系统配置
        save(setting: SystemSetting):Promise<ResponseType<String>> {
            return new Promise((resolve, reject) => {
                insert(setting).then(resp => {
                    this.map.delete(setting.settingComponentName)
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
            // 从state中获取配置信息
            if (this.map.has(componentName)) {
                return JSON.parse(this.map.get(componentName)) as T
            }
            try {
                const resp = await querySysSettingByComponentName(componentName)
                if (resp.code === 200) {
                    // 判断返回的settingJson是否存在
                    const data = resp.data?.settingJson
                    if (data) {
                        this.map.set(componentName, data)
                        return JSON.parse(data) as T
                    }
                    return undefined;
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

