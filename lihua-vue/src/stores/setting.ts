import {defineStore} from "pinia";
import {getSysSettingByKey} from "@/api/system/setting/Setting.ts";
import {message} from "ant-design-vue";

export const useSettingStore = defineStore('setting', {
    state:() => {

        /**
         * 是否启用验证码
         */
        const enableCaptcha: boolean = true;

        /**
         * 是否启用灰色模式
         */
        const enableGrayMode: boolean = false;

        /**
         * 是否启用自助注册
         */
        const enableSignIn: boolean = false;


        return {
            enableCaptcha,
            enableGrayMode,
            enableSignIn,
        }
    },
    actions: {
        /**
         * 获取配置信息
         */
        async getSettingInfo<T> (key?: string)  {
            if (!key) {
                return
            }

            // 获取系统配置
            const resp = await getSysSettingByKey(key)
            if (resp.code === 200) {
                const setting = resp.data
                if (!setting) {
                    return
                }

                return {
                    id: setting.id,
                    settingKey: key,
                    data: JSON.parse(setting.json) as T
                }
            } else {
                message.error(resp.msg)
            }
        }
    }
})

