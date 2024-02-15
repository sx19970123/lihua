import router from "./router/index";
import NProgress from "nprogress"
import 'nprogress/nprogress.css'
import { useUserStore } from "@/stores/modules/user"
import { usePermissionStore } from "@/stores/modules/permission";
import { useViewTabsStore } from "@/stores/modules/viewTabs";
import { useThemeStore } from "@/stores/modules/theme";
import Layout from "@/layout/index.vue";
import MiddleView from "@/components/middle-view/index.vue"

import type { ResponseType } from "@/api/type";
import type { UserInfoType } from "@/api/system/user/type/user";
import type {RouterType} from "@/api/system/user/type/router";

import token from "@/utils/token"
import type {RouteRecordRaw} from "vue-router";
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
        if (userStore.userInfo.id === null) {
            userStore.getUserInfo().then((resp: ResponseType<UserInfoType>) => {
                const metaRouterList = resp.data?.routerList || []
                const staticRoutes = router.options.routes
                // 初始化动态路由
                initDynamicRouter(metaRouterList)
                // 初始化用户菜单数据
                usePermission.initMenu(metaRouterList, staticRoutes as any[])
                // 初始化totalViewTabs数据
                useViewTabs.initTotalViewTabs(resp.data?.starViewVOList || [], staticRoutes as any[])
                // 设置最近使用组件的缓存key值
                useViewTabs.setViewCacheKey(resp.data?.username || '')
                // 初始化系统主题
                useTheme.init(resp.data.sysUserVO.theme)
                // hack方法 确保addRoutes已完成
                next({...to,replace: true})
            }).catch(err => {
                console.error(err)
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
const initDynamicRouter = (metaRouterList: Array<RouterType>): void => {
    // 处理各个层级 Component
    handleRouterComponent (metaRouterList)
    // 顶级无父组件目录、页面添加layout父级
    metaRouterList.forEach(route => {
        const isPageType = route.type === 'page';
        const isMenuType = route.type === 'menu';
        if ((isPageType || (isMenuType && route.children === null)) && route.parentId === '0') {
            const parentRoute: RouteRecordRaw  = {
                children: [route as any],
                path: "/",
                component: Layout,
                name: route.name + "Parent"
            };
            router.addRoute(parentRoute);
        } else {
            router.addRoute(route as any);
        }
    });
}

/**
 * 处理各个层级 Component
 * @param metaRouterList
 */
const handleRouterComponent = (metaRouterList: Array<RouterType>) => {
    if (metaRouterList && metaRouterList.length > 0) {
        metaRouterList.forEach(route => {
            if (route.children && route.children.length > 0) {
                // 页面下有子页面的情况
                if (route.type === 'page') {
                    handleSetComponent(route)
                } else {
                    // 顶级节点
                    if (route.parentId === '0') {
                        route.component = Layout
                    }
                    // 非顶级节点
                    else {
                        route.component = MiddleView
                    }
                }
                handleRouterComponent(route.children);
            }
            // 页面组件设置 component
            else {
                handleSetComponent(route)
            }
        })
    }
}

const handleSetComponent = (route: RouterType) => {
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
