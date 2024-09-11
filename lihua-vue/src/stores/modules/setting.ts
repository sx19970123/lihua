import {defineStore} from "pinia";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import {findList, insert} from "@/api/system/setting/Setting.ts";

export const useSettingStore = defineStore('setting', {
    state:() =>{
        const settings: SystemSetting[] = []
        return {
            settings
        }
    },
    actions: {
        // 初始化系统配置
        async init() {
           const resp = await findList()
            if (resp.code === 200) {
                this.settings = resp.data
            }
        },
        // 保存系统配置
        async save(setting: SystemSetting) {
            // 插入系统配置
            const resp = await insert(setting)
            // 插入完成后重新初始化
            await this.init()
            return resp
        },
        // 根据组件名称获取配置信息
        getSetting<T>(componentName: string) {
            const targetSetting = this.settings.filter(item => item.settingComponentName === componentName)
            if (targetSetting.length === 0) {
                return undefined
            }
            return JSON.parse(targetSetting[0].settingJson) as T
        }
    }
})

