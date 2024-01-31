import { defineStore } from "pinia";

export const useTheme = defineStore('theme',{
    state() {
        const theme: string = 'light'
        return {
            theme
        }
    },
    actions: {
        light() {
            this.$state.theme = 'light'
        },
        dark() {
            this.$state.theme = 'dark'
        }
    }
})
