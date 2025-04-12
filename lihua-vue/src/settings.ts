import { theme } from "ant-design-vue";

/**
 * 系统信息配置
 */
export default {
    /**
     * 系统版本
     */
    version: "1.2.1",

    /**
     * 暗色模式 true； 亮色模式 false
     */
    isDarkTheme: localStorage.getItem("data-theme") === 'dark',

    /**
     * 跟随系统
     */
    followSystemTheme: true,

    /**
     * 顶部栏背景颜色
     */
    layoutBackgroundColor: 'rgba(255,255,255,1)',

    /**
     * 布局类型 sider-header / header-sider / header-content
     */
    layoutType: 'sider-header',

    /**
     * 组件大小 small / default / large
     */
    componentSize: 'default',

    /**
     * 导航模式 inline / horizontal
     */
    siderMode: 'inline',

    /**
     * 菜单分组
     */
    siderGroup: false,

    /**
     * 主题颜色可选项
     */
    colorOptions: [
        {
            name: '拂晓蓝',
            color: 'rgb(22, 119, 255)',
        },
        {
            name: '薄暮',
            color: 'rgb(245, 34, 45)',
        },
        {
            name: '火山',
            color: 'rgb(250, 84, 28)',
        },
        {
            name: '日暮',
            color: 'rgb(250, 173, 20)',
        },
        {
            name: '明青',
            color: 'rgb(19, 194, 194)',
        },
        {
            name: '极光绿',
            color: 'rgb(82, 196, 26)',
        },
        {
            name: '极客蓝',
            color: 'rgb(47, 84, 235)',
        },
        {
            name: '酱紫',
            color: 'rgb(114, 46, 209)',
        }
    ],

    /**
     * 侧边栏背景颜色
     */
    siderBackgroundColor: 'rgba(255,255,255,1)',

    /**
     * 磨砂玻璃效果
     */
    groundGlass: true,

    /**
     * 固定头部
     */
    affixHead: true,

    /**
     * 显示多窗口标签
     */
    showViewTabs: true,

    /**
     * 侧边颜色 light / dark
     */
    siderTheme: 'light',

    /**
     * 侧边宽度
     */
    siderWith: 200,

    /**
     * 原侧边宽度，用于调整侧边栏时保存临时变量
     */
    originSiderWith: 200,

    /**
     * 切换路由时的过渡动画 zoom / fade / breathe / top / down / switch / trick
     */
    routeTransition: 'zoom',

    /**
     * 灰色模式 none active
     */
    grayModel: 'none',

    /**
     * ant 主题配置
     */
    themeConfig: {
        token: {
            colorPrimary: 'rgb(22, 119, 255)'
        },
        algorithm: theme.defaultAlgorithm
    },

    /**
     * 触发菜单变化宽度
     * 视口宽度缩窄后，到达此宽度会触发菜单变化
     */
    menuToggleWidth: 768
}
