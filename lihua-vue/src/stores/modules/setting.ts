import {defineStore} from "pinia";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import {findList, findSysSettingByComponentName, insert} from "@/api/system/setting/Setting.ts";
import {message} from "ant-design-vue";

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
        async getSetting<T>(componentName?: string) {
            if (!componentName) {
                return undefined;
            }
            const resp = await findSysSettingByComponentName(componentName)
            if (resp.code === 200) {
                return JSON.parse(resp.data.settingJson) as T
            } else {
                message.error(resp.msg)
            }
            return undefined
        }
    }
})

