import { defineStore } from "pinia";
import { theme } from "ant-design-vue";

export const useThemeStore = defineStore('theme',{
    state() {
        /**
         * 暗色模式
         */
        const dataDark: boolean = false

        // 顶部栏背景颜色
        const layoutBackgroundColor: string = 'rgba(255,255,255,1)'

        /**
         * 布局类型
         */
        const layoutType: string = 'sider-header'

        // 导航模式
        const siderMode: string = 'inline' //  horizontal

        /**
         * 样式主题
         */
        // 主要颜色
        const colorPrimary: string = 'rgb(22, 119, 255)'

        // 侧边栏类型
        const siderBackgroundColor: string = 'rgba(255,255,255,1)'

        // 磨砂玻璃效果
        const groundGlass: boolean = false

        // 固定头部
        const affixHead: boolean = true

        // 显示多窗口标签
        const showViewTags: boolean = true

        // 侧边颜色
        const siderTheme: string = 'light'

        // 侧边宽度
        const siderWith: number = 200


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
            layoutBackgroundColor,
            siderBackgroundColor,
            siderMode,
            siderWith,
            themeConfig
        }
    },
    actions: {
        // 初始化样式
        init() {
            this.changeDataDark()
            this.changeLayoutType()
            this.changeShowViewTags(true)
            this.changeAffixHead()
        },
        // 暗色模式
        changeDataDark() {
            this.changeDocumentElement()
            let backgroundColor: string = ''
            // 暗色模式下，顶部颜色为黑色、侧边颜色为黑色，透明度根据磨砂效果控制
            if (this.$state.dataDark) {
                this.siderTheme = 'light'
                if (this.$state.groundGlass) {
                    backgroundColor = 'rgba(20,20,20,0.6)'
                } else {
                    backgroundColor = 'rgba(20,20,20,1)'
                }
                this.changeLayoutBackgroundColor(backgroundColor)
                this.$state.siderBackgroundColor = this.$state.layoutBackgroundColor
                this.$state.themeConfig.algorithm = theme.darkAlgorithm
            }
            // 亮色模式下，顶部颜色为白色、侧边颜色为白色
            else {
                if (this.$state.groundGlass) {
                    backgroundColor = 'rgba(255,255,255,0.6)'
                } else {
                    backgroundColor = 'rgba(255,255,255,1)'
                }
                this.changeLayoutBackgroundColor(backgroundColor)
                this.$state.themeConfig.algorithm = theme.defaultAlgorithm
            }

        },

        // 布局类型
        changeLayoutType() {
            const value = this.$state.layoutType
            // 顶部导航默认关闭多标签
            if (value === 'header-content') {
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
        // 导航类型
        changeSiderMode(value: string) {
            this.$state.siderMode = value
        },
        // 切换主要颜色
        changeColorPrimary() {
            this.themeConfig.token.colorPrimary = this.$state.colorPrimary
        },
        // 修改侧边栏颜色
        changeSiderTheme() {
            if (this.$state.siderTheme === 'dark') {
                this.siderBackgroundColor = 'rgba(20,20,20,1)'
            } else {
                this.siderBackgroundColor = 'rgba(255,255,255,1)'
            }
        },
        // 修改磨砂玻璃效果
        changeGroundGlass() {
            if (this.$state.groundGlass) {
                document.documentElement.setAttribute("data-ground-glass",'glass')
                if (!this.$state.dataDark) {
                    this.changeLayoutBackgroundColor('rgba(255,255,255,0.6)')
                } else {
                    this.changeLayoutBackgroundColor('rgba(20,20,20,0.6)')
                }
            } else {
                document.documentElement.setAttribute("data-ground-glass",'no-glass')
                if (!this.$state.dataDark) {
                    this.changeLayoutBackgroundColor('rgba(255,255,255,1)')
                } else {
                    this.changeLayoutBackgroundColor('rgba(20,20,20,1)')
                }
            }
        },
        // 修改固定头部
        changeAffixHead() {
            if (this.$state.affixHead) {
                document.documentElement.setAttribute("data-head-affix",'affix')
            } else {
                document.documentElement.setAttribute("data-head-affix",'un-affix')
            }
        },
        // 切换顶部栏背景颜色
        changeLayoutBackgroundColor(value: string) {
            this.$state.layoutBackgroundColor = value
        },
        // 修改html标签，标记当前颜色模式
        changeDocumentElement() {
            document.documentElement.setAttribute("data-dark",this.$state.dataDark ? 'dark' : 'light')
        }
    },
})
