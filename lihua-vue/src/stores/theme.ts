import { defineStore } from "pinia";
import { theme } from "ant-design-vue";
import settings from "@/settings";

export const useThemeStore = defineStore('theme',{
    state() {
        /**
         * 暗色模式
         */
        const isDarkTheme: boolean = settings.isDarkTheme

        /**
         * 跟随系统主题
         */
        const followSystemTheme: boolean = settings.followSystemTheme

        /**
         * 顶部栏背景颜色
         */
        const layoutBackgroundColor: string = settings.siderBackgroundColor

        /**
         * 布局类型 sider-header / header-sider / header-content
         */
        const layoutType: string = settings.layoutType

        /**
         * 组件大小 small/ middle / large
         */
        const componentSize: string = settings.componentSize

        /**
         * 导航模式 inline / horizontal
         */
        const siderMode: string = settings.siderMode

        /**
         * 菜单分组
         */
        const siderGroup: boolean = settings.siderGroup

        /**
         * 主要颜色
         * 组件中使用系统颜色不可直接取用该字段
         * 使用下面提供的getColorPrimary()方法进行获取
         */
        const colorPrimary: string = settings.themeConfig.token.colorPrimary

        /**
         * 通过ant提供的theme的主要颜色，针对暗色模式进行了颜色调整
         */
        const antColorPrimary: string = settings.themeConfig.token.colorPrimary

        /**
         * 侧边栏背景颜色
         */
        const siderBackgroundColor: string = settings.layoutBackgroundColor

        /**
         * 磨砂玻璃效果
         */
        const groundGlass: boolean = settings.groundGlass

        /**
         * 固定头部
         */
        const affixHead: boolean = settings.affixHead

        /**
         * 显示多窗口标签
         */
        const showViewTabs: boolean = settings.showViewTabs

        /**
         * 侧边颜色 light / dark
          */
        const siderTheme: string = settings.siderTheme

        /**
         * 侧边宽度
         */
        const siderWith: number = settings.siderWith

        /**
         * 原侧边宽度，用于调整侧边栏时保存临时变量
         */
        const originSiderWith: number = settings.originSiderWith

        /**
         * 切换路由时的过渡动画 zoom / fade / breathe / top / down / switch / trick
         */
        const routeTransition: string = settings.routeTransition

        /**
         * 灰色模式
         */
        const grayModel: string = settings.grayModel

        /**
         * ant 主题配置
         */
        const themeConfig = settings.themeConfig

        /**
         * 是否从服务端加载完毕
         * 系统主题默认从settings中读取默认值，用户登录后会从服务器获取用户定义的主题信息
         * 当获取到服务器主题后会将此属性设置为 true
         */
        const isServerLoad = false

        return {
            layoutType,
            componentSize,
            showViewTabs,
            isDarkTheme,
            followSystemTheme,
            colorPrimary,
            antColorPrimary,
            siderTheme,
            groundGlass,
            affixHead,
            layoutBackgroundColor,
            siderBackgroundColor,
            siderMode,
            siderGroup,
            siderWith,
            originSiderWith,
            routeTransition,
            grayModel,
            themeConfig,
            isServerLoad
        }
    },
    actions: {
        // 初始化样式
        init(themeJson?: string) {
            this.initState(themeJson)
            this.changeDataDark()
            this.changeLayoutType()
            this.changeAffixHead()
            this.changeGroundGlass()
            this.changeShowViewTabs()
            this.$state.isServerLoad = true
        },
        // 通过json数据初始化state
        initState(themeJson?: string) {
            if (!themeJson) {
                return
            }
            try {
                let state = JSON.parse(themeJson);
                for (let stateKey in state) {
                    for (let $stateKey in this.$state) {
                        if (stateKey === $stateKey) {
                            // 使用类型断言告诉 TypeScript $stateKey 是 $state 对象的一个键
                            (this.$state as any)[$stateKey] = state[stateKey];
                        }
                    }
                }
                this.$state.isDarkTheme = localStorage.getItem("data-theme") === 'dark'
            } catch (e) {
                console.error('初始化主题失败，使用默认主题',e)
                return;
            }
            // 小窗模式下ViewTabs隐藏，设置导航模式为sider-header
            if (window.location.href.includes("miniWindow=true")) {
                this.$state.showViewTabs = false
                this.$state.layoutType = "sider-header"
            }
        },
        // 切换暗色模式，isSync：App.vue组件中用于同步标签页暗色模式
        changeDataDark(isSync?: boolean) {
            let backgroundColor: string = ''
            // 暗色模式下，顶部颜色为黑色、侧边颜色为黑色，透明度根据磨砂效果控制
            if (this.$state.isDarkTheme) {
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
            this.changeSiderTheme()
            if (isSync !== true) {
                localStorage.setItem('data-theme',this.$state.isDarkTheme ? 'dark' : 'light')
            }
        },
        // 布局类型
        changeLayoutType() {
            if (this.$state.layoutType === 'header-content') {
                this.changeSiderMode('horizontal')
            } else {
                this.changeSiderMode('inline')
            }
            // 修改页面标识
            document.documentElement.setAttribute("layout-type", this.$state.layoutType)
        },
        // 显示多窗口页面
        changeShowViewTabs() {
            const viewTabs = this.$state.showViewTabs
            if (viewTabs) {
                document.documentElement.setAttribute("view-tabs", "show")
            } else {
                document.documentElement.setAttribute("view-tabs", "hide")
            }
        },
        // 导航类型
        changeSiderMode(value: string) {
            this.$state.siderMode = value
        },
        // 修改导航宽度时同时修改原始值
        changeSiderWidth() {
          this.$state.originSiderWith = this.$state.siderWith
        },
        // 切换主要颜色
        changeColorPrimary() {
            this.themeConfig.token.colorPrimary = this.$state.colorPrimary
        },
        // 修改侧边栏颜色
        changeSiderTheme() {
            if (this.$state.siderTheme === 'dark') {
                this.siderBackgroundColor = 'rgba(20,20,20,1)'
                document.documentElement.setAttribute("sider-dark", "dark")
            } else {
                this.siderBackgroundColor = 'rgba(255,255,255,1)'
                document.documentElement.setAttribute("sider-dark", "light")
            }
        },
        // 修改磨砂玻璃效果
        changeGroundGlass() {
            if (this.$state.groundGlass) {
                document.documentElement.setAttribute("data-ground-glass",'glass')
                if (!this.$state.isDarkTheme) {
                    this.changeLayoutBackgroundColor('rgba(255,255,255,0.6)')
                } else {
                    this.changeLayoutBackgroundColor('rgba(20,20,20,0.6)')
                }
            } else {
                document.documentElement.setAttribute("data-ground-glass",'no-glass')
                if (!this.$state.isDarkTheme) {
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
        changeDocumentElement(colorPrimary: string) {
            document.documentElement.setAttribute("data-theme",this.$state.isDarkTheme ? 'dark' : 'light')
            document.documentElement.style.setProperty("--colorPrimary", colorPrimary)
        },
        // 获取当前主要颜色
        getColorPrimary() {
            const color = document.documentElement.style.getPropertyValue("--colorPrimary")
            this.$state.antColorPrimary = color
            return color
        },
        // 主题重置
        resetState() {
            this.$state.layoutType = settings.layoutType
            this.$state.componentSize = settings.componentSize
            this.$state.showViewTabs = settings.showViewTabs
            this.$state.isDarkTheme = localStorage.getItem("data-theme") === "dark"
            this.$state.followSystemTheme = settings.followSystemTheme
            this.$state.colorPrimary = settings.themeConfig.token.colorPrimary
            this.$state.siderTheme = settings.siderTheme
            this.$state.groundGlass = settings.groundGlass
            this.$state.affixHead = settings.affixHead
            this.$state.layoutBackgroundColor = settings.layoutBackgroundColor
            this.$state.siderBackgroundColor = settings.siderBackgroundColor
            this.$state.siderMode = settings.siderMode
            this.$state.siderWith = settings.siderWith
            this.$state.originSiderWith = settings.originSiderWith
            this.$state.routeTransition = settings.routeTransition
            this.$state.themeConfig = settings.themeConfig
            this.$state.siderGroup = settings.siderGroup
            this.$state.grayModel = settings.grayModel
            this.changeDataDark()
            this.changeGroundGlass()
        },
        // 折叠侧边栏
        foldSiderWidth()  {
            this.originSiderWith = this.siderWith
            this.siderWith = 80
        },
        // 展开侧边栏
        unfoldSiderWidth() {
            this.siderWith = this.originSiderWith
        },
        // 是否开启灰色模式
        enableGrayModel(enable?: boolean) {
            this.$state.grayModel = enable ? 'active' : 'none'
            document.documentElement.setAttribute("enable-gray-model",this.$state.grayModel)
        }
    },
})
