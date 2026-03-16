import {defineStore} from "pinia";
import {
    enableCaptcha,
    enableGrayMode,
    enableSignUp,
    getDefaultPassword,
    getSysSettingByKey
} from "@/api/system/setting/Setting.ts";
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
        const enableSignUp: boolean = false;

        return {
            enableCaptcha,
            enableGrayMode,
            enableSignUp
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
                return JSON.parse(setting.json) as T
            } else {
                message.error(resp.msg)
            }
        },
        /**
         * 初始化基础设置
         */
        async initBaseSetting() {
            await this.fetchEnableGrayMode()
            await this.fetchEnableSignUp()
            await this.fetchEnableCaptcha()
        },
        /**
         * 获取默认密码
         */
        async fetchDefaultPassword() {
            const resp = await getDefaultPassword()
            if (resp.code === 200) {
                return resp.data
            } else {
                message.error(resp.msg)
            }
        },
        /**
         * 获取是否开启验证码
         */
        async fetchEnableCaptcha() {
            const resp = await enableCaptcha()
            if (resp.code === 200) {
                this.$state.enableCaptcha = resp.data
            }
        },
        /**
         * 获取是否开启灰色模式
         */
        async fetchEnableGrayMode() {
            const resp = await enableGrayMode()
            if (resp.code === 200) {
                this.$state.enableGrayMode = resp.data
            }
        },
        /**
         * 获取是否开启自助注册
         */
        async fetchEnableSignUp() {
            const resp = await enableSignUp()
            if (resp.code === 200) {
                this.$state.enableSignUp = resp.data
            }
        }
    }
})

