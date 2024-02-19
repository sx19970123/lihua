import { defineStore } from "pinia";
import { theme } from "ant-design-vue";

export const useThemeStore = defineStore('theme',{
    state() {
        /**
         * 暗色模式
         */
        const dataDark: boolean = false

        /**
         * 顶部栏背景颜色
         */
        const layoutBackgroundColor: string = 'rgba(255,255,255,1)'
        /**
         * 布局类型 sider-header / header-sider / header-content
         */
        const layoutType: string = 'sider-header'

        /**
         * 导航模式 inline / horizontal
         */
        const siderMode: string = 'inline'

        /**
         * 主要颜色
         */
        const colorPrimary: string = 'rgb(22, 119, 255)'

        /**
         * 侧边栏背景颜色
         */
        const siderBackgroundColor: string = 'rgba(255,255,255,1)'

        /**
         * 磨砂玻璃效果
         */
        const groundGlass: boolean = false

        /**
         * 固定头部
         */
        const affixHead: boolean = true

        /**
         * 显示多窗口标签
         */
        const showViewTags: boolean = true

        /**
         * 侧边颜色 light / dark
          */
        const siderTheme: string = 'light'

        /**
         * 侧边宽度
         */
        const siderWith: number = 200

        /**
         * 原侧边宽度，用于调整侧边栏时保存临时变量
         */
        const originSiderWith: number = 200

        /**
         * 切换路由时的过渡动画 zoom / fade / breathe / top / down / switch / trick
         */
        const routeTransition: string = 'zoom'

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
            originSiderWith,
            routeTransition,
            themeConfig
        }
    },
    actions: {
        // 初始化样式
        init(themeJson: string | null) {
            this.initState(themeJson)
            this.changeDataDark()
            this.changeLayoutType()
            this.changeAffixHead()
            this.changeGroundGlass()
        },
        // 通过json数据初始化state
        initState(themeJson: string | null) {
            if (!themeJson) {
                return
            }
            let state = JSON.parse(themeJson);
            for (let stateKey in state) {
                for (let $stateKey in this.$state) {
                    if (stateKey === $stateKey) {
                        // 使用类型断言告诉 TypeScript $stateKey 是 $state 对象的一个键
                        (this.$state as any)[$stateKey] = state[stateKey];
                    }
                }
            }
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
            // 顶部导航默认关闭多标签
            if (this.$state.layoutType === 'header-content') {
                this.changeSiderMode('horizontal')
            } else {
                this.changeSiderMode('inline')
            }
            // 修改页面标识
            document.documentElement.setAttribute("layout-type", this.$state.layoutType)
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
            this.changeDocumentElement()
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
            document.documentElement.setAttribute("data-color", this.$state.colorPrimary)
        },
        // 折叠侧边栏
        foldSiderWidth()  {
            this.originSiderWith = this.siderWith
            this.siderWith = 80
        },
        // 展开侧边栏
        unfoldSiderWidth() {
            this.siderWith = this.originSiderWith
        }
    },
})
