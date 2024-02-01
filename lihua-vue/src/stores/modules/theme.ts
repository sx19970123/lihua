import { defineStore } from "pinia";
import { theme } from "ant-design-vue";

export const useTheme = defineStore('theme',{
    state() {
        const dataDark: string = 'light'
        const dataTheme: string = 'light'
        const themeConfig = {
            token: {
                colorPrimary: '#2196F3'
            },
            algorithm: theme.defaultAlgorithm
        }
        return {
            dataDark,
            dataTheme,
            themeConfig
        }
    },
    actions: {
        light() {
            this.$state.dataDark = 'light'
            this.$state.themeConfig.algorithm = theme.defaultAlgorithm
            this.changeDocumentElement()
        },
        dark() {
            this.$state.dataDark = 'dark'
            this.$state.themeConfig.algorithm = theme.darkAlgorithm
            this.changeDocumentElement()
        },
        changeTheme(color:string) {
            this.$state.dataTheme = color
        },
        changeDocumentElement() {
            document.documentElement.setAttribute("data-dark",this.$state.dataDark)
            document.documentElement.setAttribute("data-theme",this.$state.dataTheme)
        }
    },
})
