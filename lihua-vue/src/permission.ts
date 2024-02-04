import router from "./router/index";
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
import { useUserStore } from "@/stores/modules/user"
import { usePermissionStore } from "@/stores/modules/permission";
import { useViewTabsStore } from "@/stores/modules/viewTabs";
import { useThemeStore } from "@/stores/modules/theme";
import Layout from "@/layout/index.vue";
import MiddleView from "@/components/middle-view/index.vue"
import token from "@/utils/token"
const { getToken } = token
// 获取 views 下的所有 vue 组件
const modules = import.meta.glob("./views/**/*.vue")

NProgress.configure({
    showSpinner: false
})

// 路由前置守卫
router.beforeEach((to,from,next) => {
    NProgress.start()
    if (getToken()) {
        const userStore = useUserStore()
        const usePermission = usePermissionStore()
        const useViewTabs = useViewTabsStore()
        const useTheme = useThemeStore()
        // 判断是否拉取了用户信息
        if (Object.keys(userStore.userInfo).length === 0) {
            userStore.getUserInfo().then((resp:any) => {
                const metaRouterList =  resp.routerList
                const staticRoutes = router.options.routes
                // 初始化动态路由
                initDynamicRouter(metaRouterList)
                // 初始化用户菜单数据
                usePermission.initMenu(metaRouterList, staticRoutes)
                // 初始化totalViewTabs数据
                useViewTabs.initTotalViewTabs(resp.starViewVOList, staticRoutes)
                useViewTabs.setViewCacheKey(resp.username)
                useTheme.init()
                // hack方法 确保addRoutes已完成
                next({...to,replace: true})
            }).catch(err => {
                // todo 未来执行退出操作
            })
        } else {
            next()
        }

    } else {
        if (to.fullPath !== "/login") {
            next("/login")
        } else {
            next();
        }
    }

})

// 路由后置守卫
router.afterEach((to,from) => { NProgress.done() })

/**
 * 初始化动态路由
 * @param metaRouterList
 */
const initDynamicRouter = (metaRouterList: Array<any>): void => {
    // 处理各个层级 Component
    handleRouterComponent (metaRouterList)
    // 处理顶级无父菜单页面，并添加到路由
    metaRouterList.forEach(route => {
        if (route.type === 'page' && route.children === null && route.parentId === '0') {
            const parentRoute = {
                children: [route],
                path: "/",
                component: Layout,
                name: route.name + "Parent"
            }
            router.addRoute(parentRoute)
        } else {
            router.addRoute(route)
        }
    })
}

/**
 * 处理各个层级 Component
 * @param metaRouterList
 */
const handleRouterComponent = (metaRouterList: Array<any>) => {
    if (metaRouterList && metaRouterList.length > 0) {
        metaRouterList.forEach(route => {
            if (route.children && route.children.length > 0) {
                // 顶级节点
                if (route.parentId === '0') {
                    route.component = Layout
                }
                // 非顶级节点
                else {
                    route.component = MiddleView
                }
                handleRouterComponent(route.children);
            }
            // 页面组件设置 component
            else {
                for (let path in modules) {
                    const dir = path.split('views/')[1]
                    if (dir === route.component) {
                        route.component = () => modules[path]()
                    }
                }
                if (typeof route.component === 'string') {
                    route.danger = true
                    route.meta.title = "项目路径下没有找到 src/views/" + route.component + " 资源"
                }
            }
        })
    }
}
