import { defineStore } from "pinia";
import { theme } from "ant-design-vue";

export const useTheme = defineStore('theme',{
    state() {
        // 布局类型
        const layoutType: string = 'head-sider'
        // 显示多窗口页面
        const showViewTags: boolean = true
        // 暗色模式
        const dataDark: string = 'light'
        // 主要颜色
        const colorPrimary: string = 'light'
        // 侧边颜色
        const siderTheme: string = 'light'
        // 磨砂玻璃效果
        const groundGlass: boolean = false
        // 固定头部
        const affixHead: boolean = true
        // 导航模式
        const siderMode: string = 'inline' //  horizontal
        // 主题配置
        const headBackgroundColor: string = 'rgba(255,255,255,0.6)'

        const themeConfig = {
            token: {
                colorPrimary: '#2196F3'
            },
            algorithm: theme.defaultAlgorithm
        }
        return {
            layoutType,
            showViewTags,
            dataDark,
            colorPrimary,
            siderTheme,
            groundGlass,
            affixHead,
            headBackgroundColor,
            siderMode,
            themeConfig
        }
    },
    actions: {
        // 修改布局类型
        changeLayoutType(value: string) {
            this.$state.layoutType = value
            // 顶部导航默认关闭多标签
            if (value === 'head-only') {
                this.changeShowViewTags(false)
                this.changeSiderMode('horizontal')
            } else {
                this.changeShowViewTags(true)
                this.changeSiderMode('inline')
            }
        },
        // 显示多窗口页面
        changeShowViewTags(value: boolean) {
            this.$state.showViewTags = value
        },
        // 暗色/亮色模式切换
        changeDataDark(value: string) {
            this.$state.dataDark = value
            this.changeDocumentElement()
            if (value === 'light') {
                this.changeHeadBackgroundColor('rgba(255,255,255,1)')
                this.$state.themeConfig.algorithm = theme.defaultAlgorithm
            } else {
                this.changeHeadBackgroundColor('rgba(20,20,20,1)')
                this.$state.themeConfig.algorithm = theme.darkAlgorithm
            }
        },
        // 切换主要颜色
        changeColorPrimary(value: string) {
            this.$state.colorPrimary = value
        },
        // 修改侧边栏颜色
        changeSiderTheme(value: string) {
            this.$state.siderTheme = value
        },
        // 修改磨砂玻璃效果
        changeGroundGlass(value: boolean) {
            this.$state.groundGlass = value
            if (this.$state.dataDark === 'light') {
                this.changeHeadBackgroundColor('rgba(255,255,255,0.6)')
            } else {
                this.changeHeadBackgroundColor('rgba(0,0,0,0.6)')
            }
        },
        // 修改固定头部
        changeAffixHead(value: boolean) {
            this.$state.affixHead = value
        },
        // 菜单模式
        changeSiderMode(value: string) {
            this.$state.siderMode = value
        },
        // 切换顶部栏背景颜色
        changeHeadBackgroundColor(value: string) {
            this.$state.headBackgroundColor = value
        },
        // 修改html标签，标记当前颜色模式
        changeDocumentElement() {
            document.documentElement.setAttribute("data-dark",this.$state.dataDark)
            document.documentElement.setAttribute("data-theme",this.$state.colorPrimary)
        }
    },
})
